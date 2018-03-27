package com.mvw.mybatis.model;

public class User {

	private Integer id;

	private Integer pid;

	private String name;
	
	public User() {
		
	}
	
	public User(Integer id) {
		super();
		this.id = id;
	}
	
	public User(Integer id, Integer pid) {
		super();
		this.id = id;
		this.pid = pid;
	}

	public User(Integer id, Integer pid, String name) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", pid=" + pid + ", name=" + name + "]";
	}
}
