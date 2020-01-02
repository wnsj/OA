package com.jiubo.oa.common;

/**
 * 公共常量类
 */
public class Constant {
    /*
     * 页面应答返回值
     * */
    public static interface Result {
        final public String RETCODE = "retCode";
        final public String RETMSG = "retMsg";
        final public String SUCCESS = "0000";
        final public String ERROR = "9999";
        final public String REPEAT = "1111";
        final public String SUCCESS_MSG = "成功！";
        final public String ERROR_MSG = "系统异常！";
        public static final String RETDATA = "retData";
        public static final String RETCOUNT = "count";
        public static final String CODE = "code";
    }

    /**
     * 是否有权限
     */
     public static interface RuleResult{
        final public String HASRULE = "0010";
        final public String NURULE = "0011";
        final public String HASRULE_MSG = "有权限";
        final public String NURULE_MSG = "您没有此权限，请联系管理员！！";
     }

    public interface Charset {
        final public static String UTF8 = "UTF-8";
        final public static String GBK = "GBK";
    }

    public static interface TASK_PARAM{
        final public String ID_1001 = "1001";
        final public String ID_1002 = "1002";
        final public String ID_1003 = "1003";
        final public String ID_1004 = "1004";
        final public String ID_1005 = "1005";
        final public String ID_1006 = "1006";
        final public String DEL_DAYS = "delDays";
        final public String STARTDATE = "startDate";
        final public String ENDDATE = "endDate";

    }
    /**
     * 文件参数
     */
    public static interface FILE_PARAM{
        //文件后缀
        final public String FILE_SUFFIX_DAT = ".dat";
        //压缩文件后缀
        final public String FILE_SUFFIX_GZ = ".gz";
        //点号
        final public String SPOT = ".";
        //空格
        final public String SPACE = " ";
        //空格
        final public String NULLSTR = "";
        //下划线
        final public String LINES = "_";
        //竖线
        final public String VLINES = "|";

        //逗号
        final public String TRANSLATE = ",";
        //星号
        final public String ASTERISK = "*";
        //文件解压目录
        final public String DIR_BAK = "bak";
        //斜线
        final public String VIRGULE = "/";
        //商品缓存(proId)key:PID
        final public String PID = "PID";
        //商品缓存(barcode)key:BAR
        final public String BAR = "BAR";
        //生成表格文件路径
        final public String EXCEL_FILE_PATH = "C:"+java.io.File.separatorChar+"cloudExcel"+java.io.File.separatorChar;
        final public String VENDOR_CAR_PATH = "C:"+java.io.File.separatorChar+"cloudPic"+java.io.File.separatorChar;
        final public String FILE_SUFFIX_PNG = "png";
    }





}
