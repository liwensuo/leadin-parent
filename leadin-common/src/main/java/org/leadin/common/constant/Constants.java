/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.constant;

/**
 * [简短描述该类的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/15 0:39]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/15]
 * @Version: [v1.0]
 */
public interface Constants {
    /**
     * 以下为要装载的单例类的Key
     */
    String CLASS_CACHE_IMPL_KEY = "cache.class";


    /**
     * 以下是用于RuntimeContext中的常量，取值1-100
     */
    int CONTEXT_QUEUEMANAGER = 1;
    int CONTEXT_COLLECTOR = 2;
    int CONTEXT_CACHESERVICE = 3;
    int CONTEXT_CONFIGSERVICE = 4;
    int CONTEXT_ISSERVER=5;
    int CONTEXT_RUNINFOREPOSITORY=6;
    int CONTEXT_MONGOTEMPLATE=7;
    /**
     * 系统默认失败重试的次数
     */
    int SYSTEM_FAIL_RETRY =10;
    /**
     * 系统默认失败重试的间隔
     */
    long SYSTEM_FAIL_RETRY_INTERVAL=500;
    /**
     * 存储原日志内容的表名后缀
     */
    String BASE_LOG_ALL="all";
    /**
     * 存储解析后日志内容的表名后缀
     */
    String BASE_LOG_ANALYZE="analyze";
    /**
     * 存储异常日志信息，包含格式化的和未捕获的
     */
    String BASE_LOG_EXCEPTION="exception";
    /**
     * 存储客户端运行时监控信息
     */
    String CLIENT_RUNTIME_INFO="client_run_info";
}
