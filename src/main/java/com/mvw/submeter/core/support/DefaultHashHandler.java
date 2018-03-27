package com.mvw.submeter.core.support;

import java.util.Map;

import com.mvw.submeter.common.ReflectUtil;
import com.mvw.submeter.core.HashHandler;

public class DefaultHashHandler implements HashHandler {

	private final static int hashSize = 2;

	@SuppressWarnings("unchecked")
	@Override
	public String hash(String pattern, Object parameter) {
		if (parameter instanceof Map) {
			return handlerMap(pattern, (Map<String, Object>) parameter);
		} else {
			return handlerObj(pattern, parameter);
		}
	}

	// 多个参数HashMap(或有注解的单个参数)
	private String handlerMap(String pattern, Map<String, Object> p) {

		// 空:取第一个参数全部
		if ("".equals(pattern)) {
			Object obj = p.get("param1");
			return innerHash(obj);
		}

		int index = pattern.indexOf(".");

		// 非空,没有点:按序号或名称取参数全部
		if (index < 0) {

			Object obj = null;
			if (pattern.matches("\\d+")) {
				obj = p.get("param" + pattern);
			} else {
				obj = p.get(pattern);
			}
			return innerHash(obj);

		}

		// 非空,取属性：按序号或名称取参数的属性
		String pIndex = pattern.substring(0,index);
		String pName = pattern.substring(index + 1);

		Object pv = null;
		if (pIndex.matches("\\d+")) {
			pv = p.get("param" + pIndex);
		} else {
			pv = p.get(pIndex);
		}

		return innerHash(ReflectUtil.getFieldValue(pv, pName));
	}

	// 处理obj
	private String handlerObj(String pattern, Object p) {

		// 基本类型直接取参数全部
		if (ReflectUtil.isBaseType(p.getClass())) {
			return innerHash(p);
		}

		// pojo
		return handlerPojo(pattern, p);
	}

	// 算出hash:目前只支持数值
	private String innerHash(Object p) {
		long v = Long.parseLong(p.toString());
		return (v % hashSize) + "";
	}

	// 单个pojo
	private String handlerPojo(String pattern, Object p) {

		// 空:取第一个参数全部
		if ("".equals(pattern)) {
			return innerHash(p);
		}

		int index = pattern.indexOf(".");

		// 非空,没有点:按序号或名称取参数全部
		if (index < 0) {
			return innerHash(p);
		}

		// 非空,取属性：按序号或名称取参数的属性
		String pName = pattern.substring(index + 1);
		return innerHash(ReflectUtil.getFieldValue(p, pName));
	}
}
