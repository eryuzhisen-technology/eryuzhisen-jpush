package com.eryuzhisen.jpush.log;

import org.apache.log4j.Logger;

public class JpushLogFactory {

	public static Logger getPushLogger(Class<?> clazz) {
		return Logger.getLogger("push");
	}

	public static Logger getReportsLogger(Class<?> clazz) {
		return Logger.getLogger("reports");
	}
}
