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

    //查询报销单
    @PostMapping(value = "/queryReimbursementAccount")
    public JSONObject queryReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        jsonObject.put(Constant.Result.RETDATA, accountService.queryReimbursementAccount(reimbursementAccountBean));
        return jsonObject;
    }

    //添加报销单
    @PostMapping(value = "/addReimbursementAccount")
    public JSONObject addReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean, MultipartFile[] file) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        System.out.println(reimbursementAccountBean);
        System.out.println("file:" + file.length);
        accountService.applyReimbursementAccount(reimbursementAccountBean, file);
        return jsonObject;
    }

    //取消、修改、审核报销单
    @PostMapping("/operationReimbursementAccount")
    public JSONObject operationReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean, MultipartFile[] file) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        accountService.operationReimbursementAccount(reimbursementAccountBean, file);
        return jsonObject;
    }
}
