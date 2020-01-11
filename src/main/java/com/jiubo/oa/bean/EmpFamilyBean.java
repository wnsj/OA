package com.jiubo.oa.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 员工家庭信息表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EmpFamilyBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String famiId;

    //员工id
    private String empId;

    //员工姓名
    private String empName;

    //亲属姓名
    private String name;

    //性别
    private String sex;

    //出生日期
    private String birthday;

    //关系
    private String relation;

    //手机号
    private String phone;

}
