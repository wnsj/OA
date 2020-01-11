package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.EmpFamilyBean;
import com.jiubo.oa.bean.EmpShiftBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmpShiftService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empShiftAction")
public class EmpShiftAction {

    @Autowired
    EmpShiftService empShiftService;


    //查询员工调岗信息
    @PostMapping(value = "/queryEmpShift")
    public JSONObject queryEmpShift(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmpShiftBean empShiftBean = JSONObject.parseObject(params, EmpShiftBean.class);
        jsonObject.put(Constant.Result.RETDATA, empShiftService.queryEmpShift(empShiftBean));
        return jsonObject;
    }

    //新增员工调岗信息
    @PostMapping(value = "/addEmpShift")
    public JSONObject addEmpShift(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmpShiftBean empShiftBean = JSONObject.parseObject(params, EmpShiftBean.class);
        empShiftService.addEmpShift(empShiftBean);
        return jsonObject;
    }

}
