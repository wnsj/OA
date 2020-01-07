package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.RestDownBean;

import java.util.List;

/**
 * @desc:倒休dao
 * @date: 2020-01-07 15:36
 * @author: dx
 * @version: 1.0
 */
public interface RestDownDao extends BaseMapper<RestDownBean> {

    //查询倒休
    public List<RestDownBean> queryRestDown(RestDownBean restDownBean);

    //添加倒休
    public int addRestDown(RestDownBean restDownBean);

    //修改倒休
    public int updateRestDown(RestDownBean restDownBean);

    //查询未处理的倒休申请
    public List<RestDownBean> queryUntreatedRestDown(RestDownBean restDownBean);
}
