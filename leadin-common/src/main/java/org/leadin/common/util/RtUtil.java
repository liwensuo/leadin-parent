/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.util;

import org.leadin.common.config.ConfigService;
import org.leadin.common.constant.Constants;
import org.leadin.common.context.RuntimeContext;
import org.leadin.common.queue.QueueManager;

/**
 * [简化从RuntimeContext取对象的写法]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/3/9 20:20]
 * @Update: [说明本次修改内容] BY[tophawk][2015/3/9]
 * @Version: [v1.0]
 */
public class RtUtil {
    private RtUtil() {
    }

    /**
     * 获取全局的configService
     * @return
     */
    public static ConfigService getConfigService(){
        return RuntimeContext.get(Constants.CONTEXT_CONFIGSERVICE);
    }

    /**
     * 获取全局的QueueManager
     * @return
     */
    public static QueueManager getQueueManager(){
        return RuntimeContext.get(Constants.CONTEXT_QUEUEMANAGER);
    }
    public static boolean isServer(){
        return RuntimeContext.get(Constants.CONTEXT_ISSERVER);
    }
}
