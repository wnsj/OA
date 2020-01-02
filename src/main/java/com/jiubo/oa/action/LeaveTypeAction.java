package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.LeaveTypeBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.LeaveTypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc:
 * @date: 2020-01-02 16:35
 * @author: dx
 * @version: 1.0
 */
@RestController
@RequestMapping("/leaveTypeAction")
public class LeaveTypeAction {

    @Autowired
    private LeaveTypeService leaveTypeService;

    //查询请假类型
    @PostMapping(value = "/queryLeaveType")
    public JSONObject queryLeaveType(@RequestBody String params)throws Exception{
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        LeaveTypeBean leaveTypeBean = JSONObject.parseObject(params, LeaveTypeBean.class);
        jsonObject.put(Constant.Result.RETDATA, leaveTypeService.queryLeaveType(leaveTypeBean));
        return jsonObject;
    }
}
