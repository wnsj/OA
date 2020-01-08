package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.RestReasonBean;

import java.util.List;

/**
 * @desc:倒休原因服务类
 * @date: 2020-01-08 08:56
 * @author: dx
 * @version: 1.0
 */
public interface RestReasonService extends IService<RestReasonBean> {

    //查询倒休原因
    public List<RestReasonBean> queryRestReason(RestReasonBean restReasonBean);
}
