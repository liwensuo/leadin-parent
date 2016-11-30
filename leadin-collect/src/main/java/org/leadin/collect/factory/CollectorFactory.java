/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.collect.factory;


import org.leadin.collect.thread.CollectThread;
import org.leadin.common.InstanceManager;
import org.leadin.common.collect.ICollector;
import org.leadin.common.constant.Constants;
import org.leadin.common.context.RuntimeContext;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.StringUtil;

/**
 * [负责创建collectors]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/14 13:15]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/14]
 * @Version: [v1.0]
 */
public class CollectorFactory {
    /**
     * 创建主动收集 型的collector
     *
     * @param clazz
     * @return
     */
    public CollectThread create(String clazz) {
        if (StringUtil.isBlank(clazz)) {
            throw new IllegalArgumentException(StringUtil.join("Invalid class name: ", clazz));
        }
        try {
            ICollector collector = RuntimeContext.get(Constants.CONTEXT_COLLECTOR);

            CollectThread collectThread = InstanceManager.createInstance(clazz);
            collectThread.setCollector(collector);

            return collectThread;
        } catch (Exception e) {
            LogUtil.sendErr(e);
            throw new IllegalStateException(StringUtil.join("class ", clazz, " not found!"));
        }
    }
}
