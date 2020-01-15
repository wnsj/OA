package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.EmpShiftBean;

import java.util.List;

public interface EmpShiftDao extends BaseMapper<EmpShiftBean> {

    //添加调岗信息
    int addEmpShift(EmpShiftBean empShiftBean);

    //查询员工调岗信息
    List<EmpShiftBean> queryEmpShift(EmpShiftBean empShiftBean);

}
