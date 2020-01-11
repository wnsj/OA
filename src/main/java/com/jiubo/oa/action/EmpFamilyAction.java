package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.EmpFamilyBean;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmpFamilyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empFamilyAction")
public class EmpFamilyAction {

    @Autowired
    EmpFamilyService empFamilyService;

    //查询员工家庭信息
    @PostMapping(value = "/queryEmpFamily")
    public JSONObject queryEmployee(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmpFamilyBean empFamilyBean = JSONObject.parseObject(params, EmpFamilyBean.class);
        jsonObject.put(Constant.Result.RETDATA, empFamilyService.queryEmpFamily(empFamilyBean));
        return jsonObject;
    }

    //新增员工家庭信息
    @PostMapping(value = "/addEmpFamily")
    public JSONObject addEmpFamily(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmpFamilyBean empFamilyBean = JSONObject.parseObject(params, EmpFamilyBean.class);
        empFamilyService.addEmpFamily(empFamilyBean);
        return jsonObject;
    }


    //修改员工家庭信息
    @PostMapping(value = "/updateEmpFamily")
    public JSONObject updateEmpFamily(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmpFamilyBean empFamilyBean = JSONObject.parseObject(params, EmpFamilyBean.class);
        empFamilyService.updateEmpFamily(empFamilyBean);
        return jsonObject;
    }
}
