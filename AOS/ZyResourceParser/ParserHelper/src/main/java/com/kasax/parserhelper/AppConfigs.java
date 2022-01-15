package com.kasax.parserhelper;

import java.util.Arrays;
import java.util.List;

public class AppConfigs {
    // 需要语言类型
    public final static String LOCALE_EN = "en";
    //    public final static String LOCALE_ZH_RCN = "zh-rcn";
//    public final static String LOCALE_ZH_RHK = "zh-rhk";
    public final static String LOCALE_ZH_RCN = "zh-rcn";
    public final static String LOCALE_ZH_RHK = "zh-rtw";
    public final static String LOCALE_JA = "ja";

    // 忽略文件列表
    public static List<String> IGNORE_FILE_LIST = Arrays.asList("gradle", ".github", ".gradle", ".idea", "build", "debug");

    // 需要忽略的资源目录
    public static List<String> IGNORE_RES_LIST = Arrays.asList("values-land", "values-sw", "values-w");
}
