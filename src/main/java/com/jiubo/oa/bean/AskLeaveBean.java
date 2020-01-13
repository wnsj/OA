package com.jiubo.oa.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @desc:
 * @date: 2020-01-02 16:40
 * @author: dx
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AskLeaveBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //请假ID
    private String alId;

    //请假类型
    private String ltId;

    private String ltName;

    //申请人
    private String empId;

    private String empName;

    //申请时间
    private String createDate;

    private String createBegDate;

    private String createEndDate;

    //开始时间
    private String begDate;

    private String qBegDate;

    //结束时间
    private String endDate;

    private String qEndDate;

    //说明
    private String remark;

    //是否有单据
    private String bill;

    //状态
    private String state;

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

    //处理状态【0:全部 ，1：处理中，2：已通过，3：未通过】
    private String advState;

    private String deptId;

    private String deptName;

    //倒休id
    private String rdId;

    //忘记打卡id
    private String fcId;
}
