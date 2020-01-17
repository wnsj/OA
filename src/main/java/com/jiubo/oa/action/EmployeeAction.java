package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.EmpShiftBean;
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
    public JSONObject queryEmployee(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        jsonObject.put(Constant.Result.RETDATA, employeeService.queryEmployee(employeeBean));
        return jsonObject;
    }

    //查询部门负责人
    @PostMapping(value = "/queryDeptDrector")
    public JSONObject queryDeptDrector(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        jsonObject.put(Constant.Result.RETDATA, employeeService.queryDeptDrector(employeeBean));
        return jsonObject;
    }

    //查询有部门负责人部门
    @PostMapping(value = "/queryEmployeeDept")
    public JSONObject queryEmployeeDept(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        jsonObject.put(Constant.Result.RETDATA, employeeService.queryEmployeeDept(employeeBean));
        return jsonObject;
    }

    //登录
    @PostMapping(value = "/login")
    public JSONObject login(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        jsonObject.put(Constant.Result.RETDATA, employeeService.login(employeeBean));
        return jsonObject;
    }

    //微信绑定
    @PostMapping(value = "/bindOpenId")
    public JSONObject bindOpenId(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        employeeService.bindOpenId(employeeBean);
        return jsonObject;
    }


    //新增员工基本信息
    @PostMapping(value = "/addEmployee")
    public JSONObject addEmployee(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        employeeService.addEmployee(employeeBean);
        return jsonObject;
    }


    //修改员工基本信息
    @PostMapping(value = "/updateEmployee")
    public JSONObject updateEmployee(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        employeeService.updateEmployee(employeeBean);
        return jsonObject;
    }

    //岗位调动
    @PostMapping(value = "/postTransfer")
    public JSONObject postTransfer(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmpShiftBean empShiftBean = JSONObject.parseObject(params, EmpShiftBean.class);
        employeeService.postTransfer(empShiftBean);
        return jsonObject;
    }

    //根据empId查询员工基本信息，学历信息，员工家庭信息，员工岗位调动
    @PostMapping(value = "/queryAllByEmpId")
    public JSONObject queryAllByEmpId(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        jsonObject.put(Constant.Result.RETDATA, employeeService.queryAllByEmpId(employeeBean.getEmpId()));
        return jsonObject;
    }

    //查询考勤审核人
    @PostMapping(value = "/queryAttendanceExamine")
    public JSONObject queryAttendanceExamine(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        jsonObject.put(Constant.Result.RETDATA, employeeService.queryAttendanceExamine(employeeBean));
        return jsonObject;
    }

    //查询报销审核人
    @PostMapping(value = "/queryReimbursementExamine")
    public JSONObject queryReimbursementExamine(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        EmployeeBean employeeBean = JSONObject.parseObject(params, EmployeeBean.class);
        jsonObject.put(Constant.Result.RETDATA, employeeService.queryReimbursementExamine(employeeBean));
        return jsonObject;
    }
}
