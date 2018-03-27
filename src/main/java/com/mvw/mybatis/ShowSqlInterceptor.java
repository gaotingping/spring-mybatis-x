package com.mvw.mybatis;

import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import com.mvw.submeter.common.ReflectUtil;

/**
 * sql日志显示工具
 * 
 * 插件开发：
 * 1)实现下列接口
 *   <pre>
 *   	org.apache.ibatis.plugin.Interceptor
 *   </pre>
 *   
 * 2)xml配置
 *   <pre>
 *   	<plugins>
	     <plugin interceptor="springibatis.interceptor.ShowSqlInterceptor">
	        <!--
	        <property name="name" value="value"/>
	        -->
	     </plugin>
	   </plugins>
 *   </pre>
 *   
 *   #流程梳理
 *   <pre>
 *   	需要实现MyBatis的Interceptor接口，并主要要实现interceptor和plugin方法，而setProperties()
 *      当你配置了property就需要实现，没有，那么可以不用实现。实现接口后，那么就要将插件配置到MyBatis中去，
 *      然后通过XMLConfigBuilder来实例化插件对象，并将他们放到Configuration对象的InterceptChain
 *      对象的List集合中，然后在Configuration各种new的方法中调用InterceptChain的pluginAll方法，
 *      这里面将调用各个插件的plugin方法，这个方法里面则就调用Plugin的wrap方法，这个方法将要传入target和
 *      this(也就是插件自身对象)。那么在Plugin对象里面将创建一个代理对象，并且为这个代理对象创建一个InvocationHandler对象，
 *      这里将拦截代理对象的所有方法执行过程，及触发invoke方法，这里将执行实现的插件行为。这就是MyBatis的插件实现以及执行的过程。
 *   </pre>
 *   
 *   #缺点
 *   拦截器中无法获取到原始方法的信息，因而拦截行为无法根据具体情况来具体书写，用处不大
 *   
 *   #下面演示例子目的，方便调试:直接输出sql和参数(比打开debug,大量数据找要方便的多)
 *   1.输出方法名称
 *   2.输出sql
 *   3.输出参数
 *   4.输出耗时
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "query",  args = { Statement.class, ResultHandler.class }),
    		  @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }) })
public class ShowSqlInterceptor implements Interceptor {

	@Override//业务逻辑
	public Object intercept(Invocation invocation) throws Throwable {
		
		Object result = null;
		
		if (invocation.getTarget() instanceof StatementHandler) {
			
			StringBuilder sqlStr=new StringBuilder();
			sqlStr.append("\r\n");
			sqlStr.append("\r\n===========================================================");
			
			//FIXME:多个拦截器这里获取的是代理对象，怎么弄
			RoutingStatementHandler smh=(RoutingStatementHandler)invocation.getTarget();
			
			//反射
			PreparedStatementHandler p=(PreparedStatementHandler)ReflectUtil.getFieldValue(smh,"delegate");
			MappedStatement m=(MappedStatement)ReflectUtil.getFieldValue(p,"mappedStatement");
			sqlStr.append("\r\nmethod\t:"+m.getId());
			sqlStr.append("\r\nsql\t:" + smh.getBoundSql().getSql());
			sqlStr.append("\r\nparams\t:" + smh.getBoundSql().getParameterObject());
			long s = System.currentTimeMillis();
			
			
			ReflectUtil.setFieldValue(smh.getBoundSql(), "sql","select * from user");
			
			/**
			 * bug fix
			 * 执行中报错时，也输出参数和sql,方便错误查找
			 */
			try{
				result = invocation.proceed();
			}catch(Exception e){
				
				sqlStr.append("\r\ntime\t:-1");
				sqlStr.append("\r\n===========================================================");
				System.out.println(sqlStr.toString());
				
				throw new RuntimeException(e.getMessage(),e);
			}
			
			sqlStr.append("\r\ntime\t:" + (System.currentTimeMillis() - s));
			sqlStr.append("\r\n===========================================================");
			System.out.println(sqlStr.toString());
			
		} else {
			result = invocation.proceed();
		}
		
		return result;
	}

	@Override//将你插件放入到MyBatis的插件集合中去
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * 设置插件属性:在这里接收xml中配置的插件属性
	 * 非spring管理,衔接起来是有点别扭
	 */
	@Override
	public void setProperties(Properties properties) {

	}
}
