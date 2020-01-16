package com.jiubo.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiubo.oa.bean.InformationSheetBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InformationSheetService  extends IService<InformationSheetBean> {
    //查询信息单
    public List<InformationSheetBean> queryInformationSheet(InformationSheetBean informationSheetBean) throws Exception;

    //添加信息单
    public void addInformationSheet(InformationSheetBean informationSheetBean) throws Exception;

    //修改信息单
    public void updateInformationSheet(InformationSheetBean informationSheetBean) throws Exception;

    public void operationInformationSheet(InformationSheetBean informationSheetBean) throws Exception;

    public void applyInformationSheet(InformationSheetBean informationSheetBean) throws Exception;
}
