package com.eryuzhisen.jpush;

import org.apache.log4j.Logger;

import com.eryuzhisen.jpush.common.JpushConstants;
import com.eryuzhisen.jpush.log.JpushLogFactory;

import cn.jiguang.common.TimeUnit;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.report.MessagesResult;
import cn.jpush.api.report.ReceivedsResult;
import cn.jpush.api.report.UsersResult;

/**
 * 消息统计
 * 
 * @author ningzhang
 *
 */
public class ReportsUtils {
	protected static final Logger logger = JpushLogFactory
			.getReportsLogger(ReportsUtils.class);

	public static void main(String[] args) {
		testGetReport("1942377665");
		testGetMessages("269978303");
		testGetUsers("2014-06-10", 3);
	}

	public static void testGetReport(String msgIds) {
		JPushClient jpushClient = new JPushClient(JpushConstants.MASTER_SECRET,
				JpushConstants.APP_KEY);
		try {
			ReceivedsResult result = jpushClient.getReportReceiveds(msgIds);
			logger.debug("Got result - " + result);

		} catch (APIConnectionException e) {
			logger.error("Connection error. Should retry later. ", e);

		} catch (APIRequestException e) {
			logger.error(
					"Error response from JPush server. Should review and fix it. ",
					e);
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Code: " + e.getErrorCode());
			logger.info("Error Message: " + e.getErrorMessage());
		}
	}

	public static void testGetUsers(String start, int duration) {
		JPushClient jpushClient = new JPushClient(JpushConstants.MASTER_SECRET,
				JpushConstants.APP_KEY);
		try {
			UsersResult result = jpushClient.getReportUsers(TimeUnit.DAY,
					start, duration);
			logger.debug("Got result - " + result);

		} catch (APIConnectionException e) {
			logger.error("Connection error. Should retry later. ", e);

		} catch (APIRequestException e) {
			logger.error(
					"Error response from JPush server. Should review and fix it. ",
					e);
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Code: " + e.getErrorCode());
			logger.info("Error Message: " + e.getErrorMessage());
		}
	}

	public static void testGetMessages(String msgIds) {
		JPushClient jpushClient = new JPushClient(JpushConstants.MASTER_SECRET,
				JpushConstants.APP_KEY);
		try {
			MessagesResult result = jpushClient.getReportMessages(msgIds);
			logger.debug("Got result - " + result);

		} catch (APIConnectionException e) {
			logger.error("Connection error. Should retry later. ", e);

		} catch (APIRequestException e) {
			logger.error(
					"Error response from JPush server. Should review and fix it. ",
					e);
			logger.info("HTTP Status: " + e.getStatus());
			logger.info("Error Code: " + e.getErrorCode());
			logger.info("Error Message: " + e.getErrorMessage());
		}
	}

}
