package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.RestDownBean;
import com.jiubo.oa.dao.RestDownDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.RestDownService;
import com.jiubo.oa.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @desc:倒休服务实现类
 * @date: 2020-01-07 15:34
 * @author: dx
 * @version: 1.0
 */
@Service
public class RestDownServiceImpl extends ServiceImpl<RestDownDao, RestDownBean> implements RestDownService {

    @Autowired
    private RestDownDao restDownDao;

    @Override
    public List<RestDownBean> queryRestDown(RestDownBean restDownBean) throws Exception {
        return restDownDao.queryRestDown(restDownBean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRestDown(RestDownBean restDownBean) throws Exception {
        restDownBean.setState("0");
        restDownBean.setExaminerAdv("0");
        restDownBean.setAuditorAdv("0");
        restDownBean.setApproverAdv("0");
        restDownBean.setCreateDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime()));
        if (restDownDao.addRestDown(restDownBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyRestDown(RestDownBean restDownBean) throws Exception {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRestDown(RestDownBean restDownBean) throws Exception {
        if (restDownDao.updateRestDown(restDownBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationRestDown(RestDownBean restDownBean) throws Exception {

    }

    @Override
    public List<RestDownBean> queryUntreatedRestDown(RestDownBean restDownBean) throws Exception {
        return restDownDao.queryUntreatedRestDown(restDownBean);
    }
}
