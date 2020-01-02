package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.AskLeaveBean;
import com.jiubo.oa.dao.AskLeaveDao;
import com.jiubo.oa.service.AskLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc:请假服务实现类
 * @date: 2020-01-02 16:55
 * @author: dx
 * @version: 1.0
 */
@Service
public class AskLeaveServiceImpl extends ServiceImpl<AskLeaveDao,AskLeaveBean> implements AskLeaveService {

    @Autowired
    private AskLeaveDao askLeaveDao;

    @Override
    public List<AskLeaveBean> queryAskLeave(AskLeaveBean askLeaveBean) {
        return null;
    }
}
