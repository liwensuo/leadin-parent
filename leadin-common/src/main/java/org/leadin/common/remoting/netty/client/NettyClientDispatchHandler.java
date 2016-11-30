package org.leadin.common.remoting.netty.client;

import org.leadin.common.event.Event;
import org.leadin.common.remoting.netty.handler.BaseNettyHandlerAdaptor;
import org.leadin.common.util.LogUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

@Sharable
public class NettyClientDispatchHandler extends BaseNettyHandlerAdaptor {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Event event = (Event) msg;

        //保存
        qm.push(event);

        LogUtil.debug("channelRead");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelRegistered(channelHandlerContext);
        LogUtil.debug("registered {}", channelHandlerContext.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelUnregistered(channelHandlerContext);
        LogUtil.debug("channelUnregistered {}", channelHandlerContext.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelActive(channelHandlerContext);
        LogUtil.debug("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelInactive(channelHandlerContext);


        LogUtil.debug("channelInactive");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelReadComplete(channelHandlerContext);
        LogUtil.debug("channelReadComplete");
    }
}
