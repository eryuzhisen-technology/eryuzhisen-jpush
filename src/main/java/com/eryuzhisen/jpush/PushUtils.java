package com.eryuzhisen.jpush;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.InterfaceAdapter;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.IosNotification.Builder;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;

import com.eryuzhisen.jpush.log.JpushLogFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 推送Api
 * 
 * @author ningzhang
 *
 */
public class PushUtils {
	protected static final Logger logger = JpushLogFactory
			.getPushLogger(PushUtils.class);

	/**
	 * 推送Push消息到IOS
	 * 
	 * @param alertMsg
	 *            :通知内容,内容为空则不展示到通知栏
	 * @param msgContent
	 *            :消息内容体。是被推送到客户端的内容 ,与 notification(消息体内容) 一起二者必须有其一，可以二者并存
	 * @param audience
	 *            :推送设备指定<Audience.tag_and("tag1", "tag_all")>
	 * @param sound
	 *            :通知提示声音，如果找到了指定的声音就播放该声音，否则播放默认声音<"happy">
	 * @param extraKey
	 *            :附加字段，自定义 Key/value 信息<"from", "JPush">
	 * @param extraValue
	 *            :
	 * @param apns
	 *            : true生产环境，false开发环境
	 */
	public static PushResult sendIosPush(String masterSecret, String appKey,
			String alertMsg, String msgContent, Audience audience,
			Map<String, String> iosExtraMap, boolean apns)
			throws APIConnectionException, APIRequestException {
		if (StringUtils.isEmpty(alertMsg)) {
			logger.error("sendIosPush alertMsg is null or empty!");
			return null;
		}
		if (msgContent == null) {
			logger.error("sendIosPush msgContent is null!");
			return null;
		}
		if (audience == null) {
			logger.error("sendIosPush Audience is null!");
			return null;
		}

		Builder iosBuilder = IosNotification.newBuilder();
		iosBuilder.setAlert(alertMsg).setBadge(1);

		if (iosExtraMap != null && iosExtraMap.size() > 0) {
			iosBuilder.addExtras(iosExtraMap);
		}

		ClientConfig clientConfig = ClientConfig.getInstance();
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, null,
				clientConfig);
		PushPayload payload = PushPayload
				.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(audience)
				.setNotification(
						Notification.newBuilder()
								.addPlatformNotification(iosBuilder.build())
								.build())
				.setMessage(Message.content(msgContent)).setOptions(// APNs：true生产环境，false开发环境
						Options.newBuilder().setApnsProduction(apns).build())
				.build();

		PushResult result = jpushClient.sendPush(payload);
		return result;

