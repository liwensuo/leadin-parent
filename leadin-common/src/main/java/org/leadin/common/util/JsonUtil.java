/**
 * Copyright (C) 2015-2016 Brother Group Limited
 */
package org.leadin.common.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [JSON 操作工具类]
 *
 * @ProjectName: [dubbo-server-client]
 * @Author: [liuxiaolong]
 * @CreateDate: [2015/2/9 11:21]
 * @Update: [说明本次修改内容] BY[liuxiaolong][2015/2/9]
 * @Version: [v1.0]
 */
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 Java 对象转为 JSON 字符串
     */
    public static <T> String toJSON(T obj) {
        String jsonStr;
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("Java 转 JSON 出错! {} ", e.getMessage());
            throw new RuntimeException(e);
        }
        return jsonStr;
    }

    /**
     * 将 JSON 字符串转为 Java 对象
     */
    public static <T> T fromJSON(String json, Class<T> type) {
        T obj;
        try {
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            obj = objectMapper.readValue(json, type);
        } catch (Exception e) {
            logger.error("JSON 转 Java 出错！{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return obj;
    }
}
