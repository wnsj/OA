package com.jiubo.oa.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.EmpShiftBean;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.exception.MessageException;

import java.util.List;

/**
 * @desc:员工服务类
 * @date: 2020-01-02 15:48
 * @author: dx
 * @version: 1.0
 */
public interface EmployeeService extends IService<EmployeeBean> {

    //查询员工
    public List<EmployeeBean> queryEmployee(EmployeeBean employeeBean);

    //查询部门负责人
    public List<EmployeeBean> queryDeptDrector(EmployeeBean employeeBean);

    //用户登录
    public JSONObject login(EmployeeBean employeeBean) throws Exception;

    //绑定openId
    public void bindOpenId(EmployeeBean employeeBean) throws Exception;

    //修改
    public void updateEmployee(EmployeeBean employeeBean) throws Exception;

    //新增员工基本信息
    public void addEmployee(EmployeeBean employeeBean) throws Exception;

    //岗位调动
    public void postTransfer(EmpShiftBean empShiftBean) throws Exception;

    public EmployeeBean queryAllByEmpId(String empId) throws Exception;

    //查询考勤审查人、审核人、批准人
    public List<EmployeeBean> queryAttendanceExamine(EmployeeBean employeeBean) throws Exception;

    //查询报销审查人、审核人、批准人
    public List<EmployeeBean> queryReimbursementExamine(EmployeeBean employeeBean) throws Exception;


}