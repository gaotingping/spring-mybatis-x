package com.mvw.rwsupport.route;

import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.mvw.mybatis.common.ReadConfUtils;
import com.mvw.rwsupport.common.DBContextHolder;
import com.mvw.rwsupport.common.DBrwFlag;

/**
 * 读写分离拦截器:技术点上是行得通的[路由]
 */
@Intercepts({ 
	@Signature(type = Executor.class, method = "update",  args = {MappedStatement.class, Object.class}),
	@Signature(type = Executor.class, method = "query",  args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
	@Signature(type = Executor.class, method = "query",  args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class RwRouteInterceptor implements Interceptor {
	
	private static Map<String,String> useMapMethods=null;
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		MappedStatement mst=(MappedStatement)invocation.getArgs()[0];
			
		String mId = mst.getId();
		
		if(useMapMethods.containsKey(mId)){ /*优先级最高*/
			
			DBContextHolder.setDbType(DBrwFlag.WRITE);
			
		}else if("query".equals(invocation.getMethod().getName())){/*读肯定比写多*/
			
			DBContextHolder.setDbType(DBrwFlag.READ);
			
		}else if("update".equals(invocation.getMethod().getName())){
			
			DBContextHolder.setDbType(DBrwFlag.WRITE);
			
		}
		
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		try {
			useMapMethods=ReadConfUtils.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
