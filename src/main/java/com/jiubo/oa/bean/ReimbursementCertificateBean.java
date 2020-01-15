package com.jiubo.oa.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ReimbursementCertificateBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String rcId;

    private String raId;

    private String img;

}
