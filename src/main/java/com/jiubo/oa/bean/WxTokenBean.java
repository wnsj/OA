package com.jiubo.oa.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc:
 * @date: 2019-12-23 16:50
 * @author: dx
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WxTokenBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tkId;

    private String token;

    private Date createDate;
}
