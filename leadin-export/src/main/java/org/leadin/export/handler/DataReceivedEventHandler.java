/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.export.handler;

import org.leadin.analyze.LogAnalyzer;
import org.leadin.common.bean.BaseBean;
import org.leadin.common.event.Event;
import org.leadin.common.event.events.AckEvent;
import org.leadin.common.event.handler.BaseHandler;
import org.leadin.common.event.handler.IEventHandler;
import org.leadin.common.exception.HandlerException;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * [处理数据收集CollectFinished事件,用于远程发送数据]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/11 1:10]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/11]
 * @Version: [v1.0]
 */
public class DataReceivedEventHandler extends BaseHandler implements IEventHandler<BaseBean> {


    @Override
    public void handle(Event<BaseBean> event) {
        //接收 DataReceivedEvent,并处理
      LogUtil.debug("=================== handler received:{}",event);
      LogAnalyzer logAnalyzer = LogAnalyzer.getInstance();
      try {
          boolean flag = logAnalyzer.analyze(event);

          if(flag){
              //ClientStatusEvent特殊处理,不用发回执
//              if(event.getSrcEventType().equals(EnumEventType.CLIENT_STATUS_EVENT)){
//                  return;
//              }

              AckEvent ackEvent = new AckEvent(event.getEventNo(),event.getEventType());
              qm.push(ackEvent);
          }else {
              LogUtil.err("日志解析出错！");
          }
      }catch(Exception ex){
          ex.printStackTrace();
          //TODO push to trash=done 发出异常后会被em捕获
          throw new HandlerException(StringUtil.join("===failed to analyze data ", event.toString()));
      }
    }
}
