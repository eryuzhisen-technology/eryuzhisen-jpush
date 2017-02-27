package com.eryuzhisen.jpush.push.model.audience;

public enum AudienceType {
	TAG("tag"), //数组，指定的某个设备名, 一次推送最多 20 个
	TAG_AND("tag_and"), //数组，指定的多个设备名, 一次推送最多 20 个
	ALIAS("alias"), //数组，推送目标<唯一Id>, 送最多 1000 个
	SEGMENT("segment"), //用户群
	REGISTRATION_ID("registration_id"); //数组，多个注册ID, 一次推送最多 1000 个
	
	private final String value;
	private AudienceType(final String value) {
		this.value = value;
	}
	public String value() {
		return this.value;
	}
	
	
}
