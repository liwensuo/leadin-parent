/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.event.handler;

import org.leadin.common.event.Event;

/**
 * [事件处理接口]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/11 1:07]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/11]
 * @Version: [v1.0]
 */
public interface IEventHandler<T> {
    void handle(Event<T> event);
}
