package com.mvw.rwsupport.common;

import javax.sql.DataSource;

/**
 * 数据源持有者
 * 
 * @author gaotingping
 */
public class DataSourceHolder {

	private DataSource dataSource;

    /**
     * 每个数据源，起一个很名称或标识
     * 建议做到“见名知意”,如可以取ip最后一位
     * db122,这样方便日后问题定位
     */
	private String id;
	
    /**
     * 在加权负载模式下
     * 每个数据源，可以指定权重，默认是1
     * min=1,max=10,差距太大就没有说明意思了
     */
    private int weight=1;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
