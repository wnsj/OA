package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.EmpFamilyBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmpFamilyDao extends BaseMapper<EmpFamilyBean> {

    //添加员工家庭信息
    int addEmpFamilyList(@Param("empFamilyList") List<EmpFamilyBean> empFamilyList);

    //批量更新员工家庭信息
    int updateByBeanList(@Param("empFamilyList") List<EmpFamilyBean> empFamilyList);

    //查询员工家庭信息
    List<EmpFamilyBean> queryEmpFamily(EmpFamilyBean empFamilyBean);

    //修改员工家庭信息
    int updateEmpFamily(EmpFamilyBean empFamilyBean);
}
