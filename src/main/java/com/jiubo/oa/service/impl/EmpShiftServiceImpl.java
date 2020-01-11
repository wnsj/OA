package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmpShiftBean;
import com.jiubo.oa.dao.EmpShiftDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmpShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpShiftServiceImpl  extends ServiceImpl<EmpShiftDao, EmpShiftBean> implements EmpShiftService {

    @Autowired
    EmpShiftDao empShiftDao;

    @Override
    public List<EmpShiftBean> queryEmpShift(EmpShiftBean empShiftBean) {
        return empShiftDao.queryEmpShift(empShiftBean);
    }

    @Override
    public void addEmpShift(EmpShiftBean empShiftBean) throws Exception {
        if(empShiftDao.addEmpShift(empShiftBean)<=0) throw new MessageException("添加员工调岗失败");
    }
}
