package com.jiubo.oa.dao;

import com.jiubo.oa.bean.WxTokenBean;
import org.apache.ibatis.annotations.*;

import java.util.Date;

/**
 * @desc:
 * @date: 2020-01-02 14:11
 * @author: dx
 * @version: 1.0
 */
public interface CommonDao {

    //获取数据库时间
    @Select("SELECT NOW()")
    public Date queryDBTime();

    @Insert("INSERT INTO WX_TOKEN (TOKEN, CREATE_DATE) VALUES (#{token}, NOW())")
    @Options(useGeneratedKeys = true,keyProperty = "tkId")
    public int addWxToken(WxTokenBean wxTokenBean);

    //查询Token
    @Select("SELECT TK_ID, TOKEN, CREATE_DATE FROM WX_TOKEN")
    public WxTokenBean queryWxToken();

    //修改Token
    @Update("UPDATE WX_TOKEN SET TOKEN = #{token},CREATE_DATE = #{createDate} WHERE TK_ID = #{tkId}")
    public int updateWxToken(WxTokenBean wxTokenBean);

    //删除微信Token
    @Delete("DELETE FROM WX_TOKEN")
    public int deleteWxToken(WxTokenBean wxTokenBean);
}
