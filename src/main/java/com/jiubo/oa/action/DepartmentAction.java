package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.DepartmentBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.DepartmentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc:
 * @date: 2020-01-04 14:42
 * @author: dx
 * @version: 1.0
 */
@RestController
@RequestMapping("/departmentAction")
public class DepartmentAction {

    @Autowired
    private DepartmentService departmentService;

    //查询部门
    @PostMapping(value = "/queryDepartment")
    public JSONObject queryDepartment(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        DepartmentBean departmentBean = JSONObject.parseObject(params, DepartmentBean.class);
        jsonObject.put(Constant.Result.RETDATA, departmentService.queryDepartment(departmentBean));
        return jsonObject;
    }
}
