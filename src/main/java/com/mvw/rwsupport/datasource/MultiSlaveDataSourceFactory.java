package com.mvw.rwsupport.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.mvw.rwsupport.common.DBContextHolder;
import com.mvw.rwsupport.common.DBrwFlag;
import com.mvw.rwsupport.common.DataSourceHolder;
import com.mvw.rwsupport.loadbalance.LoadBalance;
import com.mvw.rwsupport.loadbalance.support.WeightRandomLoadBalance;

/**
 * 多主支持，多个数据源选取策略，由loadBalance实现
 * 目前只支持随机加权和轮询加权(加权是可选策略，所以相当于有4种策略)
 * 配置示例:
 * <pre>
 * <bean id="dataSourceFactory" class="com.mvw.rwsupport.datasource.MultiSlaveDataSourceFactory">
		<property name="loadBalancing" ref="loadBalancing"/><!-- 可选，默认为随机 -->
		<property name="master" ref="m"/> <!-- 主库 -->
		<property name="slaves"> <!-- 从库列表 -->
		  	<list>
		  	    <!--第一个读数据源-->
				<bean class="com.mvw.rwsupport.common.DataSourceHolder">
					<property name="id" value="dbgtp2" /> <!-- 数据源的唯一标识，建议配置为“见名知意”的值，如取IP最后位 -->
					<property name="weight" value="1" /> <!-- 配置建议1-10的整数，可选，默认为1 -->
					<property name="dataSource" ref="r1" />
				</bean>
				<!--第二个读数据源-->
				<bean class="com.mvw.rwsupport.common.DataSourceHolder">
					<property name="id" value="dbgtp3" />
					<property name="weight" value="5" /> <!-- 配置建议1-10的整数，可选，默认为1 -->
					<property name="dataSource" ref="r2" />
				</bean>
			</list>
		</property>
	</bean>
 * </pre>
 * @author gaotingping
 */
public class MultiSlaveDataSourceFactory extends AbstractRoutingDataSource {

	private Map<String, DataSource> resolvedSlaves = null;
	
	private List<DataSourceHolder> slaves=null;

	private DataSource master = null;

	private LoadBalance loadBalance;

	/**
	 * 重写数据源的获取
	 */
	protected DataSource determineTargetDataSource() {

		String rwFlag = DBContextHolder.getDbType();

		if (DBrwFlag.READ.name().equals(rwFlag)) {

			String dbKey = loadBalance.lookupKey();

			return resolvedSlaves.get(dbKey);

		} else if (DBrwFlag.WRITE.name().equals(rwFlag)) {

			return master;

		} else {
			throw new IllegalStateException("DBrwFlag is error:" + rwFlag);
		}
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return null;
	}

	@Override
	public void afterPropertiesSet() {
		// super.afterPropertiesSet();
		
		if(this.loadBalance==null){
			this.loadBalance=new WeightRandomLoadBalance();
		}
		
		//resolved datasource
		if (this.slaves == null || this.slaves.isEmpty()) {
			throw new IllegalArgumentException("Property 'slaves' is required");
		}
		
		resolvedSlaves=new HashMap<String, DataSource>();
		for(DataSourceHolder dsh:slaves){
			if(dsh.getId() == null || "".equals(dsh.getId())){
				throw new IllegalArgumentException("DataSourceHolder Property 'id' is required");
			}else if(dsh.getWeight()<0 || dsh.getWeight() >10){
				throw new IllegalArgumentException("DataSourceHolder Property 'Weight' must in [1,10]");
			}
			resolvedSlaves.put(dsh.getId(), dsh.getDataSource());
		}
		
		//init loadBalance
		loadBalance.init(this.slaves);
	}

	public void setSlaves(List<DataSourceHolder> slaves) {
		this.slaves = slaves;
	}

	public void setMaster(DataSource master) {
		this.master = master;
	}

	public void setLoadBalancing(LoadBalance loadBalance) {
		this.loadBalance = loadBalance;
	}
}
