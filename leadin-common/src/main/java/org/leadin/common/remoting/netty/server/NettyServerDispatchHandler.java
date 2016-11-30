package org.leadin.common.remoting.netty.server;

import org.leadin.common.constant.EnumEventType;
import org.leadin.common.event.Event;
import org.leadin.common.event.events.AckEvent;
import org.leadin.common.exception.QueueManagerException;
import org.leadin.common.remoting.netty.handler.BaseNettyHandlerAdaptor;
import org.leadin.common.util.LogUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

@Sharable
public class NettyServerDispatchHandler extends BaseNettyHandlerAdaptor {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Event event = (Event) msg;

        LogUtil.debug("server received event:{}", event);

        EnumEventType sourceEventType = event.getEventType();

        //保存并发出 DataReceivedEvent
        event.setEventType(EnumEventType.DATA_RECEIVED_EVENT);
        try {
            qm.push(event);

            //ClientStatusEvent特殊处理,不用发回执
            if(event.getSrcEventType().equals(EnumEventType.CLIENT_STATUS_EVENT)){
                return;
            }

            //发出ack
            AckEvent ack = new AckEvent(event.getEventNo(), sourceEventType);
            ctx.writeAndFlush(ack);

        } catch (QueueManagerException ex) {
            LogUtil.err("error in put Queue: {}!", ex.getMessage());
        }
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
        LogUtil.debug("channelActive {}", channelHandlerContext.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelInactive(channelHandlerContext);
        LogUtil.debug("channelInactive {}", channelHandlerContext.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.channelReadComplete(channelHandlerContext);
        LogUtil.debug("channelReadComplete");
    }
}
