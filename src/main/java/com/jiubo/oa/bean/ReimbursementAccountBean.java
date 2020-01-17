package com.jiubo.oa.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/* *
 * @desc:报销Bean
 * @author: dx
 * @date: 2020-01-15 10:18:12
 * @version: 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ReimbursementAccountBean implements Serializable {

  private static final long serialVersionUID = 1L;

  //报销id
  @TableId(value = "RA_ID", type = IdType.AUTO)
  private String raId;

  //申请人id
  private String empId;

  private String empName;

  private String createDate;

  private String createBegDate;

  private String createEndDate;

  private String raReason;

  private double money;

  private String state;

  private String examiner;

  private String examinerName;

  private String examinerAdv;

  private String examinerDate;

  private String auditor;

  private String auditorName;

  private String auditorAdv;

  private String auditorDate;

  private String approver;

  private String approverName;

  private String approverAdv;

  private String approverDate;

  private String examinerRemark;

  private String auditorRemark;

  private String approverRemark;

  //部门id
  private String deptId;

  //部门名
  private String deptName;

  //岗位id
  private String posId;

  //岗位名
  private String posName;

  private String advState;

  //凭证
  private List<ReimbursementCertificateBean> reimbursementCertificateBeans;

  //是否需要凭证
  private String needCertificate;

}
