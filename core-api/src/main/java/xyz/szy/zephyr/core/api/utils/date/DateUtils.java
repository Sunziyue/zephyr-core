package xyz.szy.zephyr.core.api.utils.date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;

import java.util.Date;

public class DateUtils extends DateUtil {

    public static final FastDateFormat SIMPLE_YEAR_DATE_FORMAT = FastDateFormat.getInstance("yyMMdd");
    public static final FastDateFormat FULL_YEAR_DATE_FORMAT = FastDateFormat.getInstance("yyyyMMdd");
    public static final FastDateFormat ALL_UNIT_DATE_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");

    public static String allUnitDate() {
        return ALL_UNIT_DATE_FORMAT.format(date());
    }

    public static String allUnitDate(Date date) {
        return ALL_UNIT_DATE_FORMAT.format(date);
    }

    public static String processDate() {
        return SIMPLE_YEAR_DATE_FORMAT.format(date());
    }

    public static String processDate(Date date) {
        return SIMPLE_YEAR_DATE_FORMAT.format(date);
    }

    public static Integer saleYmd() {
        return Integer.parseInt(FULL_YEAR_DATE_FORMAT.format(date()));
    }

    public static Integer saleYmd(Date date) {
        return Integer.parseInt(FULL_YEAR_DATE_FORMAT.format(date));
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.allUnitDate());
        System.out.println(DateUtils.processDate());
        System.out.println(DateUtils.saleYmd());
        System.out.println(Integer.MIN_VALUE);
    }
}
