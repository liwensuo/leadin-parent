/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common;

import org.leadin.common.event.Event;
import org.leadin.common.event.events.DataCollectedEvent;
import org.leadin.common.remoting.netty.client.NettyClient;
import org.leadin.common.util.LogUtil;

import java.net.InetSocketAddress;

/**
 * [简短描述该类的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/3/5 18:05]
 * @Update: [说明本次修改内容] BY[tophawk][2015/3/5]
 * @Version: [v1.0]
 */
public class Test {
    public static void main(String args[]) throws Exception{

//        NettyServer server = new NettyServer();
//        server.start(8000);
//        LogUtil.debug("server started");


        NettyClient client = new NettyClient();
        client.init(new InetSocketAddress(8000));
        client.connect();
        LogUtil.debug("start connect");

        while(true) {
            Thread.sleep(2000);
            Event event = new DataCollectedEvent();
            client.sent(event);
        }
    }
}
