/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.util;

import org.leadin.common.bean.LogBean;
import org.leadin.common.config.ConfigService;
import org.leadin.common.constant.Constants;
import org.leadin.common.context.RuntimeContext;
import org.leadin.common.util.DateUtil;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.StringUtil;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [日志解析工具]
 *
 * @ProjectName: [leadin]
 * @Author: [liushuo]
 * @CreateDate: [2015/3/3 11:10]
 * @Update:
 * @Version: [v1.0]
 */
public class AnalyzeUtil {

    public static LogBean analyzeLine(String logContent){
        logContent = logContent.replaceAll("\\s*","");
        if(!isLog(logContent)){
            return  null;
        }
        Pattern pattern =  Pattern.compile("\\[[a-z]+\\]\\d{4}-\\d{2}-\\d{4}:[0-9]{2}:[0-9]{2}\\([^\\(]*\\)\\([^\\(]*\\)\\([^\\(]*\\)\\([^\\(]*\\)\\[[a-z]+\\]");
        Matcher matcher = pattern.matcher(logContent);
        if(matcher.find()){
            logContent = matcher.group();
        }
        LogBean logBean = new LogBean();
        Pattern acPattern = Pattern.compile("\\[[a-z]+\\]");
        Pattern timePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{4}:[0-9]{2}:[0-9]{2}");
        Pattern contentPattern = Pattern.compile("\\([^\\(]*\\)");

        Matcher acMatcher = acPattern.matcher(logContent);
        Matcher timeMatcher = timePattern.matcher(logContent);
        Matcher contentMatcher = contentPattern.matcher(logContent);
        String actionCode = null;
        String actionTime = null;
        String object = null;
        String subject = null;
        String actionType = null;
        String level = null;

        if (acMatcher.find()){
            actionCode = acMatcher.group();
        }
        if(timeMatcher.find()){
            actionTime = timeMatcher.group();
        }
        if (contentMatcher.find()){
            object = contentMatcher.group();
        }
        if (contentMatcher.find()){
            subject = contentMatcher.group();
        }
        if (contentMatcher.find()){
            actionType = contentMatcher.group();
        }
        if (contentMatcher.find()){
            level = contentMatcher.group();
        }
        logBean.setActionCode(actionCode);
        logBean.setLevel(level);
        logBean.setMain(object);
        logBean.setTime(actionTime);
        logBean.setActionType(actionType);
        logBean.setSubject(subject);
        return logBean;
    }

    public static boolean isLog(String logContent){
        logContent = logContent.replaceAll("\\s*","");
//        Pattern pattern =  Pattern.compile("^\\[[a-z]+\\].*\\[[a-z]+\\]$");
        Pattern pattern =  Pattern.compile("\\[[a-z]+\\]\\d{4}-\\d{2}-\\d{4}:[0-9]{2}:[0-9]{2}\\([^\\(]*\\)\\([^\\(]*\\)\\([^\\(]*\\)\\([^\\(]*\\)\\[[a-z]+\\]");
        Matcher matcher = pattern.matcher(logContent);
        boolean flag = matcher.find();
//        return matcher.matches();
        return flag;
    }

    public static List<String> bufferToList(BufferedReader bufferedReader){
        List<String> logList = new ArrayList<String>();
        try{
            String lineLog;
            while ((lineLog = bufferedReader.readLine()) != null){
                logList.add(lineLog);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return logList;
    }

    public static boolean checkException(String logContent){
        ConfigService configService = RuntimeContext.get(Constants.CONTEXT_CONFIGSERVICE);
        String exceptionreg =configService.getString("EXCEPTION_REG");
        if(StringUtil.isBlank(exceptionreg)){
            LogUtil.err("EXCEPTION_REG is required!");
        }
        Pattern pattern = Pattern.compile(exceptionreg,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(logContent);
        return matcher.find();
    }
    public static Date timeContent(String logContent){
        Pattern pattern =  Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})\\s*[0-9]{2}:[0-9]{2}:[0-9]{2}");
        Matcher matcher = pattern.matcher(logContent.trim());
        if (matcher.find()){
            return DateUtil.stringToDate(matcher.group(), DateUtil.DATETIME_FORMAT);
        }else{
            LogUtil.warn("not find time str in [{}]", logContent);
            return new Date();
        }
    }
    public static String timeContent2(String logContent){
        Pattern pattern =  Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:[0-9]{2}:[0-9]{2}");
        Matcher matcher = pattern.matcher(logContent);
        if (matcher.find()){
            return matcher.group();
        }
        return "";
    }

    public static void main(String[] a ){
//        String logContent = "[regiter]  2015-03-05 13:11:53  (13422222222,sure1024)  ()  ()  (INFO)  [register]";
//        AnalyzeUtil.analyzeLine(logContent);
//        System.out.print(AnalyzeUtil.isLog(logContent));


//        LogAnalyzer logAnalyzer = new LogAnalyzer(new TitleAnalyzer());
//        DataReceivedEvent event = new DataReceivedEvent();
//        String logContent = "[regiter]  2015-03-05 13:11:53  (13422222222,sure1024)  ()  ()  (INFO)  [register]" +
//                "\n[regiter]  2015-03-05 13:11:53  (13422222222,sure1024)  ()  ()  (INFO)  [register]";
//        event.setEventObject("logContent");
//        logAnalyzer.analyze(event);
        String logContent = "2015-03-20 18:31:39.595 [Thread-/home/apache-tomcat/logs/p_seller&&&seller--readFile] DEBUG leadin - QueueManager->push(230)：qm received:Event{eventNo='null', eventType=DATA_COLLECTED_EVENT, eventObject=ID:null ;content:14:36:34,223 DEBUG org.jasig.cas.client.session.SingleSignOutHandler (153) - Error invalidating session.\n" +
                "java.lang.IllegalStateException: invalidate: Session already invalidated\n" +
                "\tat org.apache.catalina.session.StandardSession.invalidate(StandardSession.java:1273)\n" +
                "\tat org.apache.catalina.session.StandardSessionFacade.invalidate(StandardSessionFacade.java:188)\n" +
                "\tat org.jasig.cas.client.session.SingleSignOutHandler.destroySession(SingleSignOutHandler.java:151)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:241)\n" +
                "\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:208)\n" +
                "\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:220)\n" +
                "\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:122)\n" +
                "}";
        System.out.print(timeContent(logContent));

    }

}
