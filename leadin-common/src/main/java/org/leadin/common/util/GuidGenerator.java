/*
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.util;

import java.util.UUID;

/**
 * [生成系统唯一数]
 *
 * @ProjectName: [leadin]
 * @Author: [tophawk]
 * @CreateDate: [2015/2/10 22:30]
 * @Update: [说明本次修改内容] BY[tophawk][2015/2/10]
 * @Version: [v1.0]
 */
public class GuidGenerator {
    public static synchronized String getGUID() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
