/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.collect.thread;

import org.leadin.common.bean.BaseBean;
import org.leadin.common.config.ConfigService;
import org.leadin.common.constant.Constants;
import org.leadin.common.util.FileUtil;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.RtUtil;
import org.leadin.common.util.StringUtil;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * [负责文件日志信息主动收集-开放平台]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/11 0:06]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/11]
 * @Version: [v1.0]
 */
public class PTLogCollector extends CollectThread {
    private final ConfigService configService = RtUtil.getConfigService();

    @Override
    public void run() {
        try {
            //在配置文件中配置读取的数据源路径，几个数据源用几个线程分别读取数据
            String[] filePaths = configService.getString("cache.log.collect.path").split(",");
            int size = filePaths.length;
            if (size > 0) {
                ExecutorService executor = Executors.newFixedThreadPool(size);
                for (int i = 0; i < size; i++) {
                    executor.submit(new ReadLogFileRunnable(filePaths[i].trim(), configService));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.sendErr(ex);
        }
    }

    /**
     * 单文件线程类
     */
    class ReadLogFileRunnable implements Runnable {
        private String filePath;
        private String tomcatName;
        private String ip;
        /**
         * 用于实时读取配置信息
         */
        private final ConfigService configService;
        private Map<String, Object> transferInfo = new HashMap<String, Object>();

        public ReadLogFileRunnable(String filePath, ConfigService configService) {
            if (filePath.contains("&&&")) {
                this.filePath = filePath.split("&&&")[0];
                this.tomcatName = filePath.split("&&&")[1];
            } else {
                LogUtil.info("config properties not includ  data source name!");
                this.filePath = filePath;
                this.tomcatName = "";
            }
            ip = RtUtil.getConfigService().getString("local.ip.address");
            this.configService = configService;
        }

        @Override
        public void run() {
            try {
                //获取路径指定路径下的日志文件
                File file = new File(filePath);
                if (!file.exists() || !file.isFile()) {
                    LogUtil.err("the file {} not exists， please check the file data", filePath);
                    return;
                }
                //获取文件编码格式，为了解决中文乱码问题
                String encoder = FileUtil.getFilecharset(file);
                LogUtil.debug("==================charset="+encoder);
                while (true) {
                    //重新获取配置信息
                    //每次读取数据的行数
                    int readLine = configService.getInt("collect.file.read.line");
                    //读取完以后等待时间
                    long readInterval = configService.getLong("cache.read.interval");

                    String value = FileUtil.readFileByPosition(readLine, file, encoder, transferInfo);
                    //加一层过滤，去掉不必要的数据 todo:可加过滤事件做扩展
                    value = filterData(value);
                    if (value != null) {
                        BaseBean bean = new BaseBean();
                        bean.setContent(value);
                        bean.setTomcatName(tomcatName);
                        bean.setServerIp(ip);
                        //发送处理
                        boolean collect = sendCollectData(bean);
                        //失败重试
                        if (!collect) {
                            int retry = RtUtil.getConfigService().getInt("collect.retry");
                            if (retry == 0) retry = Constants.SYSTEM_FAIL_RETRY;
                            int count = retry;
                            while (count-- > 0 || (collect = sendCollectData(bean))) {
                                try {
                                    Thread.sleep(Constants.SYSTEM_FAIL_RETRY_INTERVAL);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (count <= 0 && (!collect)) {
                                //将失败数据放入到存放错误文件的路径下面，便于之后进行数据分析和传输
                                LogUtil.err("all of {} retries of the data {} is fail,push to errorFile!", retry, file.getName());
                                writerErrorFile(file.getName(), bean);
                            }
                        }
                        LogUtil.info("the current thread:{} read the file:{} success ,has read file from  {} to {}  the line number is {}", Thread.currentThread().getName(), file.getName(), transferInfo.get("start_position"), transferInfo.get("end_position"), transferInfo.get("read_row"));
                    }

                    Thread.sleep(readInterval);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.err("read the file :{} error,the error info is {}", filePath, e.getMessage());
            }
        }
    }

    /**
     * 发送收集的数据，截获异常信息
     *
     * @param bean
     * @return
     */
    private boolean sendCollectData(BaseBean bean) {
        try {
            collector.collect(bean);
        } catch (Exception e) {
            LogUtil.err("collect the data is error, the error info is :{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 收集数据处理不成功，将把数据存入的错误文件中，便于以后进行分析
     *
     * @param fileName
     * @param bean
     */
    private void writerErrorFile(String fileName, BaseBean bean) {
        try {
            String errorFilePath = RtUtil.getConfigService().getString("collect.fail.path");
            StringBuffer newFileName = new StringBuffer();
            newFileName.append(fileName.substring(0, fileName.indexOf(".")));
            String prefix = StringUtil.join(bean.getServerIp(), "&", bean.getTomcatName());
            String encoderStr = (new BASE64Encoder()).encode(prefix.getBytes());
            newFileName.append("_").append(encoderStr).append(fileName.substring(fileName.indexOf(".")));
            FileUtil.createFile(bean.getContent(), errorFilePath, newFileName.toString(), true, true);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.err("the error log info store in file error ,the error info :{}", bean.toString());
            return;
        }
        LogUtil.err("error log info store in file success");
    }

    /**
     * 对收集的数据进行简单过滤
     *
     * @param value
     * @return
     */
    private String filterData(String value) {
        if(value == null) return value;
        //针对open-api
        String filterStr = RtUtil.getConfigService().getString("collect.start.class.filter");
        if (StringUtil.isBlank(filterStr)) return value;

        String[] filters = filterStr.split("\\,");
        String[] lines = value.split("\\n");
        StringBuilder buf = new StringBuilder();

        //多行，用不同的过滤词进行过滤
        for(String line:lines) {
            boolean bAdd = true;
            for (String s : filters) {
                if (line.contains(s)) {
                    System.out.println("过滤：" + line);
                    bAdd = false;
                    break;
                }
            }
            if(bAdd){
                buf.append(line).append("\n");
            }
        }
        if(buf.length()>0) return buf.toString();
        return null;
    }
}
