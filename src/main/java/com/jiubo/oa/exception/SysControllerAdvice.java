package com.jiubo.oa.exception;

import com.alibaba.fastjson.JSONObject;
import com.jiubo.oa.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @desc:action层异常处理
 * @date: 2019-07-31 15:02
 * @author: dx
 * @version: 1.0
 */
@ControllerAdvice  //不指定包默认加了@Controller和@RestController都能控制
//@ControllerAdvice(basePackages ="com.example.demo.controller")
public class SysControllerAdvice {

    private Logger log = LoggerFactory.getLogger(SysControllerAdvice.class);

    /* *
     * @desc:全局异常处理，反正异常返回统一格式的JSONObject
     * @author: dx
     * @date: 2019-07-31 15:08:42
     * @param ex :
     * @return: com.alibaba.fastjson.JSONObject
     * @throws:
     * @version: 1.0
     **/
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JSONObject exceptionHandler(Exception e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.ERROR);
        jsonObject.put(Constant.Result.RETMSG, Constant.Result.ERROR_MSG);
        log.error(Constant.Result.RETMSG, e);
        return jsonObject;
    }

    /* *
     * @desc:自定义异常处理
     * @author: dx
     * @date: 2019-07-31 15:11:10
     * @param e :
     * @return: com.alibaba.fastjson.JSONObject
     * @throws:
     * @version: 1.0
     **/
    @ResponseBody
    @ExceptionHandler(value = MessageException.class)
    public JSONObject messageExceptionHandler(MessageException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.Result.RETCODE, Constant.Result.ERROR);
        jsonObject.put(Constant.Result.RETMSG, e.getMessage());
        return jsonObject;
    }
}
