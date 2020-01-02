package com.jiubo.oa.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @desc:
 * @date: 2020-01-02 16:22
 * @author: dx
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LeaveTypeBean {

    //请假类型ID
    private String ltId;

    //请假类型名
    private String ltName;

    //是否启用
    private String isuse;

    //创建时间
    private String createDate;
}
