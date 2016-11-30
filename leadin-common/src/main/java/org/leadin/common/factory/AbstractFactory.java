/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.factory;

/**
 * [抽象工厂]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/14 20:44]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/14]
 * @Version: [v1.0]
 */
public abstract class AbstractFactory<T> {
    /**
     * 储存单例模式创建的实例
     */
    //private final Map<Enum,T> singleTonCacheMap = new HashMap<>();
    abstract T create(String clazz);
}
