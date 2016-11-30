/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.util;

import org.leadin.common.bean.BaseBean;
import org.leadin.common.config.ConfigService;
import org.leadin.common.constant.Constants;
import org.leadin.common.context.RuntimeContext;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [文件操作的工具类]
 *
 * @ProjectName: [leadin]
 * @Author: [lixu]
 * @CreateDate: [2015/2/10 22:30]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/10]
 * @Version: [v1.0]
 */
public class FileUtil {
    /**
     * 缓存大小
     */
    private static final int BUFFER_SIZE = 1024 * 1024;

    private FileUtil() {
    }


    /**
     * 获取路径下特定文件类型的文件列表
     *
     * @param filePath 文件目录
     * @param fileType 文件类型
     * @return 没有文件返回null
     */
    public static List<File> getFilesByDirection(String filePath, final String fileType) {
        File[] e = (new File(filePath)).listFiles(new FileFilter() {
            public boolean accept(File file) {
                boolean result = file.isFile();
                if (fileType != null && !fileType.equals("")) {
                    if (file.getName().endsWith(fileType)) {
                        return result;
                    }
                    return false;
                } else {
                    return result;
                }

            }
        });
        if (e != null && e.length > 0) {
            return Arrays.asList(e);
        }
        return null;
    }

    /**
     * 读取文件，以指定编码方式返回文件内容
     *
     * @param path 文件的全路径
     * @param enc  编码
     * @return
     * @throws IOException
     */
    public static final String getFileContent(String path, String enc)
            throws IOException {
        StringBuffer bufLine = new StringBuffer();
        try {
            File f = new File(path);
            if (f.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(f), enc);
                BufferedReader br = new BufferedReader(read);
                String line = br.readLine();
                while (line != null) {
                    bufLine.append(line).append("\r\n");
                    line = br.readLine();
                }
                br.close();
                read.close();
                br = null;
                read = null;
            }

        } catch (IOException e) {
            throw e;
        }
        return bufLine.toString();
    }


    /**
     * 创建文件
     *
     * @param data          文件内容
     * @param fileLocation  文件放置的地址
     * @param fileName      文件名称
     * @param isOverFile    判断是否在覆盖文件 true--支持在存在的文件中写入 /false--不支持在存在的文件写入
     * @param isOverContent 判断文件内容是覆盖还是追加写入  true -- 覆盖写入 / false 在文件中追加写入
     * @return
     */
    public static boolean createFile(String data, String fileLocation, String fileName, boolean isOverFile, boolean isOverContent) {
        boolean pathExist = validyPath(fileLocation);
        String totalFilePath = StringUtil.join(fileLocation, File.separator, fileName);
        if (pathExist) {
            File file = new File(totalFilePath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    LogUtil.err("the fileName:{} create error ,the reason is {}", totalFilePath, e.getMessage());
                    throw new RuntimeException(StringUtil.join("The file [", totalFilePath, "] create error , the reason is:", e.getMessage()));
                }
            } else {
                if (!isOverFile) {
                    LogUtil.err("The file {} is already exists", totalFilePath);
                    throw new RuntimeException(StringUtil.join("The file [", totalFilePath, "] is already exists ,cannot cover the file"));
                }
            }
            BufferedWriter bfw = null;
            try {
                bfw = new BufferedWriter(new FileWriter(file, isOverContent));
                bfw.write(data);
            } catch (IOException e) {
                LogUtil.err("the file:{} write data error , the reason is ", file.getName(), e.getMessage());
                throw new RuntimeException(StringUtil.join("The file[", file.getName(), "] write data  error ,the error message :", e.getMessage()));
            } finally {
                try {
                    bfw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    bfw = null;
                }
            }
        }
        return true;
    }

    /**
     * 按照文件全路径进行删除文件操作
     *
     * @param filePath
     * @return true -- 删除文件成功/false -- 删除文件失败
     */
    public static boolean deleteFileByPath(String filePath) {
        File file = new File(filePath);
        boolean isDelete = true;
        if (file.exists()) {
            isDelete = file.delete();
        }
        return isDelete;
    }


    /**
     * 校验路径，没有路径则创建路径
     *
     * @param path
     * @return
     */
    public static final boolean validyPath(String path) {
        String[] arraypath = path.split("/");
        String tmppath = "";
        for (int i = 0; i < arraypath.length; i++) {
            tmppath += "/" + arraypath[i];
            File d = new File(tmppath);
            if (!d.exists()) {
                if (!d.mkdir()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 读取文件内容，转换成String对象返回
     *
     * @param file
     * @return
     */
    public static String getFileContents(File file) {
        StringBuffer lineContentBuff = new StringBuffer();
        if (file != null && file.exists() && file.isFile()) {
            InputStreamReader reader = null;
            BufferedReader bufferReader = null;
            try {
                reader = new InputStreamReader(new FileInputStream(file));
                bufferReader = new BufferedReader(reader);
                String lineContent = bufferReader.readLine();
                while (lineContent != null) {
                    if (!lineContent.trim().equals("")) {
                        lineContentBuff.append(lineContent).append("\r\n");
                    }
                    lineContent = bufferReader.readLine();
                }
            } catch (Exception e) {
                LogUtil.err("Read the file:{} error , the error reason is:{}", file.getName(), e.getMessage());
                throw new RuntimeException(StringUtil.join("Read the file [", file.getName(), "] error , the reason is ", e.getMessage()));
            } finally {
                try {
                    bufferReader.close();
                    reader.close();
                } catch (IOException e) {
                    LogUtil.err("the read stream cannot close , the reason is {}", e.getMessage());
                    throw new RuntimeException(StringUtil.join("the read stream cannot close , the reason is ", e.getMessage()));
                } finally {
                    bufferReader = null;
                    reader = null;
                }
            }
        }
        return lineContentBuff.toString();

    }

    public static File getSigleFile(String fileTotalPath) {
        File file = new File(fileTotalPath);
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }

    public static boolean renameFile(File targetFile, String fileTotalPath) {
        try {
            File file = new File(fileTotalPath);
            if (file.exists()) {
                LogUtil.err("target file :{} is exists, please check the data", fileTotalPath);
                return false;
            }
            targetFile.renameTo(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.err("the target file:{} cannot remove to the file :{}", targetFile.getName(), fileTotalPath, e.getMessage());
            throw e;
        }
    }

    public static String readFileContext(File file) {
        FileChannel fis = null;
        StringBuffer strBuffer = new StringBuffer();
        try {
            fis = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            byte[] bytes = new byte[BUFFER_SIZE];
            String tempString = null;
            while (fis.read(buffer) != -1) {
                int rSize = buffer.position();
                buffer.rewind();
                buffer.get(bytes);
                buffer.clear();
                tempString = new String(bytes, 0, rSize);
                strBuffer.append(tempString);
            }
            LogUtil.info("read the file {} success", file.getName());
        } catch (FileNotFoundException e) {
            LogUtil.err("the file:{} is not exists", e.getMessage());
            throw new RuntimeException(StringUtil.join("the file [", file.getName(), "] read error ,the error reason is ", e.getMessage()));
        } catch (Exception e) {
            LogUtil.err("read the file:{} error ,the reason is:{}", e.getMessage());
            throw new RuntimeException(StringUtil.join("read the file [", file.getName(), "] error ,the reason is ", e.getMessage()));
        } finally {
            try {
                fis.close();
                fis = null;
                return strBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(StringUtil.join("read the file [", file.getName(), "] error ,the reason is ", e.getMessage()));
            }

        }
    }

    /**
     * 按照指定指针和指定行读取文件内容
     * @param row  要开始读取的行数
     * @param file  要开始读取的文件
     * @return
     */
    /**
     * 按照指定指针，指定行数，编码方式和开始读取的位置
     *
     * @param row     指定读取行数
     * @param file    要读取的文件对象
     * @param encoder 编码方式
     * @param map     放入读取文件的细节信息，为了以后进行定位
     * @return
     */
    public static String readFileByPosition(int row, File file, String encoder, Map map) {
        RandomAccessFile logFile = null;
        StringBuilder list = new StringBuilder();
        int readLine = 0;
        try {
            //以只读方式打开日志文件
            logFile = new RandomAccessFile(file, "r");
            //计算要读取的行号，从末行开始读取，所以末行行号为0
            //获取文件大小
            long len = logFile.length();
            long position = FileUtil.readFilePointer(file.getAbsolutePath());
            map.put("start_position", position);
            if (position == len) {
                return null;
            } else if (position > len) {
                //文件的字符数小于记录的读取文件字符数的时候，这样表示文件出现了异常信息，由于日志文件从新生成，这样就需要把指针文件重置
                writerFilePointer(file.getAbsolutePath(), 0);
                position = 0;
            }
            logFile.seek(position);
            if (len > 0) {
                //读取一行,肯定是新行
                String line = logFile.readLine();
                if (line != null) {
                    line = getEncodeValue(line, encoder);
                    list.append(line);
                    position = logFile.getFilePointer();


                    String lineSub = null;
                    while ((lineSub = logFile.readLine()) != null && position <= len) {
                        //readLine++;
                        lineSub = getEncodeValue(lineSub,encoder);

                        BaseBean baseBean = new BaseBean();
                        baseBean.setContent(lineSub);
                        if (!analysisIntegrityInfo(baseBean)) {//不是起始行
                            list.append(" ").append(lineSub);
                            position = logFile.getFilePointer();
                        } else {//是起始行
                            readLine++;
                            if (readLine == row) {
                                //退回一行
                                list.append("\r\n");
                                logFile.seek(position);
                                break;
                            }else{
                                list.append("\r\n").append(baseBean.getContent());
                                //记录当前位置
                                position = logFile.getFilePointer();
                            }
                        }
                    }
                    //最后一行计数器
                    if(lineSub == null && position == len){
                        readLine++;
                    }
                }
            }
            try {
                long endFilePosition = logFile.getFilePointer();
                writerFilePointer(file.getAbsolutePath(), endFilePosition);
                map.put("end_position", endFilePosition);
                map.put("read_row", readLine);
                if (logFile != null) logFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //读取文件首行
            if (list.length() == 0) {  //没有读取到任何数据，表示已经加载过了所有内容
                LogUtil.info("the file {}  has readed completed!", file.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtil.info("It is not exists the file ;{} ", file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.err("the file {}  read failed! " + file.getName());
        } finally {
            try {
                logFile.close();
                logFile = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println("==========++++++++++++++++++");
//        System.out.println("==========read："+list.toString());
//        System.out.println("==========++++++++++++++++++");
        return list.toString();
    }

    /**
     * 获取指定文件读取的指针，为了进行接下来文件的读取
     *
     * @param fileContext 文件中要读取的内容
     * @return
     */
    public static synchronized long readFilePointer(String fileContext) {
        InputStream in = null;
        try {
            String fileLocation = RtUtil.getConfigService().getString("cache.pointer.file.path");
            boolean pathExist = validyPath(fileLocation);
            if (!pathExist) {
                throw new RuntimeException("the location of pointer file is error");
            }
            File file = new File(fileLocation, RtUtil.getConfigService().getString("cache.pointer.file.name"));
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new RuntimeException("The pointer file create failed!");
                }
            }
            Properties proper = new Properties();
            in = new BufferedInputStream(new FileInputStream(file));
            proper.load(in);
            String value = proper.getProperty(fileContext);
            if (value == null || value.equals("")) {
                return 0;
            } else {
                return Long.parseLong(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
        }
    }

    /**
     * 获取指定文件读取的指针，为了进行接下来文件的读取,在最开始读取文件的时候
     *
     * @param fileContext 文件中要读取的内容
     * @param pointer     文件中指针值
     * @return
     */
    public static synchronized void writerFilePointer(String fileContext, long pointer) {
        OutputStream fos = null;
        try {
            String fileLocation = RtUtil.getConfigService().getString("cache.pointer.file.path");
            boolean pathExist = validyPath(fileLocation);
            if (!pathExist) {
                throw new RuntimeException("the location of pointer file is error");
            }
            File file = new File(fileLocation, RtUtil.getConfigService().getString("cache.pointer.file.name"));
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new RuntimeException("The pointer file create failed!");
                }
            }
            String profilePath = StringUtil.join(RtUtil.getConfigService().getString("cache.pointer.file.path"), File.separator, RtUtil.getConfigService().getString("cache.pointer.file.name"));
            Properties props = new Properties();
            props.load(new FileInputStream(profilePath));
            fos = new FileOutputStream(profilePath);
            props.setProperty(fileContext, Long.toString(pointer));

            props.store(fos, StringUtil.join("updata ", fileContext, " value =", Long.toString(pointer)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
        }
    }

    public static String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    /**
     * 进行异常或完整一条日志记录进行的分析，异常信息或者一条完整的日志记录信息必须整合为一行记录传递出去不可分割，目前通过日志中首起行的日期时间来判断时间来判断
     *
     * @param bean
     * @return
     */
    private static boolean analysisIntegrityInfo(BaseBean bean) {
        String content = bean.getContent();
        boolean result = false;
        String timePatternReg = RtUtil.getConfigService().getString("collect.log.date.reg");
        String clockPatternReg = RtUtil.getConfigService().getString("collect.log.clock.reg");
        //首先进行日期上面的判断
        String[] timeRegs = timePatternReg.split("&&&");
        String newTimeStr = null;
        for (String timeReg : timeRegs) {
            Pattern p = Pattern.compile(timeReg);
            Matcher m = p.matcher(content);
            if (m.find()) {
                String token = m.group();
                if (content.startsWith(token)) {
                    //日志开头是日期格式，确认
                    newTimeStr = formatPatter(token, "-");
                    content = StringUtil.join(newTimeStr, content.substring(token.length()));
                    result = true;
                    break;
                }
            }
        }

        if (!result) {
            String[] clockRegs = clockPatternReg.split("&&&");
            for (String clockReg : clockRegs) {
                Pattern p = Pattern.compile(clockReg);
                Matcher m = p.matcher(content);
                if (m.find()) {
                    String token = m.group();
                    if (newTimeStr != null) {
                        if (content.substring(newTimeStr.length()).trim().startsWith(token)) {
                            //以时间开始为标准
                            content = content.replaceFirst(token, formatPatter(token, ":"));
                            result = true;
                            break;
                        }
                    } else {
                        if (content.trim().startsWith(token)) {
                            String newToken = formatPatter(token, ":");
                            content = content.replaceFirst(token, newToken);
                            String dayTime = compareCriticalTime(newToken);
                            content = StringUtil.join(dayTime, " ", content);
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        bean.setContent(content);
        return result;
    }


    private static String formatPatter(String timeContext, String newToken) {
        String seperate = RtUtil.getConfigService().getString("collect.time.seperate");
        String[] seperateTokens = seperate.split("&&&");
        for (String token : seperateTokens) {
            if (timeContext.contains(token)) {
                return timeContext.replace(token, newToken);
            }
        }
        return timeContext;
    }

    /**
     * 进行临界时间比较，由于读取日志文件和程序处理时间有差异，当在接近一天结束时间读取的日志可能程序处理时间为第二天，这时会导致日志时间处理异常，需要对临界时间进行比较
     *
     * @param time 时间格式为xx:xx:xx,如23:59:59
     * @return
     */
    private static String compareCriticalTime(String time) {
        String context = DateUtil.getToday();
        if (time.startsWith("23:59") || time.startsWith("23:58")) {
            String dateFormat = "HH:mm:ss";
            String calendarFormat = "yyyy-MM-dd";
            DateFormat df = new SimpleDateFormat(dateFormat);
            try {
                Date currentDate = new Date();
                java.util.Date d1 = df.parse(time);
                java.util.Date d2 = df.parse(df.format(currentDate));
//                java.util.Date d2 = df.parse("00:00:01");
                if (d2.getTime() < d1.getTime()) {
                    //通过临界点时,表示已经是第二天的时间
                    Calendar calendar = Calendar.getInstance(); //得到日历
                    calendar.setTime(currentDate);//把当前时间赋给日历
                    calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
                    Date beforeDate = calendar.getTime();   //得到前一天的时间
                    DateFormat calendarDf = new SimpleDateFormat(calendarFormat);
                    return calendarDf.format(beforeDate);
                }
                return context;
            } catch (Exception e) {
                LogUtil.err("The time format is invalie , the erro info :{}", e.getMessage());
                return context;
            }
        } else {
            return context;
        }

    }

    public static String getEncodeValue(String param, String encoder) {
        try {
            return new String(param.getBytes("8859_1"), encoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return param;
        }
    }

    public static void main(String[] args) {
        ConfigService cs = new ConfigService();
        cs.init();
        RuntimeContext.set(Constants.CONTEXT_CONFIGSERVICE, cs);
//        System.out.println(FileUtil.compareCriticalTime("15:37:12"));
//        System.out.println(FileUtil.compareCriticalTime("23:58:59"));

        BaseBean pojo = new BaseBean();
        pojo.setContent("03-24 11:36:02.515 0    INFO  [main] cn..framework.ds.DataSourceManager     - created default DataSourceFactory: cn..framework.plugin.druid.DruidDataSourceFactory@573fd745");
        System.out.println(FileUtil.analysisIntegrityInfo(pojo) + "=>" + pojo);
    }
}