package com.jiubo.oa.service;

import java.io.File;
import java.util.Map;

/**
 * @desc:
 * @date: 2020-01-16 12:57
 * @author: dx
 * @version: 1.0
 */
public interface FileService {

    File getCertificateImg(Map<String,Object> map) throws Exception;
}
