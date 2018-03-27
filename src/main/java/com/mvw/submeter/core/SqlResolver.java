package com.mvw.submeter.core;

import com.mvw.submeter.common.SqlInfo;

/**
 * sql解析器，负责解析出表名等信息
 */
public interface SqlResolver {

	/**
	 * 解析sql
	 * 
	 * @param sql
	 * @return
	 */
	public SqlInfo resolver(String sql);

	/**
	 * 重组sql
	 * 
	 * @param hash
	 * @param sqlInfo
	 * @return
	 */
	public String buildSql(String hash, SqlInfo sqlInfo);
}
