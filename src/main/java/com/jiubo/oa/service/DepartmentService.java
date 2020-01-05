package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.DepartmentBean;
import org.apache.catalina.LifecycleState;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-02 14:52
 * @author: dx
 * @version: 1.0
 */
public interface DepartmentService extends IService<DepartmentBean> {

    //查询部门
    public List<DepartmentBean> queryDepartment(DepartmentBean departmentBean);

}
