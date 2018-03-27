package com.mvw.rwsupport.loadbalance.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvw.rwsupport.common.DataSourceHolder;
import com.mvw.rwsupport.loadbalance.LoadBalance;

/**
 * 加权随机/随机(weight=1,相当于只随机)
 * 
 * @author gaotingping
 */
public class WeightRandomLoadBalance implements LoadBalance {

	private List<String> slaves = null;
	
	private Random random = new Random();
	
	private final static Logger logger=LoggerFactory.getLogger(WeightRandomLoadBalance.class);

	@Override
	public String lookupKey() {
		
		if(slaves.isEmpty()){
			return null;
		}
		
		int index = random.nextInt(slaves.size()); // 0.. size
		
		return slaves.get(index);
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
