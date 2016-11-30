/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.remoting.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.leadin.common.remoting.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * [Kryo 序列化]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/3/3 17:08]
 * @Update: [说明本次修改内容] BY[tophawk][2015/3/3]
 * @Version: [v1.0]
 */

public final class KryoSerialization implements Serialization {

    private final KryoFactory kryoFactory;

    public KryoSerialization(final KryoFactory kryoFactory) {
        this.kryoFactory = kryoFactory;
    }

    @Override
    public void serialize(final OutputStream out, final Object message) throws IOException {
        Kryo kryo = kryoFactory.getKryo();
        Output output = new Output(out);
        kryo.writeClassAndObject(output, message);
        output.close();
        kryoFactory.returnKryo(kryo);
    }

    @Override
    public Object deserialize(final InputStream in) throws IOException {
        Kryo kryo = kryoFactory.getKryo();
        Input input = new Input(in);
        Object result = kryo.readClassAndObject(input);
        input.close();
        kryoFactory.returnKryo(kryo);
        return result;
    }
}