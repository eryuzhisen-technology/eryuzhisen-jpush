package com.eryuzhisen.jpush.device;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import cn.jiguang.common.resp.BaseResult;

@SuppressWarnings("serial")
public class AliasDeviceListResult extends BaseResult {
   
	@Expose public List<String> registration_ids = new ArrayList<String>();

}

