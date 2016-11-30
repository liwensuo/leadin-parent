/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.event.handler;

import org.leadin.common.constant.Constants;
import org.leadin.common.context.RuntimeContext;
import org.leadin.common.queue.QueueManager;

/**
 * [事件处理基类]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/11 1:07]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/11]
 * @Version: [v1.0]
 */
public class BaseHandler {
    public QueueManager qm;
    public BaseHandler(){
        this.qm = RuntimeContext.get(Constants.CONTEXT_QUEUEMANAGER);
    }
}
