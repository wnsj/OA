package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.RestDownBean;
import net.sf.jsqlparser.statement.execute.Execute;

import java.util.List;

/**
 * @desc:倒休服务类
 * @date: 2020-01-07 15:34
 * @author: dx
 * @version: 1.0
 */
public interface RestDownService extends IService<RestDownBean> {

    //查询倒休
    public List<RestDownBean> queryRestDown(RestDownBean restDownBean) throws Exception;

    //添加倒休
    public void addRestDown(RestDownBean restDownBean) throws Exception;

    //申请倒休
    public void applyRestDown(RestDownBean restDownBean) throws Exception;

    //修改倒休
    public void updateRestDown(RestDownBean restDownBean) throws Exception;

    //取消，修改，审核倒休
    public void operationRestDown(RestDownBean restDownBean) throws Exception;

    //查询未处理的倒休申请
    public List<RestDownBean> queryUntreatedRestDown(RestDownBean restDownBean) throws Exception;
}
