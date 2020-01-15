package com.jiubo.oa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiubo.oa.bean.InformationSheetBean;

import java.util.List;

public interface InformationSheetDao  extends BaseMapper<InformationSheetBean> {
    //查询忘记打卡原因
    public List<InformationSheetBean> queryInformationSheet(InformationSheetBean informationSheetBean);
}
