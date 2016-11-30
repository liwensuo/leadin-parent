/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.cache.model;

/**
 * [缓存对象]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/10 22:46]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/10]
 * @Version: [v1.0]
 */
@Deprecated
public class CacheObject<T> {
    /**
     * 缓存数据的UUID号码，用来唯一标识缓存数据
     */
    private String cacheNo;
    private T data;


    public String getCacheNo() {
        return cacheNo;
    }

    public void setCacheNo(String cacheNo) {
        this.cacheNo = cacheNo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return this.getCacheNo();
    }
}
