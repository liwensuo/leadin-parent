/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.exception;

/**
 * [事件队列异常]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/10 22:06]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/10]
 * @Version: [v1.0]
 */
public class QueueManagerException extends BaseRuntimeException {
    public QueueManagerException(Throwable t) {
        super(t);
    }
}
