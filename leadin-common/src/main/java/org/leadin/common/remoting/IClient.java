/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.remoting;

import org.leadin.common.event.Event;

import java.net.InetSocketAddress;

/**
 * [简短描述该类的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/3/3 13:50]
 * @Update: [说明本次修改内容] BY[tophawk][2015/3/3]
 * @Version: [v1.0]
 */
public interface IClient {
    /**
     * 初始化
     *
     * @param socketAddress
     */
    void init(InetSocketAddress socketAddress);

    void connect();

    boolean isConnected();

    void sent(Event obj);

    InetSocketAddress getRemoteAddress();

    void close();
}
