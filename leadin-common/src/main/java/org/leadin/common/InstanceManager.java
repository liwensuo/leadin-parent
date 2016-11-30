/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.common;

import org.leadin.common.util.LogUtil;

/**
 * [创建对象使用]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/14 21:43]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/14]
 * @Version: [v1.0]
 */
public class InstanceManager {
    public static <T> T createInstance(String implClassName) {
        // 通过反射创建该实现类对应的实例
        T instance;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> commandClass = Class.forName(implClassName,true,loader);
            instance = (T) commandClass.newInstance();
        } catch (Exception e) {
            LogUtil.err("Error in create instance:", e);
            throw new RuntimeException(e);
        }
        return instance;
    }
}
