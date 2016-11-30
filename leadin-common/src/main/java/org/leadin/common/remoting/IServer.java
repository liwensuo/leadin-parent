/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.remoting;

/**
 * [简短描述该类的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/3/3 13:50]
 * @Update: [说明本次修改内容] BY[tophawk][2015/3/3]
 * @Version: [v1.0]
 */
public interface IServer {
    void start(int port);

    void close();
}
