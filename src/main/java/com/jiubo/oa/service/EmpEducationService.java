package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.EmpEducationBean;
import com.jiubo.oa.exception.MessageException;

import java.util.List;

public interface EmpEducationService extends IService<EmpEducationBean> {

    //查询员工学历信息
    EmpEducationBean queryEmpEducation(EmpEducationBean educationBean);

    //新增员工学历信息
    void addEmpEducation(EmpEducationBean educationBean) throws MessageException, Exception;

    //修改员工学历信息
    void updateByEducatId(EmpEducationBean educationBean) throws MessageException, Exception;
}
