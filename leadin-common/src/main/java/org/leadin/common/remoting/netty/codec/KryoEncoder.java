/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.remoting.netty.codec;

import org.leadin.common.exception.RemotingException;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * [kryo 加密的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/3/4 10:17]
 * @Update: [说明本次修改内容] BY[tophawk][2015/3/4]
 * @Version: [v1.0]
 */

public final class KryoEncoder extends MessageToByteEncoder<Object> {

    private final KryoPool kryoPool;

    public KryoEncoder(final KryoPool kryoSerializationFactory) {
        this.kryoPool = kryoSerializationFactory;
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object msg, final ByteBuf out) throws Exception {
        try {
            int startIdx = out.writerIndex();
            kryoPool.encode(out, msg);
            int endIdx = out.writerIndex();
            out.setInt(startIdx, endIdx - startIdx - 4);
        }catch (Exception ex){
            LogUtil.err(ex);
            throw new RemotingException(StringUtil.join("encode error,object:",msg.toString()));
        }
    }
}

