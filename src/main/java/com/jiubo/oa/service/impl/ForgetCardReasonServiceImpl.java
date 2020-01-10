package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.ForgetCardReasonBean;
import com.jiubo.oa.dao.ForgetCardReasonDao;
import com.jiubo.oa.service.ForgetCardReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc:忘记打卡原因服务实现类
 * @date: 2020-01-10 15:17
 * @author: dx
 * @version: 1.0
 */
@Service
public class ForgetCardReasonServiceImpl extends ServiceImpl<ForgetCardReasonDao, ForgetCardReasonBean> implements ForgetCardReasonService {

    @Autowired
    private ForgetCardReasonDao forgetCardReasonDao;

    @Override
    public List<ForgetCardReasonBean> queryForgetCardReason(ForgetCardReasonBean forgetCardReasonBean) {
        return forgetCardReasonDao.queryForgetCardReason(forgetCardReasonBean);
    }
}
