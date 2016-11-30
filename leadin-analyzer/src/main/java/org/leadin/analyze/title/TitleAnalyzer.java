/**  * Copyright (C) 2015-2016 Brother Group Limited  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
package org.leadin.analyze.title;

import org.leadin.analyze.file.FileAnalyzer;
import org.leadin.common.util.LogUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * [并发处理]
 *
 * @ProjectName: [leadin]
 * @Author: [liushuo]
 * @CreateDate: [2015/3/3 11:10]
 * @Update:
 * @Version: [v1.0]
 */
public class TitleAnalyzer{

    public boolean analyze(List<String> logList,String tableName){
        LogUtil.debug("fock join analyze start");
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Future<Boolean> result = forkJoinPool.submit(new FileAnalyzer(logList,0,tableName));
        forkJoinPool.shutdown();
        while(true){
            if(forkJoinPool.isTerminated()){
                break;
            }
        }
        boolean flag = false;
        try{
            flag = result.get();
            LogUtil.debug("fock join analyze end");
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.err(e);
        }
        return flag;
    }

}