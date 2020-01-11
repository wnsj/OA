package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.EmpShiftBean;
import com.jiubo.oa.exception.MessageException;

import java.util.List;

public interface EmpShiftService extends IService<EmpShiftBean> {

    //查询员工调岗信息
    List<EmpShiftBean> queryEmpShift(EmpShiftBean empShiftBean);

    //添加员工调岗信息
    void addEmpShift(EmpShiftBean empShiftBean) throws MessageException, Exception;
}
