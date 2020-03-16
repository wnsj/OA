package com.jiubo.oa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.WxTokenBean;
import com.jiubo.oa.service.CommonService;
import com.jiubo.oa.service.WxSendMessageService;
import com.jiubo.oa.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @desc:微信公众号消息推送
 * @date: 2019-10-21 16:46
 * @author: dx
 * @version: 1.0
 */
@Service
public class WxSendMessageServiceImpl implements WxSendMessageService {

    private static JSONObject accessTokenJSONObject = null;

    private Logger log = LoggerFactory.getLogger(WxSendMessageServiceImpl.class);

    @Value("${appId}")
    private String appId;

    @Value("${appSecret}")
    private String appSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommonService commonService;

    //获取AccessToken接口
    private final String requestAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    //微信公众号信息推送接口
    private final String sendMessageUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    //从数据库获取token判断是否过期
    private void queryAccessToken() throws Exception {
        if (accessTokenJSONObject != null) {
            Date lastTime = accessTokenJSONObject.getDate("lastTime");
            //判断token是否过期
            if (lastTime == null || (TimeUtil.getDBTime().getTime() - lastTime.getTime()) / 1000 > 3600) {
                accessTokenJSONObject = null;
                queryAccessToken();
            }
        } else {
            synchronized (WxSendMessageServiceImpl.class) {
                if (accessTokenJSONObject == null) {
                    WxTokenBean wxTokenBean = commonService.queryWxToken();
                    if (wxTokenBean == null) {
                        wxTokenBean = new WxTokenBean();
                        commonService.addWxToken(wxTokenBean);
                        requestAccessToken(wxTokenBean);
                    } else {
                        if ((TimeUtil.getDBTime().getTime() - wxTokenBean.getCreateDate().getTime()) / 1000 > 3600) {
                            //token 超时
                            requestAccessToken(wxTokenBean);
                        } else {
                            //token未超时
                            accessTokenJSONObject = new JSONObject();
                            accessTokenJSONObject.put("access_token", wxTokenBean.getToken());
                            accessTokenJSONObject.put("lastTime", wxTokenBean.getCreateDate());
                        }
                    }
                }
            }
        }
    }

    //请求微信获取ACCESS_TOKEN
    private void requestAccessToken(WxTokenBean wxTokenBean) throws Exception {
        String url = requestAccessTokenUrl.concat("&appid=").concat(appId).concat("&secret=").concat(appSecret);
        ResponseEntity<JSONObject> forEntity = restTemplate.getForEntity(url, JSONObject.class);
        JSONObject body = forEntity.getBody();
        if (body != null && "0".equals(body.getString("errcode"))) {
            Date now = TimeUtil.getDBTime();
            accessTokenJSONObject = body;
            accessTokenJSONObject.put("lastTime", now);
            wxTokenBean.setCreateDate(now);
            wxTokenBean.setToken(accessTokenJSONObject.getString("access_token"));
            commonService.updateWxToken(wxTokenBean);
        } else if (body != null && "7200".equals(body.getString("expires_in")) && StringUtils.isNotBlank(body.getString("access_token"))) {
            Date now = TimeUtil.getDBTime();
            accessTokenJSONObject = body;
            accessTokenJSONObject.put("lastTime", now);
            wxTokenBean.setCreateDate(now);
            wxTokenBean.setToken(accessTokenJSONObject.getString("access_token"));
            commonService.updateWxToken(wxTokenBean);
        } else {
            accessTokenJSONObject = null;
            log.error("获取ACCESS_TOKEN失败![错误信息:{}]", body);
        }
    }

    @Override
    public String getAccessToken() throws Exception {
        queryAccessToken();
        if (accessTokenJSONObject != null) {
            return accessTokenJSONObject.getString("access_token");
        }
        return null;
    }

    //推送消息
    private void send(JSONObject jsonObject, int flag) throws Exception {
        String accessToken = getAccessToken();
        //log.error("获取到的accessToken数据{}",accessToken);
        if (StringUtils.isNotBlank(accessToken) && flag == flag) {
            String url = sendMessageUrl.concat(accessToken);

            JSONObject resData = restTemplate.postForObject(url, jsonObject, JSONObject.class);
            if (resData == null) {
                log.error("没有响应数据!");
            } else {
                int errcode = resData.getInteger("errcode");
                if (errcode == 0) {
                    log.debug("消息推送成功!");
                } else if (errcode == 40001) {
                    log.error("错误代码【40001】");
                    accessTokenJSONObject = null;
                    commonService.deleteWxToken(new WxTokenBean());
                    send(jsonObject, ++flag);
                } else if (errcode == 41006) {
                    accessTokenJSONObject = null;
                    send(jsonObject, ++flag);
                } else {
                    log.error("消息推送失败,错误信息:{},消息:{}", resData, jsonObject);
                }
            }
        }
    }

    @Override
    public void sendMessage(JSONObject jsonObject) throws Exception {
        System.out.println("jsonObject"+jsonObject.toJSONString());
        send(jsonObject, 0);
    }
}
