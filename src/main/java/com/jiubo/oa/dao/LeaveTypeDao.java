package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.LeaveTypeBean;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-02 16:26
 * @author: dx
 * @version: 1.0
 */
public interface LeaveTypeDao extends BaseMapper<LeaveTypeBean> {

    //查询请假类型
    public List<LeaveTypeBean> queryLeaveType(LeaveTypeBean leaveTypeBean);
}
