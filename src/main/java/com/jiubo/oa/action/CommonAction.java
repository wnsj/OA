package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.service.CommonService;
import com.jiubo.oa.service.WxSendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc:公共服务接口
 * @date: 2020-01-02 14:06
 * @author: dx
 * @version: 1.0
 */
@RestController
@RequestMapping("/commonAction")
public class CommonAction {

    @Autowired
    private CommonService commonService;

    @Autowired
    private WxSendMessageService wxSendMessageService;

    //获取数据库时间
    @RequestMapping(value = "/queryDBTime",method={RequestMethod.GET,RequestMethod.POST})
    public JSONObject queryDBTime(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        jsonObject.put(Constant.Result.RETDATA, commonService.queryDBTime());
        return jsonObject;
    }

    //获取微信AccessToken
    @RequestMapping(value = "/queryAccessToken",method={RequestMethod.GET,RequestMethod.POST})
    public JSONObject queryAccessToken()throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        jsonObject.put(Constant.Result.RETDATA, wxSendMessageService.getAccessToken());
        return jsonObject;
    }
}
