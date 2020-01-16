package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.ReimbursementCertificateBean;

import java.util.List;

/**
 * @desc:
 * @date: 2020-01-15 10:39
 * @author: dx
 * @version: 1.0
 */
public interface ReimbursementCertificateDao extends BaseMapper<ReimbursementCertificateBean> {

    //添加凭证
    public int addReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean);

    //查询凭证
    public List<ReimbursementCertificateBean> queryReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean);

    //删除凭证
    public int deleteReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean);

    //修改凭证
    public int updateReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean);
}
