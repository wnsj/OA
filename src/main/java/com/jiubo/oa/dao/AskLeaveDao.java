package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.AskLeaveBean;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-02 16:56
 * @author: dx
 * @version: 1.0
 */
public interface AskLeaveDao extends BaseMapper<AskLeaveBean> {

    //查询请假
    public List<AskLeaveBean> queryAskLeave(AskLeaveBean askLeaveBean);

    //添加请假
    public int addAskLeave(AskLeaveBean askLeaveBean);

    //修改请假
    public int updateAskLeave(AskLeaveBean askLeaveBean);

    //查询未处理的申请
    public List<AskLeaveBean> queryUntreatedAskLeave(AskLeaveBean askLeaveBean);

    //查询未处理的申请
    public List<AskLeaveBean> queryUntreatedApply(AskLeaveBean askLeaveBean);
}