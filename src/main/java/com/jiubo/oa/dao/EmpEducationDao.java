package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.EmpEducationBean;

/**
 * 学历信息
 */
public interface EmpEducationDao extends BaseMapper<EmpEducationBean> {

    //添加员工学历信息
    int addEmpEducation(EmpEducationBean empEducationBean);

    //根据id修改员工学历信息
    int updateByEducatId(EmpEducationBean empEducationBean);

    //查询员工学历信息
    EmpEducationBean queryEmpEducation(EmpEducationBean empEducationBean);
}
