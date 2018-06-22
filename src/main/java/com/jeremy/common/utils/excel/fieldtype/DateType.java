package com.jeremy.common.utils.excel.fieldtype;

import com.jeremy.common.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

public class DateType {

    private static final String PATTERN = "yyyy-MM-dd";

    public static Object getValue(String val) throws ParseException {
        return DateUtils.parseDate(val, PATTERN);
    }

    public static String setValue(Object date) {
        return DateUtils.formatDate((Date) date, PATTERN);
    }
}
