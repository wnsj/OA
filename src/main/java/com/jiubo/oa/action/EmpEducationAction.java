package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.EmpEducationBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmpEducationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empEducationAction")
public class EmpEducationAction {

    @Autowired
    EmpEducationService empEducationService;

    //查询员工学历信息
    @PostMapping(value = "/queryEmpEducation")
    public JSONObject queryEmpEducation(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmpEducationBean educationBean = JSONObject.parseObject(params, EmpEducationBean.class);
        jsonObject.put(Constant.Result.RETDATA, empEducationService.queryEmpEducation(educationBean));
        return jsonObject;
    }

    //新增员工学历信息
    @PostMapping(value = "/addEmpEducation")
    public JSONObject addEmpEducation(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmpEducationBean educationBean = JSONObject.parseObject(params, EmpEducationBean.class);
        empEducationService.addEmpEducation(educationBean);
        return jsonObject;
    }

    //修改员工调岗信息
    @PostMapping(value = "/updateByEducatId")
    public JSONObject updateByEducatId(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmpEducationBean educationBean = JSONObject.parseObject(params, EmpEducationBean.class);
        empEducationService.updateByEducatId(educationBean);
        return jsonObject;
    }
}
