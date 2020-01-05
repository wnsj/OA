package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.DepartmentBean;
import com.jiubo.oa.dao.DepartmentDao;
import com.jiubo.oa.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-02 14:53
 * @author: dx
 * @version: 1.0
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentDao, DepartmentBean> implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public List<DepartmentBean> queryDepartment(DepartmentBean departmentBean) {
        return departmentDao.queryDepartment(departmentBean);
    }
}
