package com.mvw.submeter;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.mvw.submeter.common.ReflectUtil;

/**
 * FIXME:
 * 多个Interceptor处理器时，后续获取的是有些对象是
 * 获取的是反射对象，会有如下异常，如:
 *   ClassCastException: com.sun.proxy.$Proxy24 cannot be cast to 
 *   org.apache.ibatis.executor.statement.RoutingStatementHandler
 * 对于这个问题我建议，在一个拦截器中抽取
 * 并且其插件，也不是很方便业务拦截，因为调用那个方法，什么参数等等都不知道
 * 自己抽取至少可以做到如下:
 * 1.sql
 * 2.参数
 * 3.执行的那个DAO以及那个方法
 * 
 * 1.获取sql，设置sql(反射) 
 * 2.获取方法(进行cache) ->初始化的时候就搞定
 * 3.得注入spring其它bean，业务逻辑的抽取啊->配置在SqlSessionFactoryBean就可以由spring处理
 * 
 * #一定得在之前
 * 在Mybatis中Statement语句是通过RoutingStatementHandler对象的 prepare方法生成的
 */
@Intercepts({
		@Signature(type = StatementHandler.class, 
				method = "prepare", 
				args = {Connection.class, Integer.class }) })
public class SubmeterInterceptor implements Interceptor {

	private SubmeterManage submeterManage;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		/**
		 * 技术点尝试: 1.获取指定的是那个方法 2.获得sql和参数 3.重新设置sql
		 */
		if (invocation.getTarget() instanceof RoutingStatementHandler) {

			RoutingStatementHandler smh = (RoutingStatementHandler) invocation.getTarget();

			// 反射
			PreparedStatementHandler ps = (PreparedStatementHandler) ReflectUtil.getFieldValue(smh, "delegate");
			MappedStatement ms = (MappedStatement) ReflectUtil.getFieldValue(ps, "mappedStatement");
			
			String mId = ms.getId();
			if(submeterManage.isShardBy(mId)){
				
				BoundSql boundSql = smh.getBoundSql();
				
				//构建分表sql
				String submeterSql=submeterManage.buildSubmeterSql(mId,boundSql.getSql(),boundSql.getParameterObject());
				
				System.out.println(submeterSql);
				
				// 绑定
				ReflectUtil.setFieldValue(boundSql, "sql",submeterSql);
			}
		}
		
		return invocation.proceed();
	}

	@Override // 设置拦截类型
	public Object plugin(Object target) {
		if (target instanceof RoutingStatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		// 初始化
	}
	
	public void setSubmeterManage(SubmeterManage submeterManage) {
		this.submeterManage = submeterManage;
	}
}
