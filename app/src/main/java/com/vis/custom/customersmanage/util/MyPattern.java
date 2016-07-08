package com.vis.custom.customersmanage.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/7.
 */
public class MyPattern {

    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    public static boolean isCn(String cn) {
        Pattern p = Pattern
                .compile("[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(cn);

        return m.matches();
    }


    public static boolean isFax(String cn) {
        Pattern p = Pattern
                .compile("\\d{3}-\\d{8}|\\d{4}-\\d{7}");
        Matcher m = p.matcher(cn);

        return m.matches();
    }


    public static boolean isQq(String cn) {
        Pattern p = Pattern
                .compile("[1-9][0-9]{4,}");
        Matcher m = p.matcher(cn);

        return m.matches();
    }
}
