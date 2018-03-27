package com.mvw.mybatis.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mvw.mybatis.model.User;
import com.mvw.submeter.annotation.ShardBy;

@Repository //自动扫描不需要加，但为了后续好控制建议加
public interface RwDao{
	
	public User get(String p);
	
	public User get2();
	
	public int update(String name);
	
	@ShardBy("1") //单个常量
	public User test1(@Param("p")String name);
	
	@ShardBy("1.id") //单个POJO 支持多级散列
	public User test2(User u);
	
	@ShardBy("u.pid") //多个参数
	public User test3(@Param("u")User u,@Param("pid")String pid);
}
