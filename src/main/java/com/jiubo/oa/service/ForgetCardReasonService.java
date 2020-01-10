package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.ForgetCardReasonBean;

import java.util.List;

/**
 * @desc:忘记打卡原因服务类
 * @date: 2020-01-10 15:16
 * @author: dx
 * @version: 1.0
 */
public interface ForgetCardReasonService extends IService<ForgetCardReasonBean> {

    //查询忘记打卡原因
    public List<ForgetCardReasonBean> queryForgetCardReason(ForgetCardReasonBean forgetCardReasonBean);
}
