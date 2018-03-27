package com.mvw.rwsupport.loadbalance.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvw.rwsupport.common.DataSourceHolder;
import com.mvw.rwsupport.loadbalance.LoadBalance;

/**
 * 轮询/加权轮询
 * 
 * @author gaotingping
 */
public class WeightPollLoadBalance implements LoadBalance{

	private List<String> slaves = null;
	
	private long index=0;
	
	private final static Logger logger=LoggerFactory.getLogger(WeightPollLoadBalance.class);
	
	@Override
	public String lookupKey() {
		
		if(slaves.isEmpty()){
			return null;
		}
		
		int k = (int)(++index)%slaves.size();
		
		return slaves.get(k);
	}

	@Override
	public void init(List<DataSourceHolder> list) {
		
		this.slaves=new ArrayList<>();
		
		for(DataSourceHolder dsh:list){
			int w = dsh.getWeight();
			for(int i=0;i<w;i++){
				this.slaves.add(dsh.getId());
			}
		}
		
		//随机打散
		Collections.shuffle(this.slaves);
		
		logger.info("The loadBalance inited");
	}
}
