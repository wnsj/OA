package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.dao.EmployeeDao;
import com.jiubo.oa.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
