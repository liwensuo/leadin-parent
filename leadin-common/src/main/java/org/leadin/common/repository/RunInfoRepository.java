/*
 * shijie99.com copyright@2016
 */
package org.leadin.common.repository;

import org.leadin.common.bean.ClientInfoBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * 用途
 *
 * @author tophawk
 * @date 2016/11/28
 */

public interface RunInfoRepository extends MongoRepository<ClientInfoBean, String> {
}
