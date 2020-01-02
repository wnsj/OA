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
    public List<AskLeaveBean> queryAskLeave(AskLeaveBean askLeaveBean);
}
