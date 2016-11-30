/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.exception;

/**
 * [远程异常]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/10 22:06]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/10]
 * @Version: [v1.0]
 */
public class RemotingException extends BaseRuntimeException {
    public RemotingException(String msg){
        super(msg);
        //TO DO: 可以选择发往服务器报警
    }
}
