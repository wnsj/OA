package com.jiubo.oa.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @desc:倒休原因Bean
 * @date: 2020-01-08 08:51
 * @author: dx
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RestReasonBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //原因id
    private String reaId;

    //原因名
    private String reaName;

    //是否使用
    private String isuse;

    //创建时间
    private String createDate;
}
