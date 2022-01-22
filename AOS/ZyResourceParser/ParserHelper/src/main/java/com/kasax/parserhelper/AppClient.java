package com.kasax.parserhelper;

import com.kasax.parserhelper.utils.ParserEngine;
import com.kasax.parserhelper.utils.ParserHelper;

public class AppClient {
    public static void main(String[] args) {
        ParserHelper parserHelper = new ParserHelper();
        String[] configs = parserHelper.loadAppConfig("F:\\zyprojectspace\\gitees\\SampleApps\\AOS\\ZyResourceParser\\ParserHelper\\src\\main\\resources\\appconfig.properties");
        ParserEngine parserEngine = new ParserEngine(configs);
        parserEngine.launch();
        parserEngine.release();
    }
}