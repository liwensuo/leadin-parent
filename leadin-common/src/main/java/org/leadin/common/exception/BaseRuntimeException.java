/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.exception;

import org.leadin.common.event.Event;
import org.leadin.common.event.events.ExceptionEvent;
import org.leadin.common.queue.BlackBoxQueue;
import org.leadin.common.util.StatUtil;

import java.util.concurrent.BlockingQueue;

/**
 * [异常父类]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/10 22:05]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/10]
 * @Version: [v1.0]
 */
public class BaseRuntimeException extends RuntimeException {
    /**
     * 黑匣子
     */
    protected static BlockingQueue<Event> queue = BlackBoxQueue.getEQ();

    public BaseRuntimeException(Throwable t) {
        super(t);
    }

    public BaseRuntimeException(String msg) {
        super(msg);
    }

    /**
     * [发生异常构建异常事件向中控调度申请暂停]
     *
     * @param event
     */
    public void addExceptionEvent(Event event) {
        BlackBoxQueue.getEQ().add(new ExceptionEvent(this.getMessage(),event));
        StatUtil.stat(event,this);
    }
}
