/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.analyze;

import org.leadin.analyze.title.TitleAnalyzer;
import org.leadin.common.bean.BaseBean;
import org.leadin.common.bean.ClientInfoBean;
import org.leadin.common.constant.Constants;
import org.leadin.common.constant.EnumEventType;
import org.leadin.common.context.RuntimeContext;
import org.leadin.common.event.Event;
import org.leadin.common.event.events.DataReceivedEvent;
import org.leadin.common.repository.RunInfoRepository;
import org.leadin.common.util.DateUtil;
import org.leadin.common.util.JsonUtil;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.StringUtil;
import org.leadin.util.AnalyzeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * [数据解析入口]
 *
 * @ProjectName: [leadin]
 * @Author: [liushuo]
 * @CreateDate: [2015/3/3 11:10]
 * @Update:
 * @Version: [v1.0]
 */
public class LogAnalyzer {

    private static  LogAnalyzer logAnalyzer = null;

    private TitleAnalyzer titleAnalyzer;

    //@Resource
    public RunInfoRepository runInfoRepository;

    //@Autowired
    public MongoTemplate mongoTemplate;

    private LogAnalyzer(TitleAnalyzer titleAnalyzer){
        LogUtil.debug("create LogAnalyzer!");
        this.titleAnalyzer = titleAnalyzer;
        runInfoRepository = RuntimeContext.get(Constants.CONTEXT_RUNINFOREPOSITORY);
        mongoTemplate = RuntimeContext.get(Constants.CONTEXT_MONGOTEMPLATE);
    }

    /**
     * 创建解析对象--单例模式
     * @return
     */
    public static LogAnalyzer getInstance(){
        if(logAnalyzer == null){
            logAnalyzer = new LogAnalyzer(new TitleAnalyzer());
        }
        return logAnalyzer;
    }

    public boolean analyze(Event<BaseBean> event){
        BaseBean baseBean = event.getEventObject();
        //LogUtil.debug(EnumEventType.STATUS_CHANGE_EVENT+event.getSrcEventType().toString());

        if(EnumEventType.CLIENT_STATUS_EVENT.equals(event.getSrcEventType())){
            LogUtil.debug("analyze client status !");
            ClientInfoBean runInfo = JsonUtil.fromJSON(baseBean.getContent(), ClientInfoBean.class);
            String date = DateUtil.getTodayYYYYMMDD_();
            runInfo.setReceiveDate(date);
            runInfo.setReceiveTime(DateUtil.getTodayHHMMSS());
            runInfo = runInfoRepository.save(runInfo);
            //MongoDBManager.saveBsonStr(baseBean.getContent(), Constants.CLIENT_RUNTIME_INFO);
            return  true;
        }
        String tableName = baseBean.getServerIp()+baseBean.getTomcatName();
        if(StringUtil.isBlank(tableName)){
            return false;
        }
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(baseBean.getContent().getBytes(Charset.forName("utf8"))),Charset.forName("utf8")));
        List<String> logList = AnalyzeUtil.bufferToList(bufferedReader);
        LogUtil.debug("analyze file, size:"+logList.size());
        for(String logContent:logList){
            BaseBean baseBeans = new BaseBean();
            baseBeans.setServerIp(baseBean.getServerIp());
            baseBeans.setTomcatName(baseBean.getTomcatName());
            baseBeans.setContent(logContent);
            baseBeans.setTime(AnalyzeUtil.timeContent(logContent));
            mongoTemplate.save(baseBeans, tableName + Constants.BASE_LOG_ALL);
            //MongoDBManager.mongoTemplate.save(baseBeans,tableName+ Constants.BASE_LOG_ALL);
            if(AnalyzeUtil.checkException(logContent)){
                //MongoDBManager.mongoTemplate.save(baseBeans,tableName+Constants.BASE_LOG_EXCEPTION);
                mongoTemplate.save(baseBeans, tableName + Constants.BASE_LOG_EXCEPTION);
            }
        }
        try{
            bufferedReader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return titleAnalyzer.analyze(logList,tableName+Constants.BASE_LOG_ANALYZE);
    }

    public static void main(String[] a){
        LogAnalyzer logAnalyzer = new LogAnalyzer(new TitleAnalyzer());
        DataReceivedEvent event = new DataReceivedEvent();

//        String logContent = "[regiter]  2015-03-05 13:11:53  (13422222222,sure1024)  ()  ()  (INFO)  [register]" +
//                "\n[regiter]  2015-03-05 13:11:53  (13422222222,sure2048)  ()  ()  (INFO)  [register]";
        String logContent = "2015-03-20 14:21:39,936 INFO OTHER (263) - [login]  2015-03-11 14:21:39  (yzab19880000)  ()  ()  (INFO)  [login]";
        BaseBean baseBean = new BaseBean();
        baseBean.setServerIp("192.168.0.1");
        baseBean.setTomcatName("buyer");
        baseBean.setContent(logContent);
        event.setEventObject(baseBean);
        logAnalyzer.analyze(event);
    }
}