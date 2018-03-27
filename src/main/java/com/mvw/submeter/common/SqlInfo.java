package com.mvw.submeter.common;

public class SqlInfo {
	
	//表名
	private String tableName; 
	
	//读写
	private String sqlType;
	
	//除表名外的前后缀
	private String sqlPrefix;
	
	private String sqlSuffix;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getSqlPrefix() {
		return sqlPrefix;
	}

	public void setSqlPrefix(String sqlPrefix) {
		this.sqlPrefix = sqlPrefix;
	}

	public String getSqlSuffix() {
		return sqlSuffix;
	}

	public void setSqlSuffix(String sqlSuffix) {
		this.sqlSuffix = sqlSuffix;
	}

	@Override
	public String toString() {
		return "SqlInfo [tableName=" + tableName + ", sqlType=" + sqlType + ", sqlPrefix=" + sqlPrefix + ", sqlSuffix="
				+ sqlSuffix + "]";
	}
}
