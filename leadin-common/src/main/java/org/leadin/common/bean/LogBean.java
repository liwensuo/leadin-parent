package org.leadin.common.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * [解析后日志bean]
 *
 * @ProjectName: [leadin]
 * @Author: [Jon.K]
 * @CreateDate: [2015/2/25 11:47]
 * @Update: [说明本次修改内容] BY[Jon.K][2015/2/25 11:47]
 * @Version: [v1.0]
 */
@Data
@Document(collection = "act_log")
public class LogBean implements Serializable {
    @Id
    private String id;
    /**
     * 动作代码
     */
    private String actionCode;
    /**
     * 日志级别
     */
    private String level;
    /**
     * 主体
     */
    private String main;
    /**
     * 客体
     */
    private String subject;
    /**
     * 动作类型
     */
    private String actionType;
    /**
     * 日志打印日期：yyyy-MM-ddhh:mm:ss
     */
    private String time;

    @Override
    public String toString() {
        return "id:" + id + "; actionCode:" + actionCode + "; level:" + level + "; main:" + main + "; subject:" + subject + "; actionType:" + actionType + "; time:" + time;
    }
}
