/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.collect.thread;

import org.leadin.common.collect.ICollector;

/**
 * [简短描述该类的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/15 0:55]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/15]
 * @Version: [v1.0]
 */
public class CollectThread implements Runnable{
    protected ICollector collector;

    public void setCollector(ICollector collector) {
        this.collector = collector;
    }

    @Override
    public void run() {

    }
}
