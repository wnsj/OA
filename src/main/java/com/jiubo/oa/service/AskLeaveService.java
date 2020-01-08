package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.AskLeaveBean;

import java.util.List;

/**
 * @desc:请假服务类
 * @date: 2020-01-02 16:52
 * @author: dx
 * @version: 1.0
 */
public interface AskLeaveService extends IService<AskLeaveBean> {

    //查询请假
    public List<AskLeaveBean> queryAskLeave(AskLeaveBean askLeaveBean) throws Exception;

    //添加请假
    public void addAskLeave(AskLeaveBean askLeaveBean) throws Exception;

    //修改请假
    public void updateAskLeave(AskLeaveBean askLeaveBean) throws Exception;

    //申请请假
    public void applyLeave(AskLeaveBean askLeaveBean) throws Exception;

    //取消，审核申请
    public void operationLeave(AskLeaveBean askLeaveBean) throws Exception;

    //查询未处理的申请
    public List<AskLeaveBean> queryUntreatedAskLeave(AskLeaveBean askLeaveBean);

    //查询未处理的申请
    public List<AskLeaveBean> queryUntreatedApply(AskLeaveBean askLeaveBean) throws Exception;
}
