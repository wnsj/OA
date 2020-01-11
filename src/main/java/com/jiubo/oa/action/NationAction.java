package com.jiubo.oa.action;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.bean.NationBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.NationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc:民族接口
 * @date: 2020-01-02 16:15
 * @author: dx
 * @version: 1.0
 */
@RestController
@RequestMapping("/nationAction")
public class NationAction {

    @Autowired
    NationService nationService;

    @PostMapping(value = "/queryNation")
    public JSONObject queryNation() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.SUCCESS);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.SUCCESS_MSG);
        jsonObject.put(Constant.Result.RETDATA, nationService.queryNation());
        return jsonObject;
    }
}
