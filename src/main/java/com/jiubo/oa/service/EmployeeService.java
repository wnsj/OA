package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.EmployeeBean;

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
}
