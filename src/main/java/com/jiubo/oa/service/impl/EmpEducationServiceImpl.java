package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmpEducationBean;
import com.jiubo.oa.dao.EmpEducationDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmpEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpEducationServiceImpl extends ServiceImpl<EmpEducationDao, EmpEducationBean> implements EmpEducationService {

    @Autowired
    EmpEducationDao empEducationDao;

    @Override
    public EmpEducationBean queryEmpEducation(EmpEducationBean educationBean) {
        return empEducationDao.queryEmpEducation(educationBean);
    }

    @Override
    public void addEmpEducation(EmpEducationBean educationBean) throws Exception {
        if(empEducationDao.addEmpEducation(educationBean)<=0) throw new MessageException("添加员工学历信息失败");
    }

    @Override
    public void updateByEducatId(EmpEducationBean educationBean) throws Exception {
        if(empEducationDao.updateByEducatId(educationBean)<=0) throw new MessageException("修改员工学历信息失败");
    }
}
