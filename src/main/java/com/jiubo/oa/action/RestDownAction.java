package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.RestDownBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.RestDownService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc:倒休接口
 * @date: 2020-01-07 15:33
 * @author: dx
 * @version: 1.0
 */
@RestController
@RequestMapping("/restDownAction")
public class RestDownAction {

    @Autowired
    private RestDownService restDownService;

    //查询倒休
    @PostMapping(value = "/queryRestDown")
    public JSONObject queryRestDown(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        RestDownBean restDownBean = JSONObject.parseObject(params, RestDownBean.class);
        jsonObject.put(Constant.Result.RETDATA, restDownService.queryRestDown(restDownBean));
        return jsonObject;
    }

    //添加倒休
    @PostMapping(value = "/applyRestDown")
    public JSONObject applyRestDown(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        RestDownBean restDownBean = JSONObject.parseObject(params, RestDownBean.class);
        restDownService.applyRestDown(restDownBean);
        return jsonObject;
    }

    //取消，修改，审核倒休
    @PostMapping(value = "/operationRestDown")
    public JSONObject operationRestDown(@RequestBody String params) throws Exception {
        if (StringUtils.isBlank(params)) throw new MessageException("参数接收失败!");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        RestDownBean restDownBean = JSONObject.parseObject(params, RestDownBean.class);
        restDownService.operationRestDown(restDownBean);
        return jsonObject;
    }
}
