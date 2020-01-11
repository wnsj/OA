package com.jiubo.oa.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 员工岗位调动信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EmpShiftBean implements Serializable {

    private static final long serialVersionUID = 1L;

    //主键
    private String shiftId;

    //员工id
    private String empId;

    //员工姓名
    private String empName;

    //调动前岗位ID
    private String beforePosId;

    //调动前岗位名
    private String beforePosName;

    //调动后岗位ID
    private String afterPosId;

    //调动后岗位名
    private String afterPosName;

    //调动前部门
    private String beforeDeptId;

    //调动前部门名
    private String beforeDeptName;

    //调动后部门
    private String afterDeptId;

    //调动后部门
    private String afterDeptName;

    //调岗类型 1：换岗 2：晋升 3：降职
    private String type;

    //调动时间
    private String createTime;

    //账户名
    private String accName;

}
