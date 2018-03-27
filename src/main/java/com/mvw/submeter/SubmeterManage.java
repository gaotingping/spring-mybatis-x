package com.mvw.submeter;

public interface SubmeterManage {

	/**
	 * 判断当前方法是否分片
	 * 
	 * @param mId
	 * @return
	 */
	public boolean isShardBy(String mId);

	/**
	 * 构建分片的sql
	 * 
	 * @param mid
	 * @param sql
	 * @param parameter
	 * @return
	 */
	public String buildSubmeterSql(String mid,String sql, Object parameter);

}
