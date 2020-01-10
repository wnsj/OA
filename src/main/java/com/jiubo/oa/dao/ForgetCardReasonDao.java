package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.ForgetCardReasonBean;

import java.util.List;

/**
 * @desc:忘记打卡原因Dao
 * @date: 2020-01-10 15:18
 * @author: dx
 * @version: 1.0
 */
public interface ForgetCardReasonDao extends BaseMapper<ForgetCardReasonBean> {

    //查询忘记打卡原因
    public List<ForgetCardReasonBean> queryForgetCardReason(ForgetCardReasonBean forgetCardReasonBean);
}
