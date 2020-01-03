package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.AskLeaveBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.AskLeaveService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc:
 * @date: 2020-01-03 08:45
 * @author: dx
 * @version: 1.0
 */
@RestController
@RequestMapping("/askLeaveAction")
public class AskLeaveAction {

    @Autowired
    private AskLeaveService askLeaveService;

    //查询请假申请
    @PostMapping(value = "/queryAskLeave")
    public JSONObject queryAskLeave(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        AskLeaveBean askLeaveBean = JSONObject.parseObject(params, AskLeaveBean.class);
        jsonObject.put(Constant.Result.RETDATA, askLeaveService.queryAskLeave(askLeaveBean));
        return jsonObject;
    }

    //添加请假申请
    @PostMapping(value = "/addAskLeave")
    public JSONObject addAskLeave(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        AskLeaveBean askLeaveBean = JSONObject.parseObject(params, AskLeaveBean.class);
        askLeaveService.addAskLeave(askLeaveBean);
        return jsonObject;
    }

    //取消，审核请假申请
    @PostMapping(value = "/operationLeave")
    public JSONObject operationLeave(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        AskLeaveBean askLeaveBean = JSONObject.parseObject(params, AskLeaveBean.class);
        askLeaveService.operationLeave(askLeaveBean);
        return jsonObject;
    }
}
