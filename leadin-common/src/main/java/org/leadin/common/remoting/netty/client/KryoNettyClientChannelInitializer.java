package org.leadin.common.remoting.netty.client;

import org.leadin.common.remoting.netty.codec.KryoDecoder;
import org.leadin.common.remoting.netty.codec.KryoEncoder;
import org.leadin.common.remoting.netty.codec.KryoPool;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class KryoNettyClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private KryoPool kryoSerializationFactory;

    public KryoNettyClientChannelInitializer() {
        //此处可以优化大小
        this.kryoSerializationFactory = new KryoPool();
    }

    protected void initChannel(final SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new KryoEncoder(kryoSerializationFactory));
        ch.pipeline().addLast(new KryoDecoder(kryoSerializationFactory));
        //send biz events
        ch.pipeline().addLast(new NettyClientDispatchHandler());
    }
}
