package org.leadin.common.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @ProjectName: [leadin]
 * @Author: [Jon.K]
 * @CreateDate: [2015/3/19 15:17]
 * @Update: [说明本次修改内容] BY[Jon][2015/3/19 15:17]
 * @Update: [说明本次修改内容] BY[tophawk][2016/11/20]
 * @Version: [v1.0]
 */
@Data
@Document(collection = "client_run_info")
public class ClientInfoBean implements Serializable {
    @Id
    private String id;
    private String clientId;
    private String clientStartTime;
    private String receiveTime;
    private String receiveDate;
    private String exception;
    private String status;
    private String msg;

    @Override
    public String toString() {
        return "ClientInfoBean{" +
                "id='" + id + '\'' +
                ", client_id='" + clientId + '\'' +
                ", client_start_time='" + clientStartTime + '\'' +
                ", receive_time='" + receiveTime + '\'' +
                ", receive_date='" + receiveDate + '\'' +
                ", exception='" + exception + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
