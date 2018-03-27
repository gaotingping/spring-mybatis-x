package com.mvw.rwsupport.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.mvw.rwsupport.common.DBContextHolder;

/**
 * 简单数据源实现
 * 
 * @author gaotingping
 */
public class SimpleDataSourceFactory extends AbstractRoutingDataSource {

	protected Object determineCurrentLookupKey() {
		return DBContextHolder.getDbType();
	}
}
