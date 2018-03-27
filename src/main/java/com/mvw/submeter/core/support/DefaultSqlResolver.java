package com.mvw.submeter.core.support;

import com.mvw.submeter.common.SqlInfo;
import com.mvw.submeter.core.SqlResolver;

public class DefaultSqlResolver implements SqlResolver {

	@Override
	public SqlInfo resolver(String sql) {

		String[] ts = sql.split("\\s");

		int f1 = 0,f2 = 0,f3 = 0;
		SqlInfo info = new SqlInfo();

		StringBuilder tmp = new StringBuilder();

		for (String t : ts) {
			if (f1 < 1) {// 第一个非空的是sql类型标识
				if (!"".equals(t)) {
					info.setSqlType(t);
					f1 = 1;
				}
				tmp.append(t + " ");
			} else if (f2 < 1) { // from出现了后，取第一个非空的为表名
				if ("from".equalsIgnoreCase(t)) {
					f2 = 1;
				}
				tmp.append(t + " ");
			} else if (f2 > 0 && f3 < 1) { // 取表名
				if (!"".equals(t)) {
					info.setTableName(t);
					info.setSqlPrefix(tmp.toString());
					tmp.setLength(0);
					f3 = 1;
				}
			} else {
				tmp.append(t + " ");
			}
		}

		info.setSqlSuffix(tmp.toString());

		return info;
	}

	@Override
	public String buildSql(String hash, SqlInfo sqlInfo) {
		
		StringBuilder tmp = new StringBuilder();
		tmp.append(sqlInfo.getSqlPrefix());
		tmp.append(" ");
		tmp.append(sqlInfo.getTableName());
		if(hash!=null){
			tmp.append("_");
			tmp.append(hash);
		}
		tmp.append(" ");
		tmp.append(sqlInfo.getSqlSuffix());
		
		return tmp.toString();
	}
}
