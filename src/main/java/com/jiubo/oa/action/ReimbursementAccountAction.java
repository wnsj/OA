package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.ReimbursementAccountBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.service.ReimbursementAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    //添加报销单
    @PostMapping(value = "/addReimbursementAccount")
    public JSONObject addReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean, MultipartFile[] file) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        System.out.println(reimbursementAccountBean);
        System.out.println("file:" + file.length);
        //System.out.println(reimbursementAccountBean.getFile()[0].getOriginalFilename());
//        for (MultipartFile mf : files) {
//            if (!mf.isEmpty()) {
//                System.out.println(mf.getOriginalFilename());
//                InputStream is = mf.getInputStream();
//                int len = 0;
//                byte[] by = new byte[1024];
//                OutputStream os = new FileOutputStream("c:/" + mf.getOriginalFilename());
//                BufferedInputStream bis = new BufferedInputStream(is);
//                BufferedOutputStream bos = new BufferedOutputStream(os);
//                while ((len = bis.read(by)) != -1) {
//                    bos.write(by, 0, len);
//                    bos.flush();
//                }
//            }
//        }

        accountService.applyReimbursementAccount(reimbursementAccountBean,file);
        return jsonObject;
    }

    //取消、修改、审核报销单
    @PostMapping("/operationReimbursementAccount")
    public JSONObject operationReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean, MultipartFile[] file) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        accountService.operationReimbursementAccount(reimbursementAccountBean,file);
        return jsonObject;
    }
}
