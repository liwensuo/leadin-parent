package org.leadin.common.bean;

import lombok.Data;
import org.leadin.common.util.StringUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * [原生日志bean]
 *
 * @ProjectName: [leadin]
 * @Author: [Jon.K]
 * @CreateDate: [2015/3/2 18:45]
 * @Update: [说明本次修改内容] BY[Jon][2015/3/2 18:45]
 * @Version: [v1.0]
 */
@Data
public class BaseBean implements Serializable{
    /**
     * ID
     */
    public String id;
    /**
     * 服务器信息
     */
    public String serverIp;
    /**
     * tomcat 信息
     */
    public String tomcatName;
    /**
     * 日志内容
     */
    public String content;

    /**
     * 日志打印日期：yyyy-MM-ddhh:mm:ss
     */
    private Date time;

    @Override
    public String toString() {
        //return StringUtil.join("ID:",id," serverIp:" ,serverIp, " tomcatName:", tomcatName, " ;content:", content.substring(0,content.length()>20?20:content.length()));
        return StringUtil.join("ID:",id," serverIp:" ,serverIp, " tomcatName:", tomcatName, " ;content:", content);
    }
}
