/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.client.bootstrap;

import org.leadin.collect.factory.CollectorFactory;
import org.leadin.collect.impl.DefaultDataCollector;
import org.leadin.collect.thread.CollectThread;
import org.leadin.common.InstanceManager;
import org.leadin.common.cache.ICache;
import org.leadin.common.cache.service.CacheService;
import org.leadin.common.config.ConfigService;
import org.leadin.common.constant.Constants;
import org.leadin.common.context.RuntimeContext;
import org.leadin.common.event.manager.AccidentHandleManager;
import org.leadin.common.event.manager.EventManager;
import org.leadin.common.queue.QueueManager;
import org.leadin.common.schedule.ScheduledExcutor;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.RtUtil;
import org.leadin.common.util.StatUtil;
import org.leadin.common.util.StringUtil;

import java.util.concurrent.Executors;

/**
 * [简短描述该类的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/14 21:49]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/14]
 * @Version: [v1.0]
 */
public class ClientBootStrap {
    private ScheduledExcutor excutor;

    public boolean init() {
        try {
            //初始化config
            ConfigService configService = new ConfigService();
            if (!configService.init()) {
                LogUtil.err("Config init fail!");
                return false;
            }
            //放入context
            RuntimeContext.set(Constants.CONTEXT_CONFIGSERVICE, configService);
            //RuntimeContext.setConfigService(configService);

            //初始化cache
            String cacheClazz = configService.getString(Constants.CLASS_CACHE_IMPL_KEY);
            ICache cache = InstanceManager.createInstance(cacheClazz);
            CacheService cacheService = new CacheService(cache);
            RuntimeContext.set(Constants.CONTEXT_CACHESERVICE, cacheService);

            //初始化QM
            QueueManager queueManager = new QueueManager();
            RuntimeContext.set(Constants.CONTEXT_QUEUEMANAGER, queueManager);

            //初始化EM
            EventManager eventManager = new EventManager(queueManager);

            //初始化Collector
            RuntimeContext.set(Constants.CONTEXT_COLLECTOR, new DefaultDataCollector());

            excutor = new ScheduledExcutor();
            excutor.register(eventManager);
            //错误事件处理
            AccidentHandleManager accidentHandleManager = new AccidentHandleManager();
            excutor.register(accidentHandleManager);

            //放入运行标志
            RuntimeContext.set(Constants.CONTEXT_ISSERVER,false);

            //客户端运行统计
            StatUtil.init();

            return true;
        } catch (Exception ex) {
            LogUtil.err(ex);
            return false;
        }
    }

    /**
     * 作为app调用的时候，启动
     */
    public void start() {
        //初始化collectors 并启动
        try {
            CollectThread thread = new CollectorFactory().create(RtUtil.getConfigService().getString("collect.start.class"));
            Executors.newSingleThreadExecutor().submit(thread);
            excutor.scheduledBusiness();
        } catch (Exception ex) {
            LogUtil.err(ex);
            throw new RuntimeException(StringUtil.join("Start failed due to:", ex.getMessage()));
        }
    }
}
