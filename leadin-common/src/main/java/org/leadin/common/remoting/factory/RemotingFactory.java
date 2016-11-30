/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.remoting.factory;

import org.leadin.common.remoting.IClient;
import org.leadin.common.remoting.IServer;
import org.leadin.common.remoting.netty.factory.NettyRemotingFactory;

/**
 * [简短描述该类的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/3/3 14:11]
 * @Update: [说明本次修改内容] BY[tophawk][2015/3/3]
 * @Version: [v1.0]
 */
public abstract class RemotingFactory {
    private static final RemotingFactory factory = new NettyRemotingFactory();

    public static RemotingFactory getDefaultFactory() {
        return factory;
    }

    public abstract IClient createClient();

    public abstract IServer createServer();
}
