package com.jiubo.oa.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc:
 * @date: 2019-10-21 16:45
 * @author: dx
 * @version: 1.0
 */
public interface WxSendMessageService {

    //获取AccessToken
    public String getAccessToken() throws Exception;

    //调用微信接口推送消息
    public void sendMessage(JSONObject jsonObject) throws Exception;
}
