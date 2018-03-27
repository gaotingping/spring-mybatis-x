package com.mvw.rwsupport.loadbalance;

import java.util.List;

import com.mvw.rwsupport.common.DataSourceHolder;

public interface LoadBalance {

	/**
	 * 获得一个数据源标识
	 */
	String lookupKey();

	/**
	 * 初始化:设置数据源
	 */
	void init(List<DataSourceHolder> slaves);
	
}

