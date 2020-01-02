package com.jiubo.oa.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @desc:部门
 * @date: 2020-01-02 14:48
 * @author: dx
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DepartmentBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //部门Id
    private String deptId;

    //部门名
    private String deptName;

    //上级部门
    private String parentId;

    //部门等级
    private String deptLevel ;

    //是否在用
    private String isuse;
}
