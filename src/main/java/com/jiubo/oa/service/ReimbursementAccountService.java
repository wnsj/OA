package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.ReimbursementAccountBean;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-15 10:25
 * @author: dx
 * @version: 1.0
 */
public interface ReimbursementAccountService extends IService<ReimbursementAccountBean> {

    //添加报销单
    public void addReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean) throws Exception;

    //修改报销单
    public void updateReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean) throws Exception;

    //查询报销单
    public List<ReimbursementAccountBean> queryReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean)throws Exception;

    //添加申请
    public void applyReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean) throws Exception;

    //取消，审核申请
    public void operationReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean) throws Exception;
}
