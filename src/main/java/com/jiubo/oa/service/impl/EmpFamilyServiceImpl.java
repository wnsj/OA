package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmpFamilyBean;
import com.jiubo.oa.dao.EmpFamilyDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmpFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpFamilyServiceImpl extends ServiceImpl<EmpFamilyDao, EmpFamilyBean> implements EmpFamilyService {

    @Autowired
    EmpFamilyDao empFamilyDao;

    @Override
    public List<EmpFamilyBean> queryEmpFamily(EmpFamilyBean empFamilyBean) {
        return empFamilyDao.queryEmpFamily(empFamilyBean);
    }

    @Override
    public void addEmpFamily(EmpFamilyBean empFamilyBean) throws Exception {
        ArrayList<EmpFamilyBean> familyBeanArrayList = new ArrayList<>();
        familyBeanArrayList.add(empFamilyBean);
        if (empFamilyDao.addEmpFamilyList(familyBeanArrayList)<=0) throw new MessageException("新增员工信息失败");
    }

    @Override
    public void updateEmpFamily(EmpFamilyBean empFamilyBean) throws Exception {
        if (empFamilyDao.updateEmpFamily(empFamilyBean)<=0) throw new MessageException("修改员工信息失败");
    }
}