		// try {
		// PushResult result = jpushClient.sendPush(payload);
		// logger.info("sendIosPush Got result - " + result);
		// return result;
		// } catch (APIConnectionException e) {
		// logger.error("sendIosPush Connection error. Should retry later. ",
		// e);
		// return null;
		// } catch (APIRequestException e) {
		// logger.error(
		// "sendIosPush Error response from JPush server. Should review and fix it. ",
		// e);
		// logger.info("sendIosPush HTTP Status: " + e.getStatus());
		// logger.info("sendIosPush Error Code: " + e.getErrorCode());
		// logger.info("sendIosPush Error Message: " + e.getErrorMessage());
		// logger.info("sendIosPush Msg ID: " + e.getMsgId());
		// return null;
		// }
	}

	/**
	 * 推送Push消息到IOS或Android, Json格式
	 * 
	 * @param payloadString
	 *            : Json格式
	 */
	// Json格式样例：需要反编译
	// {\"platform\":{\"all\":false,\"deviceTypes\":[\"IOS\"]},\"audience\":{\"all\":false,\"targets\":[{\"audienceType\":\"TAG_AND\",\"values\":[\"tag1\",\"tag_all\"]}]},\"notification\":{\"notifications\":[{\"soundDisabled\":false,\"badgeDisabled\":false,\"sound\":\"happy\",\"badge\":\"5\",\"contentAvailable\":false,\"alert\":\"Test
	// from API Example -
	// alert\",\"extras\":{\"from\":\"JPush\"},\"type\":\"cn.jpush.api.push.model.notification.IosNotification\"}]},\"message\":{\"msgContent\":\"Test
	// from API Example -
	// msgContent\"},\"options\":{\"sendno\":1429488213,\"overrideMsgId\":0,\"timeToLive\":-1,\"apnsProduction\":true,\"bigPushDuration\":0}}
	public static PushResult sendPush_fromJSON(String masterSecret,
			String appKey, String payloadString) throws APIConnectionException,
			APIRequestException {
		ClientConfig clientConfig = ClientConfig.getInstance();
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, null,
				clientConfig);
		Gson gson = new GsonBuilder().registerTypeAdapter(
				PlatformNotification.class,
				new InterfaceAdapter<PlatformNotification>()).create();
		// Since the type of DeviceType is enum, thus the value should be
		// uppercase, same with the AudienceType.
		PushPayload payload = gson.fromJson(payloadString, PushPayload.class);

		PushResult result = jpushClient.sendPush(payload);
		return result;

		// try {
		// PushResult result = jpushClient.sendPush(payload);
		// logger.info("Got result - " + result);
		// return result;
		// } catch (APIConnectionException e) {
		// logger.error("Connection error. Should retry later. ", e);
		// return null;
		// } catch (APIRequestException e) {
		// logger.error(
		// "Error response from JPush server. Should review and fix it. ",
		// e);
		// logger.info("HTTP Status: " + e.getStatus());
		// logger.info("Error Code: " + e.getErrorCode());
		// logger.info("Error Message: " + e.getErrorMessage());
		// logger.info("Msg ID: " + e.getMsgId());
		// return null;
		// }
	}

	/**
	 * 推送Push消息到Android
	 * 
	 * @param alertMsg
	 *            :通知内容,内容为空则不展示到通知栏
	 * @param title
	 *            :通知标题
	 * @param msgContent
	 *            :消息内容体。是被推送到客户端的内容 ,与 notification 一起二者必须有其一，可以二者并存
	 * @param audience
	 *            :推送设备指定<Audience.tag_and("tag1", "tag_all")>
	 * @param extraMap
	 *            :扩展字段
	 * @param apns
	 *            : true生产环境，false开发环境
	 */
	public static PushResult sendAndroidPush(String masterSecret,
			String appKey, String alertMsg, String title, String msgContent,
			Audience audience, Map<String, String> extraMap, boolean apns)
			throws APIConnectionException, APIRequestException {
		if (StringUtils.isEmpty(alertMsg)) {
			logger.error("sendAndroidPush alertMsg is null or empty!");
			return null;
		}
		if (audience == null) {
			logger.error("sendAndroidPush Audience is null!");
			return null;
		}

		cn.jpush.api.push.model.notification.AndroidNotification.Builder androidBuilder = AndroidNotification
				.newBuilder();
		androidBuilder.setAlert(alertMsg);

		if (StringUtils.isNotBlank(title)) {
			androidBuilder.setTitle(title);
		}

		if (extraMap != null && extraMap.size() > 0) {
			androidBuilder.addExtras(extraMap);
		}

		Notification notification = Notification.newBuilder()
				.addPlatformNotification(androidBuilder.build()).build();

		PushPayload androidBuild = PushPayload.newBuilder()
				.setPlatform(Platform.android()).setAudience(audience)
				.setNotification(notification)
				.setMessage(Message.content(msgContent)).setOptions(// APNs：true生产环境，false开发环境
						Options.newBuilder().setApnsProduction(apns).build())
				.build();

		ClientConfig clientConfig = ClientConfig.getInstance();
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, null,
				clientConfig);

		PushResult result = jpushClient.sendPush(androidBuild);
		return result;

		// try {
		// PushResult result = jpushClient.sendPush(androidBuild);
		// logger.info("sendAndroidPush Got result - " + result);
		// return result;
		// } catch (APIConnectionException e) {
		// logger.error(
		// "sendAndroidPush Connection error. Should retry later. ", e);
		// return null;
		// } catch (APIRequestException e) {
		// logger.error(
		// "sendAndroidPush Error response from JPush server. Should review and fix it. ",
		// e);
		// logger.info("sendAndroidPush HTTP Status: " + e.getStatus());
		// logger.info("sendAndroidPush Error Code: " + e.getErrorCode());
		// logger.info("sendAndroidPush Error Message: " + e.getErrorMessage());
		// logger.info("sendAndroidPush Msg ID: " + e.getMsgId());
		// return null;
		// }
	}

	/**
	 * 推送Push消息到Ios_And_Android
	 * 
	 * @param alertMsg
	 *            :通知内容,内容为空则不展示到通知栏
	 * @param msgContent
	 *            :消息内容体。是被推送到客户端的内容 ,与 notification 一起二者必须有其一，可以二者并存
	 * @param andriodTitle
	 *            :通知标题
	 * @param audience
	 *            :推送设备指定<Audience.tag_and("tag1", "tag_all")>
	 * @param iosExtraMap
	 *            :ios附加字段，自定义 Key/value 信息<"from", "JPush">
	 * @param AndroidExtraMap
	 *            :android附加字段，自定义 Key/value 信息<"from", "JPush">
	 * @param msgExtraMap
	 *            :该通知消息附加字段，自定义 Key/value 信息<"from", "JPush">
	 * @param apns
	 *            :true生产环境，false开发环境
	 * @return
	 */
	public static PushResult sendIosAndAndroidPush(String masterSecret,
			String appKey, String alertMsg, String msgContent,
			String andriodTitle, Audience audience,
			Map<String, String> iosExtraMap,
			Map<String, String> AndroidExtraMap,
			Map<String, String> msgExtraMap, boolean apns)
			throws APIConnectionException, APIRequestException {

		if (StringUtils.isBlank(alertMsg)) {
			logger.error("sendIosAndAndroidPush alertMsg is null or empty!");
			return null;
		}

		if (audience == null) {
			logger.error("sendIosAndAndroidPush audience is null or empty!");
			return null;
		}

		Builder iosBuilder = IosNotification.newBuilder();
		iosBuilder.incrBadge(1);
		if (iosExtraMap != null && iosExtraMap.size() > 0) {
			iosBuilder.addExtras(iosExtraMap);
		}

		cn.jpush.api.push.model.notification.AndroidNotification.Builder androidBuilder = AndroidNotification
				.newBuilder();
		androidBuilder.setTitle(andriodTitle);
		if (AndroidExtraMap != null && AndroidExtraMap.size() > 0) {
			androidBuilder.addExtras(AndroidExtraMap);
		}

		cn.jpush.api.push.model.Message.Builder msgBuilder = Message
				.newBuilder();
		msgBuilder.setMsgContent(msgContent);
		if (msgExtraMap != null && msgExtraMap.size() > 0) {
			msgBuilder.addExtras(msgExtraMap);
		}

		PushPayload androidBuild = PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(audience)
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(alertMsg)
								.addPlatformNotification(iosBuilder.build())
								.addPlatformNotification(androidBuilder.build())
								.build())

				.setMessage(msgBuilder.build()).setOptions(// APNs：true生产环境，false开发环境
						Options.newBuilder().setApnsProduction(apns).build())
				.build();

		ClientConfig clientConfig = ClientConfig.getInstance();

		JPushClient jpushClient = new JPushClient(masterSecret, appKey, null,
				clientConfig);

		PushResult result = jpushClient.sendPush(androidBuild);
		return result;

		// try {
		// PushResult result = jpushClient.sendPush(androidBuild);
		// logger.info("sendAndroidPush Got result - " + result);
		// return result;
		// } catch (APIConnectionException e) {
		// logger.error(
		// "sendAndroidPush Connection error. Should retry later. ", e);
		// return null;
		// } catch (APIRequestException e) {
		// logger.error(
		// "sendAndroidPush Error response from JPush server. Should review and fix it. ",
		// e);
		// logger.info("sendAndroidPush HTTP Status: " + e.getStatus());
		// logger.info("sendAndroidPush Error Code: " + e.getErrorCode());
		// logger.info("sendAndroidPush Error Message: " + e.getErrorMessage());
		// logger.info("sendAndroidPush Msg ID: " + e.getMsgId());
		// return null;
		// }
	}

	public static void main(String[] args) {/*
		// 1广播, 给所有人发
		// PushResult pushResult1 = sendIosPush("测试Jpush4", "测试Jpush4",
		// Audience.all(), null);
		// System.out.println(pushResult1);

		// 2给指定用户发(userId)
		Audience audience = Audience.newBuilder()
				.addAudienceTarget(AudienceTarget.alias("10001", "10003"))
				.build();
		// PushResult pushResult2 = sendIosPush("测试Jpush2", "测试Jpush2",
		// audience,
		// null);
		// System.out.println(pushResult2);

		// 发送Json
		PushPayload payload = PushPayload
				.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(Audience.all())
				.setNotification(Notification.alert("测试Json1"))
				.setMessage(Message.content("测试Json11"))
				.setOptions(
						Options.newBuilder().setApnsProduction(false).build())
				.build();

		String jsonMsg = JsonUtil.toJson(payload);
		System.out.println(jsonMsg);
		try {
			PushResult pushResult3 = sendPush_fromJSON(
					JpushConstants.MASTER_SECRET, JpushConstants.APP_KEY,
					jsonMsg);
			System.out.println(pushResult3);
		} catch (Exception e) {
			e.printStackTrace();
		}

	*/}

}
