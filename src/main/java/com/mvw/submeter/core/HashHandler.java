package com.mvw.submeter.core;

/**
 * 散列处理器:负责根据pattern算出一个hash值
 */
public interface HashHandler {
	
	/**
	 * 按照模式获取散列(一般建议返回个数值，但是为了支持其它方式，预留为字符串)
	 * 
	 * @param pattern
	 * @return
	 */
	public String hash(String pattern,Object parameter);
}
