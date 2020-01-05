package com.jiubo.oa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.dao.EmployeeDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-02 15:49
 * @author: dx
 * @version: 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, EmployeeBean> implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public List<EmployeeBean> queryEmployee(EmployeeBean employeeBean) {
        return employeeDao.queryEmployee(employeeBean);
    }

    @Override
    public JSONObject login(EmployeeBean employeeBean) throws Exception {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isBlank(employeeBean.getAccName())) throw new MessageException("账号不能为空!");
        if (StringUtils.isBlank(employeeBean.getAccPwd())) throw new MessageException("密码不能为空!");
        List<EmployeeBean> employeeBeans = queryEmployee(new EmployeeBean().setAccName(employeeBean.getAccName()).setShowPwd("1"));
        if (employeeBeans.isEmpty()) throw new MessageException(employeeBean.getAccName() + "账号不存在!");
        EmployeeBean employee = employeeBeans.get(0);
        if (!employee.getAccPwd().equals(employeeBean.getAccPwd())) throw new MessageException("密码错误!");
        employee.setAccPwd("");
        jsonObject.put("empData", employee);
        return jsonObject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmployee(EmployeeBean employeeBean) throws Exception {
        if (employeeDao.updateEmployee(employeeBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindOpenId(EmployeeBean employeeBean) throws Exception {
        if (StringUtils.isBlank(employeeBean.getAccName())) throw new MessageException("账号不能为空!");
        if (StringUtils.isBlank(employeeBean.getAccPwd())) throw new MessageException("密码不能为空!");
        if (StringUtils.isBlank(employeeBean.getOpenId())) throw new MessageException("openId为空!");
        List<EmployeeBean> employeeBeans = queryEmployee(new EmployeeBean().setAccName(employeeBean.getAccName()).setShowPwd("1"));
        if (employeeBeans.isEmpty()) throw new MessageException(employeeBean.getAccName() + "账号不存在!");
        EmployeeBean employee = employeeBeans.get(0);
        if (!employee.getAccPwd().equals(employeeBean.getAccPwd())) throw new MessageException("密码错误!");
        if (StringUtils.isNotBlank(employee.getOpenId()))throw new MessageException("该账号已绑定微信，不可重复绑定!");
        employeeBeans = queryEmployee(new EmployeeBean().setOpenId(employeeBean.getOpenId()));
        if (employeeBeans.size() > 0)throw new MessageException("该微信已绑定账号,不可重复绑定!");
        updateEmployee(new EmployeeBean().setEmpId(employee.getEmpId()).setOpenId(employeeBean.getOpenId()));
    }
}
