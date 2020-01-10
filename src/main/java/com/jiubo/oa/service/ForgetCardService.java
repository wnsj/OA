package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.ForgetCardBean;

import java.util.List;

/**
 * @desc:忘记打卡服务类
 * @date: 2020-01-10 15:27
 * @author: dx
 * @version: 1.0
 */
public interface ForgetCardService extends IService<ForgetCardBean> {

    //查询忘记打卡
    public List<ForgetCardBean> queryForgetCard(ForgetCardBean forgetCardBean) throws Exception;

    //添加忘记打卡
    public void addForgetCard(ForgetCardBean forgetCardBean) throws Exception;

    //修改忘记打卡
    public void updateForgetCard(ForgetCardBean forgetCardBean) throws Exception;

    //申请忘记打卡
    public void applyForgetCard(ForgetCardBean forgetCardBean) throws Exception;

    //取消，审核申请
    public void operationForgetCard(ForgetCardBean forgetCardBean) throws Exception;

}
