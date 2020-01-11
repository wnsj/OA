package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.NationBean;

import java.util.List;

public interface NationService extends IService<NationBean>  {
    //查询民族信息
    List<NationBean> queryNation();
}
