package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.EmpShiftBean;
import com.jiubo.oa.bean.EmployeeBean;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-02 15:50
 * @author: dx
 * @version: 1.0
 */
public interface EmployeeDao extends BaseMapper<EmployeeBean> {

    //查询员工
    public List<EmployeeBean> queryEmployee(EmployeeBean employeeBean);

    //添加员工
    public int addEmployee(EmployeeBean employeeBean);

    //修改员工
    public int updateEmployee(EmployeeBean employeeBean);

    //岗位调动
    int postTransfer(EmpShiftBean empShiftBean);

    //查询审核人
    List<EmployeeBean> queryExamine(EmployeeBean employeeBean);

    //查询部门负责人
    List<EmployeeBean> queryDeptDrector(EmployeeBean employeeBean);

    //查询有部门负责人的部门
    List<EmployeeBean> queryEmployeeDept(EmployeeBean employeeBean);
}
