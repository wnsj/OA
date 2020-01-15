package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.ReimbursementCertificateBean;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-15 10:36
 * @author: dx
 * @version: 1.0
 */
public interface ReimbursementCertificateService extends IService<ReimbursementCertificateBean> {

    //添加凭证
    public void addReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean);

    //删除凭证
    public void deleteReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean) throws Exception;

    //查询凭证
    public List<ReimbursementCertificateBean> queryReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean) throws Exception;
}
