package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.ForgetCardBean;

import java.util.List;

/**
 * @desc:忘记打卡Dao
 * @date: 2020-01-10 15:31
 * @author: dx
 * @version: 1.0
 */
public interface ForgetCardDao extends BaseMapper<ForgetCardBean> {

    //查询忘记打卡
    public List<ForgetCardBean> queryForgetCard(ForgetCardBean forgetCardBean);

    //添加忘记打卡
    public int addForgetCard(ForgetCardBean forgetCardBean);

    //修改忘记打卡
    public int updateForgetCard(ForgetCardBean forgetCardBean);
}
