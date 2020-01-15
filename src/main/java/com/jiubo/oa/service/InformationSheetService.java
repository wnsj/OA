package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.InformationSheetBean;

import java.util.List;

public interface InformationSheetService  extends IService<InformationSheetBean> {
    //查询忘记打卡
    public List<InformationSheetBean> queryInformationSheet(InformationSheetBean informationSheetBean) throws Exception;

    //添加忘记打卡
    public void addInformationSheet(InformationSheetBean informationSheetBean) throws Exception;

    //修改忘记打卡
    public void updateInformationSheet(InformationSheetBean informationSheetBean) throws Exception;

}
