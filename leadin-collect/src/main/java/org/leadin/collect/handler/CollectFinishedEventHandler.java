/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.collect.handler;

import org.leadin.common.constant.Constants;
import org.leadin.common.constant.EnumEventType;
import org.leadin.common.event.Event;
import org.leadin.common.event.handler.BaseHandler;
import org.leadin.common.event.handler.IEventHandler;
import org.leadin.common.exception.HandlerException;
import org.leadin.common.exception.RemotingException;
import org.leadin.common.remoting.IClient;
import org.leadin.common.remoting.factory.RemotingFactory;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.RtUtil;
import org.leadin.common.util.StringUtil;

/**
 * [处理数据收集CollectFinished事件,用于远程发送数据]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/11 1:10]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/11]
 * @Version: [v1.0]
 */
public class CollectFinishedEventHandler extends BaseHandler implements IEventHandler<String> {
    private IClient client;


    public void setClient(IClient client) {
        this.client = client;
    }
    public CollectFinishedEventHandler(){
        super();
        initClient();
    }

    public void initClient(){
        //TODO need 断线重连，发送失败重试机制 =>OK
        client = RemotingFactory.getDefaultFactory().createClient();
        setClient(client);
    }

    @Override
    public void handle(Event event) {
        //接收 CollectFinishedEvent,并处理
        EnumEventType sourceEventType = event.getEventType();
        LogUtil.debug("received:{}", sourceEventType);
        //TODO 远程发送,在这里扩展容错机制=>done

        boolean sent = sendEvent(event);

        //失败重试
        if(!sent) {

            int retry = RtUtil.getConfigService().getInt("remote.retry");
            if (retry == 0) retry = Constants.SYSTEM_FAIL_RETRY;

            int count = retry;
            while(count-->0 ||(sent=sendEvent(event))){
                try {
                    Thread.sleep(Constants.SYSTEM_FAIL_RETRY_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(count <=0 && (!sent)){
                //放入处理失败队列
                LogUtil.err("all of {} retries of the data {} is fail,push to trash!",retry,event);
                //TODO push to trash=done 发出异常后会被em捕获
                throw new HandlerException(StringUtil.join("===finally failed to send data ",event.toString()));
            }
        }
    }

    /**
     * 远程发送消息
     * @return
     */
    private boolean sendEvent(Event event){
        try {
            client.sent(event);
            return true;
        }catch (RemotingException ex){
            LogUtil.err(ex);
            return false;
        }
    }
}
