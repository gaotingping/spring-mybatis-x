package com.mvw.submeter.common;

import java.lang.reflect.Field;

/**
 * 反射工具类(反射和代理已经根植于大多数框架中了)
 */
public class ReflectUtil {

	/**
	 * 反射获得字段的值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object result = null;
		final Field field = ReflectUtil.getField(obj, fieldName);
		if (field != null) {
			field.setAccessible(true);
			try {
				result = field.get(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 根据字段名获得其反射对象
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	private static Field getField(Object obj, String fieldName) {
		Field field = null;
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				//e.printStackTrace();
			}
		}
		return field;
	}

	/**
	 * 反射设置字段的值
	 * 
	 * @param obj
	 * @param fieldName
	 * @param fieldValue
	 */
	public static void setFieldValue(Object obj, String fieldName, String fieldValue) {
		final Field field = ReflectUtil.getField(obj, fieldName);
		if (field != null) {
			try {
				field.setAccessible(true);
				field.set(obj, fieldValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isBaseType(Class<?> c) {
		if (c.isPrimitive()) {
			return true;
		} else if (c == String.class) {
			return true;
		} else if (c == Integer.class) {
			return true;
		} else if (c == Long.class) {
			return true;
		} else if (c == Double.class) {
			return true;
		} else if (c == Float.class) {
			return true;
		} else if (c == Boolean.class) {
			return true;
		}else if (c == Short.class) {
			return true;
		}else if (c == Byte.class) {
			return true;
		}else if (c == Character.class) {
			return true;
		}
		return false;
	}
}
