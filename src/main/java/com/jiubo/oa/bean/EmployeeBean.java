package com.jiubo.oa.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @desc:员工Bean
 * @date: 2020-01-02 15:16
 * @author: dx
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EmployeeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //员工Id
    private String empId;

    //员工姓名
    private String empName;

    //性别
    private String sex;

    //身份证
    private String idCard;

    //生日
    private String birthday;

    //民族
    private String nationality;

    //政治面貌
    private String political;

    //户口性质
    private String regResidence;

    //籍贯
    private String nativePlace;

    //手机
    private String phone;

    //户籍地址
    private String permanentAddress;

    //现住址
    private String address;

    //紧急联系人
    private String emergencyContact;

    //紧急联系电话
    private String emergencyPhone;

    //合同属性
    private String contractAttribute;

    //合同期限
    private String contractPeriod;

    //续订1
    private String subscript1;

    //续订2
    private String subscript2;

    //续订3
    private String subscript3;

    //续订4
    private String subscript4;

    //账号
    private String accName;

    //密码
    private String accPwd;

    //部门
    private String deptId;

    //部门名
    private String deptName;

    //岗位
    private String posId;

    //岗位名
    private String posName;

    //入职时间
    private String entryDate;

    //转正时间
    private String formalDate;

    //离职时间
    private String leavelDate;

    //状态
    private String state;

    //微信id
    private String openId;

    //排除员工
    private String[] excludeEmp;

    //是否显示密码
    private String showPwd;
}
