package com.jiubo.oa.service.impl;

import com.jiubo.oa.bean.WxTokenBean;
import com.jiubo.oa.dao.CommonDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.CommonService;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @desc:公共服务实现类
 * @date: 2020-01-02 14:09
 * @author: dx
 * @version: 1.0
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonDao commonDao;

    @Override
    public Date queryDBTime() {
        return commonDao.queryDBTime();
    }

    @Override
    public WxTokenBean queryWxToken() {
        return commonDao.queryWxToken();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWxToken(WxTokenBean wxTokenBean) throws Exception {
        if (commonDao.updateWxToken(wxTokenBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWxToken(WxTokenBean wxTokenBean) throws Exception {
        if (commonDao.addWxToken(wxTokenBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWxToken(WxTokenBean wxTokenBean) throws Exception {
        if (commonDao.deleteWxToken(wxTokenBean) <= 0) throw new MessageException("操作失败!");
    }
}
