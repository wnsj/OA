package com.jiubo.oa.action;


import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.InformationSheetBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.InformationSheetService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/informationSheetAction")
public class InformationSheetAction {

    @Autowired
    private InformationSheetService informationSheetService;

    //查询信息单
    @PostMapping(value = "/queryInformationSheet")
    public JSONObject queryInformationSheet(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        InformationSheetBean informationSheetBean = JSONObject.parseObject(params, InformationSheetBean.class);
        jsonObject.put(Constant.Result.RETDATA, informationSheetService.queryInformationSheet(informationSheetBean));
        return jsonObject;
    }

    //添加信息单
    @PostMapping(value = "/applyInformationSheet")
    public JSONObject applyInformationSheet(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        InformationSheetBean informationSheetBean = JSONObject.parseObject(params, InformationSheetBean.class);
        informationSheetService.applyInformationSheet(informationSheetBean);
        return jsonObject;
    }

    //信息单的操作
    @PostMapping(value = "/operationInformationSheet")
    public JSONObject operationInformationSheet(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        InformationSheetBean informationSheetBean = JSONObject.parseObject(params, InformationSheetBean.class);
        informationSheetService.operationInformationSheet(informationSheetBean);
        return jsonObject;
    }
}