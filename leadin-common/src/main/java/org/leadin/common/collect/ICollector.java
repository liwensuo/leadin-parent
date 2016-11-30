/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.collect;

import org.leadin.common.exception.CollectException;

/**
 * [收集接口]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/11 0:00]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/11]
 * @Version: [v1.0]
 */
public interface ICollector<T> {
    void collect(T data) throws CollectException;
}
