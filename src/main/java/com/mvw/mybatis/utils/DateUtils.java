package com.mvw.mybatis.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	private static final String defFormat = "yyyy-MM-dd";

	public static String getSysTemDate() {
		return new SimpleDateFormat(defFormat).format(new Date());
	}

	public static String getLongDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String getLongDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}
}
