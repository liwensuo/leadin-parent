/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.collect.impl;

import org.leadin.common.bean.BaseBean;
import org.leadin.common.cache.service.CacheService;
import org.leadin.common.collect.ICollector;
import org.leadin.common.constant.Constants;
import org.leadin.common.context.RuntimeContext;
import org.leadin.common.event.Event;
import org.leadin.common.event.events.DataCollectedEvent;
import org.leadin.common.exception.CollectException;
import org.leadin.common.queue.QueueManager;
import org.leadin.common.util.StringUtil;

/**
 * [简短描述该类的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/14 21:20]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/14]
 * @Version: [v1.0]
 */
public class DefaultDataCollector implements ICollector<BaseBean> {
    private CacheService cacheService;

    public DefaultDataCollector() {
    }

    public DefaultDataCollector(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 加入缓存
     *
     * @param baseBean
     * @throws CollectException
     */
    @Override
    public void collect(BaseBean baseBean) throws CollectException {
        if (!StringUtil.isBlank(baseBean.getContent())) {
            //cacheService.add(EnumCacheType.CACHE_COLLECT,data);
            QueueManager qm = RuntimeContext.get(Constants.CONTEXT_QUEUEMANAGER);
            Event e = new DataCollectedEvent();

            e.setEventObject(baseBean);
            qm.push(e);
        }
    }
}
