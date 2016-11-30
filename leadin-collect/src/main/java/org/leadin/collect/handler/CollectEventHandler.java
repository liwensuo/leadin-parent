/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.collect.handler;

import org.leadin.common.constant.Constants;
import org.leadin.common.constant.EnumEventType;
import org.leadin.common.event.Event;
import org.leadin.common.event.events.AckEvent;
import org.leadin.common.event.handler.BaseHandler;
import org.leadin.common.event.handler.IEventHandler;
import org.leadin.common.exception.HandlerException;
import org.leadin.common.exception.QueueManagerException;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.RtUtil;
import org.leadin.common.util.StringUtil;

/**
 * [处理数据收集DataCollected事件]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/11 1:10]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/11]
 * @Version: [v1.0]
 */
public class CollectEventHandler extends BaseHandler implements IEventHandler {

    @Override
    public void handle(Event event) {
        //接收 DataCollectedEvent,并处理
        EnumEventType sourceEventType = event.getEventType();

        //发出 CollectFinishedEvent
        event.setEventType(EnumEventType.COLLECT_FINISHED_EVENT);

        boolean sent = sendEvent(event,sourceEventType);

        //失败重试
        if(!sent) {

            int retry = RtUtil.getConfigService().getInt("collect.retry");
            if (retry == 0) retry = Constants.SYSTEM_FAIL_RETRY;

            int count = retry;
            while(count-->0 ||(sent=sendEvent(event,sourceEventType))){
                try {
                    Thread.sleep(Constants.SYSTEM_FAIL_RETRY_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(count <=0 && (!sent)){
                //放入处理失败队列
                LogUtil.err("all of {} retries of the data {} is fail,push to trash!", retry, event);
                //TODO push to trash=done 发出异常后会被em捕获
                throw new HandlerException(StringUtil.join("finally failed to collect data ", event.toString()));
            }
        }
    }

    private boolean sendEvent(Event event,EnumEventType sourceEventType){
        try {
            qm.push(event);

            //发出ack
            AckEvent ack = new AckEvent(event.getEventNo(), sourceEventType);
            qm.push(ack);

            return true;
        } catch (QueueManagerException ex) {
            LogUtil.err(ex);
            return false;
        }
    }
}
