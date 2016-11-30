/*
 * shijie99.com copyright@2016
 */
package org.leadin.common.repository;

import org.leadin.common.bean.LogBean;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 用途
 *
 * @author tophawk
 * @date 2016/11/28
 */
public interface LogRepository extends MongoRepository<LogBean, String> {
}
