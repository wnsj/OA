package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.EmpFamilyBean;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.exception.MessageException;

import java.util.List;

public interface EmpFamilyService extends IService<EmpFamilyBean> {

    //查询家庭信息
    List<EmpFamilyBean> queryEmpFamily(EmpFamilyBean empFamilyBean);

    //新增员工家庭信息
    void addEmpFamily(EmpFamilyBean empFamilyBean) throws MessageException, Exception;

    //修改员工家庭信息
    void updateEmpFamily(EmpFamilyBean empFamilyBean) throws Exception;
}
