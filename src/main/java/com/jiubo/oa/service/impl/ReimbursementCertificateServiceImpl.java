package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.ReimbursementCertificateBean;
import com.jiubo.oa.dao.ReimbursementCertificateDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.ReimbursementCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @desc:
 * @date: 2020-01-15 10:37
 * @author: dx
 * @version: 1.0
 */
@Service
public class ReimbursementCertificateServiceImpl extends ServiceImpl<ReimbursementCertificateDao, ReimbursementCertificateBean> implements ReimbursementCertificateService {

//    @Value("${certificateDir}")
//    private String certificateDir;
    //String path = certificateDir.endsWith("/") ? certificateDir.concat() : certificateDir.concat("/");

    @Autowired
    private ReimbursementCertificateDao reimbursementCertificateDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean) {
        reimbursementCertificateDao.addReimbursementCertificate(reimbursementCertificateBean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean) throws Exception {
        List<ReimbursementCertificateBean> reimbursementCertificateBeans = queryReimbursementCertificate(reimbursementCertificateBean);
        if (reimbursementCertificateBeans.isEmpty()) throw new MessageException("参数错误!");

        //删除数据库数据
        for (ReimbursementCertificateBean reimbursement : reimbursementCertificateBeans) {
            reimbursementCertificateDao.deleteReimbursementCertificate(new ReimbursementCertificateBean().setRcId(reimbursement.getRcId()));
            File f = new File(reimbursement.getImg());
            if (f.exists()) f.delete();
        }

        //删除文件
        for (ReimbursementCertificateBean reimbursement : reimbursementCertificateBeans) {
            File f = new File(reimbursement.getImg());
            if (f.exists()) f.delete();
        }

    }

    @Override
    public List<ReimbursementCertificateBean> queryReimbursementCertificate(ReimbursementCertificateBean reimbursementCertificateBean) throws Exception {
        return reimbursementCertificateDao.queryReimbursementCertificate(reimbursementCertificateBean);
    }
}
