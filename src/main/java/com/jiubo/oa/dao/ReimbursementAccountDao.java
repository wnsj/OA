package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.ReimbursementAccountBean;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-15 10:27
 * @author: dx
 * @version: 1.0
 */
public interface ReimbursementAccountDao extends BaseMapper<ReimbursementAccountBean> {

    //添加报销
    public int addReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean);

    //查询报销
    public List<ReimbursementAccountBean> queryReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean);

    //修改报销
    public int updateReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean);
}
