package com.jiubo.oa.service.impl;

import com.jiubo.oa.bean.ReimbursementCertificateBean;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.FileService;
import com.jiubo.oa.service.ReimbursementCertificateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @date: 2020-01-16 12:58
 * @author: dx
 * @version: 1.0
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private ReimbursementCertificateService certificateService;

    @Override
    public File getCertificateImg(Map<String, Object> map) throws Exception {
        File file = null;
        if (map.get("rcId") != null && StringUtils.isNotBlank(String.valueOf(map.get("rcId")))) {
            List<ReimbursementCertificateBean> reimbursementCertificateBeans = certificateService.queryReimbursementCertificate(new ReimbursementCertificateBean().setRcId(String.valueOf(map.get("rcId"))));
            if (reimbursementCertificateBeans.isEmpty()) throw new MessageException("未查询到该文件信息!");
            ReimbursementCertificateBean reimbursementCertificateBean = reimbursementCertificateBeans.get(0);
            file = new File(reimbursementCertificateBean.getImg());
            if (!file.exists())throw new MessageException("文件不存在!");
        }
        return file;
    }
}
