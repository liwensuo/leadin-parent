/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.server.app;

import org.leadin.common.constant.Constants;
import org.leadin.common.context.RuntimeContext;
import org.leadin.common.repository.RunInfoRepository;
import org.leadin.server.bootstrap.ServerBootStrap;
import org.leadin.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * [服务端功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/14 21:42]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/14]
 * @Version: [v1.0]
 */
//@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.leadin.common.repository,org.leadin.analyze,org.leadin.analyze.title,org.leadin.export.handler,org.leadin.server.bootstrap,org.leadin.common.bean")
@EnableMongoRepositories(basePackages = "org.leadin.common.repository")
public class ServerApp implements CommandLineRunner {
    /**
     * 服务端入口
     *
     * @param args
     */

    @Autowired
    private RunInfoRepository runInfoRepository;

    @Autowired
    private MongoTemplate template;

    @Autowired
    private ServerBootStrap bootStrap;

    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class);
    }

    @Override
    public void run(String... strings) throws Exception {
        try {
            //系统初始化并启动
            RuntimeContext.set(Constants.CONTEXT_RUNINFOREPOSITORY,runInfoRepository);
            RuntimeContext.set(Constants.CONTEXT_MONGOTEMPLATE,template);
            if (!bootStrap.init()) {
                LogUtil.err("server init failure,system halted!");
                System.exit(-1);
            }
            bootStrap.start();
            LogUtil.info("server started!");
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.err("server init failure,system halted!");
        }
    }
}
