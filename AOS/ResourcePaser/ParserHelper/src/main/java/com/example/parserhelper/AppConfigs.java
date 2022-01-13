package com.example.parserhelper;

public class AppConfigs {
    // 需要语言类型
    public final static String LOCALE_EN = "en";
    public final static String LOCALE_ZH_RCN = "zh-rcn";
    public final static String LOCALE_ZH_RHK = "zh-rhk";

    // 源路径
    public static final String SOURCE_PATH = "/Users/liviadmin/Desktop/Livi bank/GitLab/livi_main2/livi_android";
    // 输出文件路径
    public static final String OUTPUT_PATH = "/Users/liviadmin/AndroidStudioProjects/aos/ResourcePaser/ParserHelper/language.xls";

    // 支持的语言类型
    public static String[] SUPPORT_LOCATIONS = {
            LOCALE_EN,LOCALE_ZH_RCN,LOCALE_ZH_RHK
    };

    // 忽略文件列表
    public static String[] IGNORE_FILE_LIST = {"DexGuard-8.7.09"};
}
