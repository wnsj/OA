package com.jiubo.oa.service;

import com.alibaba.fastjson.JSONObject;
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

    //用户登录
    public JSONObject login(EmployeeBean employeeBean) throws Exception;

    //绑定openId
    public void bindOpenId(EmployeeBean employeeBean)throws Exception;

    //修改
    public void updateEmployee(EmployeeBean employeeBean) throws Exception;
}