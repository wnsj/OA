package com.jiubo.oa.util;

import com.jiubo.oa.common.InstantContextAfterProcessor;
import com.jiubo.oa.service.CommonService;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeUtil {

    public final static String _NULLSTR = "";
    public final static String YYYY = "yyyy";
    public final static String MM = "MM";
    public final static String DD = "DD";
    public final static String YYYYMM = "yyyyMM";
    public final static String YYYY_MM = "yyyy-MM";
    public final static String YYYY$MM = "yyyy/MM";
    public final static String YYYYMMDD = "yyyyMMdd";
    public final static String YYYY$MM$DD = "yyyy/MM/dd";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    public final static String YYYY_M_D = "yyyy-M-d";
    public final static String YYYY$M$D = "yyyy/M/d";
    public final static String YYYY_M_DD = "yyyy-M-dd";
    public final static String YYYY$M$DD = "yyyy/M/dd";
    public final static String YYYY_MM_D = "yyyy-MM-d";
    public final static String YYYY$MM$D = "yyyy/MM/d";
    public final static String MM_DD = "MM月dd日";
    //星期一:Monday（Mon.）
    public final static String MONDAY = "星期一";
    public final static String WEEK_MON = "周一";
    //星期二：Tuesday（Tues.）
    public final static String TUESDAY = "星期二";
    public final static String WEEK_TUES = "周二";
    //星期三：Wednesday（Wed.）
    public final static String WEDNESDAY = "星期三";
    public final static String WEEK_WEDNES = "周三";
    //星期四：Thursday（Thur./Thurs.）
    public final static String THURSDAY = "星期四";
    public final static String WEEK_THURS = "周四";
    //星期五：Friday（Fri.）
    public final static String FRIDAY = "星期五";
    public final static String WEEK_FRI = "周五";
    //星期六：Saturday（Sat.）
    public final static String SATURDAY = "星期六";
    public final static String WEEK_SAT = "周六";
    //星期日：Sunday（Sun.）
    public final static String SUNDAY = "星期日";
    public final static String WEEK_SUN = "周日";

    private static SimpleDateFormat sdf_YYYY = new SimpleDateFormat(YYYY);
    private static SimpleDateFormat sdf_MM = new SimpleDateFormat(MM);
    private static SimpleDateFormat sdf_YYYYMM = new SimpleDateFormat(YYYYMM);
    private static SimpleDateFormat sdf_DD = new SimpleDateFormat(DD);
    private static SimpleDateFormat sdf_MM_DD = new SimpleDateFormat(MM_DD);
    private static SimpleDateFormat sdf_YYYYMMDD = new SimpleDateFormat(YYYYMMDD);
    private static SimpleDateFormat sdf_YYYY$M$D = new SimpleDateFormat(YYYY$M$D);
    private static SimpleDateFormat sdf_YYYY_M_D = new SimpleDateFormat(YYYY_M_D);
    private static SimpleDateFormat sdf_YYYY$M$DD = new SimpleDateFormat(YYYY$M$DD);
    private static SimpleDateFormat sdf_YYYY_M_DD = new SimpleDateFormat(YYYY_M_DD);
    private static SimpleDateFormat sdf_YYYY$MM$D = new SimpleDateFormat(YYYY$MM$D);
    private static SimpleDateFormat sdf_YYYY_MM_D = new SimpleDateFormat(YYYY_MM_D);
    private static SimpleDateFormat sdf_YYYY$MM$DD = new SimpleDateFormat(YYYY$MM$DD);
    private static SimpleDateFormat sdf_YYYY_MM_DD = new SimpleDateFormat(YYYY_MM_DD);
    private static SimpleDateFormat sdf_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat sdf_YYYYMMDDHHMMSSSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static SimpleDateFormat sdf_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat sdf_YYMMDDHHMMSS = new SimpleDateFormat("yyMMddHHmmss");
    private static SimpleDateFormat sdf_YYMMDD = new SimpleDateFormat("yyMMdd");
    private static SimpleDateFormat sdf_YYYY_MM_DD_HH_MM_SS_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static SimpleDateFormat sdf_YYYY$MM$DD$HH$MM$SS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static SimpleDateFormat sdf_YYYY$MM$DD$HH$MM$SS$SSS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    private static SimpleDateFormat sdf_YYYYMM01 = new SimpleDateFormat("yyyyMM01");
    private static SimpleDateFormat sdf_HH_MM_SS = new SimpleDateFormat("HH:mm:ss");

    private static SimpleDateFormat sdf_EEEE = new SimpleDateFormat("EEEE");
    private static SimpleDateFormat sdf_YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat sdf_YYYY_MM = new SimpleDateFormat(YYYY_MM);
    private static SimpleDateFormat sdf_YYYY$MM = new SimpleDateFormat(YYYY$MM);
    

    //静态日历对象
    private static Calendar calendar = Calendar.getInstance();

    //日期单位
    public final static int UNIT_YEAR = Calendar.YEAR;
    public final static int UNIT_MONTH = Calendar.MONTH;
    public final static int UNIT_DAY = Calendar.DATE;
    public final static int UNIT_HOUR = Calendar.HOUR;
    public final static int UNIT_MINUTE = Calendar.MINUTE;
    public final static int UNIT_SECOND = Calendar.SECOND;
    //24进制小时
    public final static int UNIT_HOUR_OF_DAY = Calendar.HOUR_OF_DAY;
    
    
    /**
     * 转换带T日期格式
     */
    public synchronized static String convertDateT(String strDate) {
    	String str = "";
    	try {
    		strDate = strDate.replace("T", " ");
			Date date = sdf_YYYY_MM_DD_HH_MM.parse(strDate);
			str = sdf_YYYY_MM_DD_HH_MM.format(date);
			return str;
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return str;
    }
    
    /**
     * 返回日期增减
     */
    public synchronized static int DateDiffDays(Date begDate, Date endDate) {

        //得到两个日期相差的天数
        Date date1 = null;
        Date date2 = null;

        int days = 0;

        //容错处理
        if (begDate == null || endDate == null) return days;

        try {
            date1 = parseDateYYYYMMDD(getDateYYYYMMDD(begDate));
            date2 = parseDateYYYYMMDD(getDateYYYYMMDD(endDate));
            days = (int) ((date2.getTime() - date1.getTime()) / (24 * 3600 * 1000));
        } catch (ParseException e) {
            //内部异常，概率极低，不处理
        }

        return days;

    }


    /**
     * 返回日期增减
     */

    public static synchronized Date dateAdd(Date targetDate, int unit, int num) {
        if (targetDate == null) return targetDate;
        calendar.setTime(targetDate);
        calendar.add(unit, num);
        return calendar.getTime();
    }

    /*
     * 获取年月字符串
     * */
    public static synchronized String getYearMonthStr(Date date) throws ParseException {

        return date == null ? _NULLSTR : sdf_YYYYMM.format(date);

    }

    /*
     * 获取月字符串
     * */
    public static synchronized String getMonthStr(Date date) throws ParseException {

        return date == null ? _NULLSTR : sdf_MM.format(date);

    }


    /*
     * 获取年月字符串
     * */
    public static synchronized String getDayStr(Date date) throws ParseException {
        if (date == null) return _NULLSTR;
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static synchronized String getHourStr(Date date) throws ParseException {
        if (date == null) return _NULLSTR;
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
    }

    /**
     * 返回月日字符串
     *
     * @param date
     * @param concat，concat1，concat2 月日字符串格式,如5.1 or 5-1 or 5月1日
     * @return
     * @throws Exception
     */
    public static synchronized String getMMDDStr(Date date, String concat) throws Exception {
        if (date == null) return _NULLSTR;
        calendar.setTime(date);
        concat = concat == null ? _NULLSTR : concat;
        return (calendar.get(Calendar.MONTH) + 1) + concat + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static synchronized String getMMDDString(Date date, String concat1, String concat2) throws Exception {
        if (date == null) return _NULLSTR;
        calendar.setTime(date);
        concat1 = concat1 == null ? _NULLSTR : concat1;
        concat2 = concat2 == null ? _NULLSTR : concat2;
        return (calendar.get(Calendar.MONTH) + 1) + concat1 + calendar.get(Calendar.DAY_OF_MONTH) + concat2;
    }

    /*
     * 获取年字符串
     * */
    public static synchronized String getYearStr(Date date) throws ParseException {

        return date == null ? _NULLSTR : sdf_YYYY.format(date);

    }


    /*
     * 获取季度
     * */
    public static synchronized int getQuarter(Date date) throws ParseException {
        int season = 0;
        if (date == null) return season;
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;

    }

    /*
     * 获取月的第一天
     * */
    public static synchronized Date getFirstDayOfMonth(Date date) throws ParseException {

        return date == null ? null : sdf_YYYYMMDD.parse(sdf_YYYYMM01.format(date));

    }

    /*
     * 获取月的最后一天,注意时间是0点
     * */
    public static synchronized Date getLastDayOfMonth(Date date) throws ParseException {
        if (date == null) return null;
        Date time = dateAdd(getFirstDayOfMonth(dateAdd(date, UNIT_MONTH, 1)), UNIT_DAY, -1);
        return sdf_YYYYMMDD.parse(sdf_YYYYMMDD.format(time));
    }


    public static synchronized Date parseDateYYYYMMDDHHMMSSSSS(String date) throws ParseException {

        Date time = (date == null ? null : sdf_YYYYMMDDHHMMSSSSS.parse(date));

        return time;

    }


    public static synchronized String getDateYYYYMMDDHHMMSSSSS(Date date) {

        return date == null ? _NULLSTR : sdf_YYYYMMDDHHMMSSSSS.format(date);

    }

    public static synchronized Date parseDateYYYY$MM$DD$HH$MM$SS$SSS(String date) throws ParseException {

        Date time = (date == null ? null : sdf_YYYY$MM$DD$HH$MM$SS$SSS.parse(date));

        return time;

    }


    public static synchronized String getDateYYYY$MM$DD$HH$MM$SS$SSS(Date date) {

        return date == null ? _NULLSTR : sdf_YYYY$MM$DD$HH$MM$SS$SSS.format(date);

    }

    public static synchronized Date parseDateYYYY$MM$DD$HH$MM$SS(String date) throws ParseException {

        Date time = (date == null ? null : sdf_YYYY$MM$DD$HH$MM$SS.parse(date));

        return time;

    }

    public static synchronized String getDateYYYY$MM$DD$HH$MM$SS(Date date) {

        return date == null ? _NULLSTR : sdf_YYYY$MM$DD$HH$MM$SS.format(date);

    }

    public static synchronized Date parseDateYYYYMMDDHHMMSS(String date) throws ParseException {

        Date time = (date == null ? null : sdf_YYYYMMDDHHMMSS.parse(date));

        return time;

    }


    public static synchronized String getDateYYYYMMDDHHMMSS(Date date) {

        return date == null ? _NULLSTR : sdf_YYYYMMDDHHMMSS.format(date);

    }


    public static synchronized String getDateYYMMDDHHMMSS(Date date) {

        return date == null ? _NULLSTR : sdf_YYMMDDHHMMSS.format(date);

    }

    public static synchronized String getDateYYMMDD(Date date) {

        return date == null ? _NULLSTR : sdf_YYMMDD.format(date);

    }

    /*
     *
     * yyyy-MM-dd HH:mm:ss.SSS
     * */
    public static synchronized Date parseDateYYYY_MM_DD_HH_MM_SS_SSS(String date) throws ParseException {

        Date time = (date == null ? null : sdf_YYYY_MM_DD_HH_MM_SS_SSS.parse(date));

        return time;

    }

    /*
     * yyyyMMdd
     * */
    public static synchronized String getDateYYYY_MM_DD_HH_MM_SS_SSS(Date date) {

        return date == null ? _NULLSTR : sdf_YYYY_MM_DD_HH_MM_SS_SSS.format(date);

    }

    /**
     * 字符串日期转化带时间的字符串日期   YYYY-MM-DD->YYYY-MM-DD HH:MM:SS.SSS
     *
     * @param dateTime
     * @return 返回值类型  Date
     * @throws ParseException
     * @author 作者 mwl
     * @date 时间 2019年6月7日下午2:05:40
     */
    public static synchronized String YYYYMMDD_SHIFT_YYYYMMDDHHMMSSSSS(String dateTime) throws ParseException {

        Date time = parseAnyDate(dateTime);

        return getDateYYYY_MM_DD_HH_MM_SS_SSS(time);

    }

    /*
     *
     * yyyyMMdd
     * */
    public static synchronized Date parseDateYYYYMMDD(String date) throws ParseException {

        Date time = (date == null ? null : sdf_YYYYMMDD.parse(date));

        return time;

    }

    /*
     * yyyy-MM-dd HH:mm:ss.SSS
     * */
    public static synchronized String getDateYYYYMMDD(Date date) {

        return date == null ? _NULLSTR : sdf_YYYYMMDD.format(date);

    }


    /*
     *
     * yyyy/MM/dd
     * */
    public static synchronized Date parseDateYYYY$MM$DD(String date) throws ParseException {

        Date time = (date == null ? null : sdf_YYYY$MM$DD.parse(date));

        return time;

    }

    /*
     * yyyy/MM/dd
     * */
    public static synchronized String getDateYYYY$MM$DD(Date date) {

        return date == null ? _NULLSTR : sdf_YYYY$MM$DD.format(date);

    }

    /*
     *
     * yyyy/MM/dd
     * */
    public static synchronized Date parseDateYYYY_MM_DD(String date) throws ParseException {

        Date time = (date == null ? null : sdf_YYYY_MM_DD.parse(date));

        return time;

    }

    /*
     * yyyy/MM/dd
     * */
    public static synchronized String getDateYYYY_MM_DD(Date date) {

        return date == null ? _NULLSTR : sdf_YYYY_MM_DD.format(date);

    }


    public static synchronized String getDateMM_DD(Date date) {

        return date == null ? _NULLSTR : sdf_MM_DD.format(date);

    }

    /*
     *
     * yyyy/MM/dd
     * */
    public static synchronized Date parseAnyDate(String date) throws ParseException {

        Date time = null;

        if (StringUtils.isBlank(date)) return null;
        if (date.length() == 6) {
            time = sdf_YYYYMM.parse(date);
        } else if (date.length() == 7) {
            try {
                time = sdf_YYYY_MM.parse(date);
            } catch (Exception e) {
                time = sdf_YYYY$MM.parse(date);
            }
        } else if (date.length() == 8) {
            try {
                time = sdf_YYYY_M_D.parse(date);
            } catch (Exception e1) {
                try {
                    time = sdf_YYYY$M$D.parse(date);
                } catch (Exception e2) {
                    time = sdf_YYYYMMDD.parse(date);
                }
            }
        } else if (date.length() == 9) {
            try {
                time = sdf_YYYY_M_DD.parse(date);
            } catch (Exception e1) {
                try {
                    time = sdf_YYYY$M$DD.parse(date);
                } catch (Exception e2) {
                    try {
                        time = sdf_YYYY_MM_D.parse(date);
                    } catch (Exception e3) {
                        time = sdf_YYYY$M$D.parse(date);
                    }
                }
            }
        } else if (date.length() <= 10) {
            try {
                time = sdf_YYYY_MM_DD.parse(date);
            } catch (Exception e1) {
                try {
                    time = sdf_YYYY$MM$DD.parse(date);
                } catch (Exception e2) {
                    time = sdf_YYYYMMDD.parse(date);
                }
            }
        } else if (date.length() == 14) {
            time = sdf_YYYYMMDDHHMMSS.parse(date);
        } else if (date.length() == 17) {
            time = sdf_YYYYMMDDHHMMSSSSS.parse(date);
        } else if (date.length() == 19) {
            try {
                time = sdf_YYYY$MM$DD$HH$MM$SS.parse(date);
            } catch (Exception e1) {
                time = sdf_YYYY_MM_DD_HH_MM_SS.parse(date);
            }
        } else if (date.length() > 19) {
            try {
                time = sdf_YYYY_MM_DD_HH_MM_SS_SSS.parse(date);
            } catch (Exception e1) {
                time = sdf_YYYY$MM$DD$HH$MM$SS$SSS.parse(date);
            }
        } else {
            throw new RuntimeException("不支持的时间格式！");
        }

        return time;

    }

    public static synchronized Date parseDateYYYY_MM_DD_HH_MM_SS(String date) throws ParseException {

        Date time = (date == null ? null : sdf_YYYY_MM_DD_HH_MM_SS.parse(date));

        return time;

    }


    /*
     * yyyyMMdd
     * */
    public static synchronized String getDateYYYY_MM_DD_HH_MM_SS(Date date) {

        return date == null ? _NULLSTR : sdf_YYYY_MM_DD_HH_MM_SS.format(date);

    }

    /**
     * 获取到时间字符串中的时间 HH:MM:SS
     *
     * @param date
     * @return 返回值类型  String
     * @author 作者 mwl
     * @date 时间 2019年5月11日下午2:40:38
     */
    public static synchronized String getDateHH_MM_SS(Date date) {

        return date == null ? _NULLSTR : sdf_HH_MM_SS.format(date);

    }


    /**
     * 返回当前时间指定天数后的时间 可为负数
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getNextDay(Date date, int day) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +day);
        date = calendar.getTime();
        return date;
    }


    /*
     * 显示时间值：如耗时多少小时多少分钟...
     * */
    public static String getTimeCHStr(long timeMillis) {

        StringBuffer timeStr = new StringBuffer();

        if (timeMillis >= 1000 * 60 * 60 * 24) {
            timeStr.append(timeMillis / (1000 * 60 * 60 * 24)).append("天")
                    .append(TimeUtil.getTimeCHStr(timeMillis - (timeMillis / (1000 * 60 * 60 * 24)) * 1000 * 60 * 60 * 24));
        } else if (timeMillis >= 1000 * 60 * 60) {
            timeStr.append(timeMillis / (1000 * 60 * 60)).append("小时")
                    .append(TimeUtil.getTimeCHStr(timeMillis - (timeMillis / (1000 * 60 * 60)) * 1000 * 60 * 60));
        } else if (timeMillis >= 1000 * 60) {
            timeStr.append(timeMillis / (1000 * 60)).append("分钟")
                    .append(TimeUtil.getTimeCHStr(timeMillis - (timeMillis / (1000 * 60)) * 1000 * 60));
        } else if (timeMillis >= 1000) {
            timeStr.append(timeMillis / 1000).append("秒")
                    .append(TimeUtil.getTimeCHStr(timeMillis - (timeMillis / 1000) * 1000));
        } else if (timeMillis >= 0) {
            timeStr.append(timeMillis).append("毫秒");
        } else {
            timeStr.append(timeMillis).append("错误！");
        }

        return timeStr.toString();

    }

    //字符串转date
    public static Date StrToDate(String str, String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date, String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        String str = format.format(date);
        return str;
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        if (date1 == null || date2 == null) return 0;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) //闰年
                {
                    timeDistance += 366;
                } else //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else //不同年
        {
            return day2 - day1;
        }
    }

    /**
     * 计算2个时间相差的月数
     *
     * @param date1
     * @param date2
     * @param pattern 时间格式
     * @param endDate 是否包含结束日期
     * @return
     * @throws Exception
     */
    public static int countMonths(String date1, String date2, String pattern, boolean endDate) throws Exception {
        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));

        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

        //开始日期若小月结束日期
        if (year < 0) {
            year = -year;
            count = year * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        } else {
            count = year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        }
        return endDate ? count + 1 : count;
    }

    public static int countYears(String date1, String date2, String pattern, boolean endDate) throws Exception {
        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));
        count = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        return endDate ? count + 1 : count;
    }

    //查询指定日期是否为周末
    public static boolean isWeekend(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        } else return false;
    }

    //验证字符串是否为合法的日期
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }


    public static void sleepSeconds(long seconds) {

        try {
            Thread.currentThread().sleep(1000 * seconds);
        } catch (InterruptedException e) {

        }

    }


    /**
     * @desc:日期是（星期几，周几）
     * @param:date（时间）
     * @param：type【返回类型：1或null返回星期几，其他返回周几】
     * @return: String
     * @Create at: 2019-05-06
     * @author: dx
     * @version: 1.0
     */
    public synchronized static String getWeekOfDate(Date date, String type) {
        String[] weekDays = null;
        if (StringUtils.isBlank(type) || "1".equals(type)) {
            weekDays = new String[]{SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY};
        } else {
            weekDays = new String[]{WEEK_SUN, WEEK_MON, WEEK_TUES, WEEK_WEDNES, WEEK_THURS, WEEK_FRI, WEEK_SAT};
        }
        calendar.setTime(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];
    }

    /**
     * @desc:日期是星期几
     * @param:
     * @return: String
     * @Create at: 2019-05-06
     * @author: dx
     * @version: 1.0
     */
    public synchronized static String getWeekOfDate(Date date) {
        return sdf_EEEE.format(date);
    }


    /**
     * @desc:判断2个时间是否同年，同月或同时（注:判断月时不比较年，时与月类似）
     * @param:date1:时间1
     * @param:date2：时间2
     * @param:flag（1判断是否同年，2判断是否同月，3判断是否同时）
     * @return: boolean
     * @Create at: 2019-05-07
     * @author: dx
     * @version: 1.0
     */
    public synchronized static boolean dateEqualsDate(Date date1, Date date2, int flag) {
        if (date1 == null || date2 == null) return false;
        calendar.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        //判断2个时间是否同年
        if (flag == 1)
            return calendar.get(UNIT_YEAR) == calendar2.get(UNIT_YEAR);
            //判断2个时间是否同月
        else if (flag == 2)
            return calendar.get(UNIT_MONTH) == calendar2.get(UNIT_MONTH);
            //判断2个时间是否同时
        else
            return calendar.get(UNIT_HOUR) == calendar2.get(UNIT_HOUR);
    }

    /**
     * @desc:计算相差多少小时
     * @param:
     * @return: int
     * @Create at: 2019-05-06
     * @author: dx
     * @version: 1.0
     */
    public synchronized static double DateDiffHours(Date begDate, Date endDate) {
        //得到两个日期相差的小时数
        double days = 0;
        //容错处理
        if (begDate == null || endDate == null) return days;

        long nd = 1000 * 24 * 60 * 60;//每天毫秒数

        long nh = 1000 * 60 * 60;//每小时毫秒数

        long nm = 1000 * 60;//每分钟毫秒数

        long diff = endDate.getTime() - begDate.getTime(); // 获得两个时间的毫秒时间差异

        long day = diff / nd;   // 计算差多少天

        double hour = diff % nd / nh; // 计算差多少小时

        double min = diff % nd % nh / nm;  // 计算差多少分钟

        double d = day > 0 ? day * 24 : 0;

        double m = min > 0 ? (min / 60) : 0;

        return (double) Math.round((d + hour + m) * 10) / 10;
    }

    /**
     * @desc:获取时间小时制（例8：30为8.5）
     * @param:
     * @return: double
     * @Create at: 2019-05-07
     * @author: dx
     * @version: 1.0
     */
    public static synchronized double getHourHex(Date date) {
        if (date == null) return 0;
        calendar.setTime(date);
        double h = calendar.get(UNIT_HOUR_OF_DAY);
        double m = calendar.get(UNIT_MINUTE);
        return (double) Math.round((h + (m > 0 ? m / 60 : 0)) * 10) / 10;
    }

    /**
     * @desc:获取年月日时分（例：2018-01-01 08:30:00 -> 2018-01-01 08:30）
     * @param:
     * @return: String
     * @Create at: 2019-05-08
     * @author: dx
     * @version: 1.0
     */
    public static synchronized String getDateYYYY_MM_DD_HH_MM(Date date) {
        return date == null ? _NULLSTR : sdf_YYYY_MM_DD_HH_MM.format(date);
    }

    //获取数据库时间
    public static synchronized Date getDBTime(){
        CommonService commonService = InstantContextAfterProcessor.getService("commonServiceImpl", CommonService.class);
        return commonService.queryDBTime();
    }

    //获得本周一的日期
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    //获得下周一的日期
    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    /**
     * 日期转换 Date -> String
     *
     * @param date Date类型信息
     * @return 字符串时间
     * @throws Exception 抛出异常
     */
    public static String parseDate2String(Date date) throws Exception {
        if (date == null) {
            return null;
        }
        return parseDate2String(date, "yyyy-MM-dd");
    }

    /**
     * 日期转换 Date -> String
     *
     * @param date    Date类型信息
     * @param pattern 格式模板
     * @return 字符串时间
     * @throws Exception 抛出异常
     */
    public static String parseDate2String(Date date, String pattern) throws Exception {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String strDate = sdf.format(date);
        return strDate;
    }


    /**
     * 日期转换-  String -> Date
     *
     * @param dateString 字符串时间
     * @return Date类型信息
     * @throws Exception 抛出异常
     */
    public static Date parseString2Date(String dateString) throws Exception {
        if (dateString == null) {
            return null;
        }
        return parseString2Date(dateString, "yyyy-MM-dd");
    }

    /**
     * 日期转换-  String -> Date
     *
     * @param dateString 字符串时间
     * @param pattern    格式模板
     * @return Date类型信息
     * @throws Exception 抛出异常
     */
    public static Date parseString2Date(String dateString, String pattern) throws Exception {
        if (dateString == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = sdf.parse(dateString);
        return date;
    }

}
