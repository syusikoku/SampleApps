package com.kasax.parserhelper.utils;

import com.kasax.parserhelper.configs.AppConfigs;
import com.kasax.parserhelper.entry.StringBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Map;

/**
 * string.xml解析器,转换为pom需要的字段
 */
public class ParseSAX extends DefaultHandler {
    public static final String Q_STRING = "string";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_TRANSLATABLE = "translatable";
    private Map<String, StringBean> mStringsMap;
    private StringBean mCurrentBean;
    private String mLocale = null;
    private String mTargetPath = null;

    public ParseSAX(Map<String, StringBean> stringsMap, String targetPath, String locale) {
        mStringsMap = stringsMap;
        mTargetPath = targetPath;
        mLocale = locale;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.toLowerCase().equalsIgnoreCase(Q_STRING)) {
            String name = attributes.getValue(ATTR_NAME);
            // 有些string字段带有translatable属性，不需要翻译，此处我没有进行排除， 需要进行排除的可以自己判断下
            int index = attributes.getIndex(ATTR_TRANSLATABLE);
            System.out.println("startElement index: " + index);
            if (index != -1) {
                String value = attributes.getValue(index);
                System.out.println("startElement value: " + value);
            }
            System.out.println("qName: <" + qName + " name=" + name + ">");
            // 此处使用name + "_" + mTargetPath，是因为整个系统的string字段的name肯定有重复的，加上mTargetPath是为了保证唯一性
            StringBean bean = mStringsMap.get(name + "_" + mTargetPath);
            if (bean == null) {
                bean = new StringBean();
            }
            mCurrentBean = bean;
            bean.mName = name;
            bean.mTargetPath = mTargetPath;

        } else {
            System.out.println("qName: <" + qName + ">");
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("qName: <" + qName + ">");
        if (Q_STRING.equals(qName)) {
            mStringsMap.put(mCurrentBean.mName + "_" + mCurrentBean.mTargetPath, mCurrentBean);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        // TODO: 2022/1/16 需要其它语言支持，可以在这里添加 
        if (!"\r\n".equals(str) && mCurrentBean != null) {
            System.out.println(str);
            if (AppConfigs.LOCALE_EN.equalsIgnoreCase(mLocale)) {
                mCurrentBean.mEnContent += str;
            } else if (AppConfigs.LOCALE_ZH_RCN.equalsIgnoreCase(mLocale)) {
                mCurrentBean.mZhRCNContent += str;
            } else if (AppConfigs.LOCALE_ZH_RHK.equalsIgnoreCase(mLocale)
                    || AppConfigs.LOCALE_ZH_RTW.equalsIgnoreCase(mLocale)) {
                mCurrentBean.mZhRHKContent += str;
            } else if (AppConfigs.LOCALE_JA.equalsIgnoreCase(mLocale)) {
                mCurrentBean.mJaContent += str;
            }
        }
    }
}