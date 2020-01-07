package com.jiubo.oa.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @desc:倒休Bean
 * @date: 2020-01-07 15:23
 * @author: dx
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RestDownBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //倒休id
    private String rdId;

    //倒休人id
    private String empId;

    private String empName;

    //原因
    private String reaId;

    private String reaName;

    //状态
    private String state;

    //创建时间
    private String createDate;

    private String createBegDate;

    private String createEndDate;

    //加班开始时间
    private String overtimeBeg;

    //加班结束时间
    private String overtimeEnd;

    //休息开始时间
    private String restDateBeg;

    //休息结束时间
    private String restDateEnd;

    //备注
    private String remark;

    //审查人
    private String examiner;

    //审查人姓名
    private String examinerName;

    //审查人意见
    private String examinerAdv;

    //审查时间
    private String examinerDate;

    //审核人
    private String auditor;

    //审核人姓名
    private String auditorName;

    //审核人意见
    private String auditorAdv;

    //审核时间
    private String auditorDate;

    //批准人
    private String approver;

    //批准人姓名
    private String approverName;

    //批准人意见
    private String approverAdv;

    //批准时间
    private String approverDate;

    //部门id
    private String deptId;

    //部门名
    private String deptName;

    //岗位id
    private String posId;

    //岗位名
    private String posName;

    private String advState;
}
