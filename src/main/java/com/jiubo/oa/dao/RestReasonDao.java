package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.RestReasonBean;

import java.util.List;

/**
 * @desc:倒休原因Dao
 * @date: 2020-01-08 08:58
 * @author: dx
 * @version: 1.0
 */
public interface RestReasonDao extends BaseMapper<RestReasonBean> {

    //查询倒休原因
    public List<RestReasonBean> queryRestReason(RestReasonBean restReasonBean);
}
