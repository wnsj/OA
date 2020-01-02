package com.jiubo.oa.service;

import com.jiubo.oa.bean.WxTokenBean;

import java.util.Date;

/**
 * @desc:公共服务类
 * @date: 2020-01-02 14:08
 * @author: dx
 * @version: 1.0
 */
public interface CommonService {

    //查询数据时间
    public Date queryDBTime();

    //查询微信Token
    public WxTokenBean queryWxToken();

    //修改微信Token
    public void updateWxToken(WxTokenBean wxTokenBean) throws Exception;

    //添加微信Token
    public void addWxToken(WxTokenBean wxTokenBean) throws Exception;

    //删除微信Token
    public void deleteWxToken(WxTokenBean wxTokenBean) throws Exception;
}
