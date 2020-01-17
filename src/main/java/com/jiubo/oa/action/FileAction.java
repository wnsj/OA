package com.jiubo.oa.action;

import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.FileService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc:
 * @date: 2020-01-16 11:28
 * @author: dx
 * @version: 1.0
 */
@Controller
@RequestMapping("/fileAction")
public class FileAction {

    @Autowired
    private FileService fileService;

    @RequestMapping("/getFile")
    public void getFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = request.getParameter("type");
        if (StringUtils.isBlank(type)) throw new MessageException("文件类型不能为空!");
        if ("IMG".equalsIgnoreCase(type)) {
            Map<String, Object> map = new HashMap<>();
            map.put("rcId", request.getParameter("rcId"));
            ServletOutputStream outputStream = response.getOutputStream();
            File file = fileService.getCertificateImg(map);
            response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = bis.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            if (bis != null) bis.close();
            if (fis != null) fis.close();
        }
    }
}
