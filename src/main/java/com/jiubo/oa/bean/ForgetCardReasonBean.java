package com.jiubo.oa.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/* *
 * @desc:忘记打卡原因Bean
 * @author: dx
 * @date: 2020-01-10 15:15:14
 * @version: 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ForgetCardReasonBean implements Serializable {

  private static final long serialVersionUID = 1L;

  //忘记打卡原因id
  private String fcrId;

  //忘记打卡原因名
  private String fcrName;

  //是否使用
  private String isuse;

  //创建时间
  private String createDate;
}
