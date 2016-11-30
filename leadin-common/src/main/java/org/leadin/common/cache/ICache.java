/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.common.cache;

import org.leadin.common.bean.BaseBean;
import org.leadin.common.constant.EnumEventType;
import org.leadin.common.event.Event;
import org.leadin.common.exception.CacheException;

import java.util.List;

/**
 * [缓存接口]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/10 21:50]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/10]
 * @Version: [v1.0]
 */
public interface ICache<T> {
    /**
     * 存入数据
     *
     * @param object
     * @return
     * @throws CacheException
     */
    String add(Event<T> object) throws CacheException;

    /**
     * 根据事件编号和事件类型进行缓存数据的删除操作
     *
     * @param object
     *
     */
    void remove(Event<T> object);

    /**
     * 获取缓存数据
     *
     * @param cacheNo
     * @param eventType
     * @param baseBean
     * @return
     * @throws CacheException
     */
    Event<T> get(String cacheNo , EnumEventType eventType , BaseBean baseBean);

    /**
     * 获取所有缓存数据
     *
     * @return
     */
    List<Event<T>> getAll();

    /**
     * 缓存数据的类型进行转换
     *
     * @param eventNo
     * @param targetEventType
     * @param newEventType
     * @return
     */
    boolean rename(String eventNo, EnumEventType targetEventType, EnumEventType newEventType , String prefix);
}