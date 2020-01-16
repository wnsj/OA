package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.InformationSheetBean;

import java.util.List;

public interface InformationSheetDao  extends BaseMapper<InformationSheetBean> {
    //查询信息单
    public List<InformationSheetBean> queryInformationSheet(InformationSheetBean informationSheetBean);

    //添加信息单
    public int addInformationSheet(InformationSheetBean informationSheetBean);

    //修改信息单
    public int updateInformationSheet(InformationSheetBean informationSheetBean);
}
