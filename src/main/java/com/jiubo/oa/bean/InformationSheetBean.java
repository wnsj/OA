package com.jiubo.oa.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InformationSheetBean {

    //信息单ID
    private String infId;

    //信息单类型
    private String sheefType;

    //重要性级别
    private String degreeTYPE;

    //发起人ID
    private String senderId;

    //发起人名字
    private String senderName;

    //发起负责人ID
    private String empSendId;

    //发起负责人名字
    private String empSendName;

    //发起人部门ID
    private String deptSendId;

    //发起人部门名字
    private String deptSendName;

    //发起时间
    private String sendDate;

    //接收部门ID
    private String deptRecId;

    //接收部门名字
    private String deptRecName;

    //接收负责人ID
    private String empRecId;

    //接收人名字
    private String empRecName;

    //抄送人ID
    private String empId;

    //抄送人名字
    private String empName;

    //主题
    private String theme;

    //内容
    private String content;

    //要求或者请求
    private String requireContent;

    //发起人部门负责人是否同意
    private String sendAgree;

    //发起人是否同意内容
    private String sendContent;

    //发起人同意时间
    private String sendAppDate;

    //接收负责人是否同意
    private String recAgree;

    //接收人意见
    private String recContent;

    //接收时间
    private String recAppDate;

    //处理状态
    private String state;



}
