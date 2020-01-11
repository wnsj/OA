package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.ForgetCardBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.ForgetCardService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc:忘记打卡接口
 * @date: 2020-01-11 09:11
 * @author: dx
 * @version: 1.0
 */
@RestController
@RequestMapping("/forgetCardAction")
public class ForgetCardAction {

    @Autowired
    private ForgetCardService forgetCardService;

    //查询忘记打卡
    @PostMapping(value = "/queryForgetCard")
    public JSONObject queryForgetCard(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        ForgetCardBean forgetCardBean = JSONObject.parseObject(params, ForgetCardBean.class);
        jsonObject.put(Constant.Result.RETDATA, forgetCardService.queryForgetCard(forgetCardBean));
        return jsonObject;
    }

    //添加忘记打卡
    @PostMapping(value = "/applyForgetCard")
    public JSONObject applyForgetCard(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        ForgetCardBean forgetCardBean = JSONObject.parseObject(params, ForgetCardBean.class);
        forgetCardService.applyForgetCard(forgetCardBean);
        return jsonObject;
    }

    //忘记打卡修改、取消、审核
    @PostMapping(value = "/operationForgetCard")
    public JSONObject operationForgetCard(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        ForgetCardBean forgetCardBean = JSONObject.parseObject(params, ForgetCardBean.class);
        forgetCardService.operationForgetCard(forgetCardBean);
        return jsonObject;
    }
}
