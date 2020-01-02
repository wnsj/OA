package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc:员工接口
 * @date: 2020-01-02 16:15
 * @author: dx
 * @version: 1.0
 */
@RestController
@RequestMapping("/employeeAction")
public class EmployeeAction {

    @Autowired
    private EmployeeService employeeService;

    //查询员工
    @PostMapping(value = "/queryEmployee")
    public JSONObject queryEmployee(@RequestBody String params)throws Exception{
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        jsonObject.put(Constant.Result.RETDATA, employeeService.queryEmployee(employeeBean));
        return jsonObject;
    }
}
