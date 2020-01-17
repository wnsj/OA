package com.jiubo.oa.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @desc:忘记打卡Bean
 * @date: 2020-01-10 14:52
 * @author: dx
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ForgetCardBean implements Serializable {

    //忘记打卡id
    private String fcId;

    //员工id
    private String empId;

    private String empName;

    //忘记打卡原因
    private String fcrId;

    private String fcrName;

    //未打卡时间
    private String forgetDate;

    private String forgetDateBeg;

    private String forgetDateEnd;

    //备注
    private String remark;

    //证明人
    private String witness;

    private String witnessName;

    //证明人备注
    private String witnessRemark;

    //审查人
    private String examiner;

    //审查人姓名
    private String examinerName;

   //审查人意见
    private String examinerAdv;

    //审查人备注
    private String examinerRemark;

   //审查人审核时间
    private String examinerDate;

    //审核人
    private String auditor;

    //审核人姓名
    private String auditorName;

    //审核人意见
    private String auditorAdv;

    //审核人备注
    private String auditorRemark;

    //审核人审核时间
    private String auditorDate;

    //批准人
    private String approver;

    //批准人姓名
    private String approverName;

    //批准人意见
    private String approverAdv;

    //批准人备注
    private String approverRemark;

    //批准人审核时间
    private String approverDate;

    //申请时间
    private String createDate;

    private String createBegDate;

    private String createEndDate;

    //部门
    private String deptId;

    private String deptName;

    private String deptLevel;

    //岗位
    private String posId;

    private String posName;

    private String posLeval;

    private String state;
}
