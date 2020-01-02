package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.LeaveTypeBean;
import com.jiubo.oa.dao.LeaveTypeDao;
import com.jiubo.oa.service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc:请假类型服务实现类
 * @date: 2020-01-02 16:25
 * @author: dx
 * @version: 1.0
 */
@Service
public class LeaveTypeServiceImpl extends ServiceImpl<LeaveTypeDao, LeaveTypeBean> implements LeaveTypeService {

    @Autowired
    private LeaveTypeDao leaveTypeDao;

    @Override
    public List<LeaveTypeBean> queryLeaveType(LeaveTypeBean leaveTypeBean) {
        return leaveTypeDao.queryLeaveType(leaveTypeBean);
    }
}
