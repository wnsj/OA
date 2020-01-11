package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.NationBean;

import java.util.List;

public interface NationDao extends BaseMapper<NationBean> {

    //查询民族信息
    List<NationBean> queryNation();
}
