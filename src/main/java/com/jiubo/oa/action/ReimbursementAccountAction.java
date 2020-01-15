package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.service.ReimbursementAccountService;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * @desc:
 * @date: 2020-01-15 11:49
 * @author: dx
 * @version: 1.0
 */
@RestController
@RequestMapping("/ReimbursementAccountAction")
public class ReimbursementAccountAction {

    @Autowired
    private ReimbursementAccountService accountService;

    @PostMapping("/addReimbursementAccount")
    public JSONObject addReimbursementAccount(HttpServletRequest request) throws Exception {
        //1、验证是否为文件上传
        if (ServletFileUpload.isMultipartContent(request)) {
            //2、创建工厂类
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //3、设置临时文件目录
            factory.setRepository(new File(""));
            //4、创建文件上传类对象
            ServletFileUpload sfu = new ServletFileUpload(factory);
            List<FileItem> list = sfu.parseRequest(new ServletRequestContext(request));
            for (FileItem fileItem: list){
                System.out.println(fileItem.getFieldName());
                fileItem.write(new File("c:/".concat(fileItem.getFieldName())));
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);

        //accountService.addReimbursementAccount();
        return jsonObject;
    }
}
