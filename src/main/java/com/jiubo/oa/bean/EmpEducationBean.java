package com.jiubo.oa.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 学历信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EmpEducationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //ID
    private String educatId;

    //员工ID
    private String empId;

    //员工名
    private String empName;

    //学历
    private String eduId;

    //学历名称
    private String eduName;

    //毕业时间
    private String graduateDate;

    //毕业院校
    private String school;

    //专业
    private String professional;

    //职称
    private String profession;

    //资格证
    private String certification;

    //账户名
    private String accName;
}
