package com.jiubo.oa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.ReimbursementAccountBean;
import com.jiubo.oa.dao.ReimbursementAccountDao;
import com.jiubo.oa.service.ReimbursementAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

/**
 * @desc:
 * @date: 2020-01-15 10:26
 * @author: dx
 * @version: 1.0
 */
@Service
public class ReimbursementAccountServiceImpl extends ServiceImpl<ReimbursementAccountDao, ReimbursementAccountBean> implements ReimbursementAccountService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReimbursementAccount() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request instanceof StandardMultipartHttpServletRequest) {
            MultiValueMap<String, MultipartFile> multiFileMap = ((StandardMultipartHttpServletRequest) request).getMultiFileMap();
            if (!multiFileMap.isEmpty()) {
                List<MultipartFile> fileList = multiFileMap.get("file");
                if (fileList != null) {
                    for (MultipartFile multipartFile : fileList) {
                        InputStream inputStream = multipartFile.getInputStream();
                        System.out.println("inputStream:" + inputStream);
                    }
                }
            }
        }
    }
}
