package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.LeaveTypeBean;

import java.util.List;

/**
 * @desc:请假类型服务类
 * @date: 2020-01-02 16:25
 * @author: dx
 * @version: 1.0
 */
public interface LeaveTypeService extends IService<LeaveTypeBean> {

    //查询请假类型
    public List<LeaveTypeBean> queryLeaveType(LeaveTypeBean leaveTypeBean);
}
