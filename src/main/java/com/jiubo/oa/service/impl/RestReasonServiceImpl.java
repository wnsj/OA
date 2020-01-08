package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.RestReasonBean;
import com.jiubo.oa.dao.RestReasonDao;
import com.jiubo.oa.service.RestReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc:倒休原因服务实现类
 * @date: 2020-01-08 08:57
 * @author: dx
 * @version: 1.0
 */
@Service
public class RestReasonServiceImpl extends ServiceImpl<RestReasonDao, RestReasonBean> implements RestReasonService {

    @Autowired
    private RestReasonDao restReasonDao;

    @Override
    public List<RestReasonBean> queryRestReason(RestReasonBean restReasonBean) {
        return restReasonDao.queryRestReason(restReasonBean);
    }
}
