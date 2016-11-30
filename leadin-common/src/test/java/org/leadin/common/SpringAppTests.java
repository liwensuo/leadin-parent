package org.leadin.common;

import org.leadin.common.remoting.netty.client.NettyClient;
import org.leadin.common.remoting.netty.server.NettyServer;
import org.leadin.common.util.LogUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.InetSocketAddress;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class SpringAppTests {
    @Test
    public void testNettyServer() throws Exception{
        NettyServer server = new NettyServer();
        server.start(8000);
        LogUtil.debug("server started");
    }
    @Test
    public void testNettyClient() throws Exception{
        NettyClient client = new NettyClient();
        client.init(new InetSocketAddress(8000));
        client.connect();
        LogUtil.debug("start connect");
    }
}
