package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.NationBean;
import com.jiubo.oa.dao.NationDao;
import com.jiubo.oa.service.NationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationServiceImpl extends ServiceImpl<NationDao, NationBean> implements NationService {

    @Autowired
    NationDao nationDao;

    @Override
    public List<NationBean> queryNation() {
        return nationDao.queryNation();
    }
}
