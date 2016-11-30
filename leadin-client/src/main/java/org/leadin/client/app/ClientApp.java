/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.client.app;

import org.leadin.client.bootstrap.ClientBootStrap;
import org.leadin.common.util.LogUtil;

/**
 * [简短描述该类的功能]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/14 21:42]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/14]
 * @Version: [v1.0]
 */
public class ClientApp {
    /**
     * 客户端入口
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            ClientBootStrap bootStrap = new ClientBootStrap();
            //系统初始化并启动
            if (!bootStrap.init()) {
                LogUtil.err("Client init failure,system halted!");
                System.exit(-1);
            }
            bootStrap.start();
            LogUtil.info("Client started!");
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.err("Client init failure,system halted!");
        }
    }
}
