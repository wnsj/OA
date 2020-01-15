package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.ReimbursementAccountBean;

/**
 * @desc:
 * @date: 2020-01-15 10:25
 * @author: dx
 * @version: 1.0
 */
public interface ReimbursementAccountService extends IService<ReimbursementAccountBean> {

    public void addReimbursementAccount() throws Exception;
}
