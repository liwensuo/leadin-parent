/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.analyze.file;

import org.leadin.common.bean.LogBean;
import org.leadin.common.util.LogUtil;
import org.leadin.util.AnalyzeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * [日志解析]
 *
 * @ProjectName: [leadin]
 * @Author: [liushuo]
 * @CreateDate: [2015/3/3 11:10]
 * @Update:
 * @Version: [v1.0]
 */
public class FileAnalyzer extends RecursiveTask<Boolean> {

    private List<String> logList;
    private int lineNum;
    private String tableName;

    @Autowired
    private MongoTemplate mongoTemplate;

    public FileAnalyzer(List<String> logList,int lineNum,String tableName){
        this.logList = logList;
        this.lineNum = lineNum;
        this.tableName = tableName;
    }

    @Override
    public Boolean compute(){
        boolean flag = false;

        if(lineNum < logList.size()){

            String logContent = logList.get(lineNum);
            LogBean logBean = AnalyzeUtil.analyzeLine(logContent);
            if(null == logBean){
                FileAnalyzer fileAnalyzer = new FileAnalyzer(logList,lineNum+1,tableName);
                fileAnalyzer.fork();
                flag = true;
                flag = flag && fileAnalyzer.join();
            }else {
                mongoTemplate.save(logBean,tableName);
                //MongoDBManager.mongoTemplate.save(logBean,tableName);
                FileAnalyzer fileAnalyzer = new FileAnalyzer(logList,lineNum+1,tableName);
                fileAnalyzer.fork();
                flag = true;
                flag = flag && fileAnalyzer.join();
            }
        }else {
            flag = true;
        }
        LogUtil.debug("line number is "+lineNum+",analyze success");
        return flag;
    }
}