package com.mvw.submeter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.mvw.submeter.annotation.ShardBy;
import com.mvw.submeter.common.ScannerUtils;
import com.mvw.submeter.common.SqlInfo;
import com.mvw.submeter.core.HashHandler;
import com.mvw.submeter.core.SqlResolver;
import com.mvw.submeter.core.support.DefaultHashHandler;
import com.mvw.submeter.core.support.DefaultSqlResolver;

public class DefaultSubmeterManage implements SubmeterManage,InitializingBean{

   	/**
	 * FIXME: 1.启动的时候就加载，不考虑线程安全，直接用hashMap搞定
	 * 2.需要的时候在获取，用ConcurrentHashMap考虑线程安全(首次执行有耗时)
	 */
	private static Map<String, Method> methods = new HashMap<>();
	
	private HashHandler hashHandler = new DefaultHashHandler();
	
	private SqlResolver sqlResolver = new DefaultSqlResolver();
	
	/**
	 * 需要扫描的包配置
	 */
	private List<String> packages=null;
	
	public boolean isShardBy(String mId) {
		if(methods.containsKey(mId)){
			return true;
		}
		return false;
	}

	public String buildSubmeterSql(String mid,String sql, Object parameter) {
		
		//获得hash
		ShardBy shardBy = methods.get(mid).getAnnotation(ShardBy.class);
		String hash=hashHandler.hash(shardBy.value(),parameter);
		
		//解析表
		SqlInfo sqlInfo = sqlResolver.resolver(sql);
		
		//重新构建
		return sqlResolver.buildSql(hash,sqlInfo);
	}

	//setter
	public void setPackages(List<String> packages) {
		this.packages = packages;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//初始化:扫描packages
		if(packages!=null){
			List<Class<?>> list = ScannerUtils.scanner(packages, null);
			if(list!=null){
				for(Class<?> c:list){
					//含继承的所有方法
					Method[] ms = c.getMethods();
					for(Method m:ms){
						ShardBy shardBy = m.getAnnotation(ShardBy.class);
						if(shardBy!=null){
							methods.put(c.getName()+"."+m.getName(), m);
						}
					}
				}
			}
		}
	}
}
