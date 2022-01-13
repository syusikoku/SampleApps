package com.example.parserhelper;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class AppClient {

    public static void main(String[] args) {
        // 存放所有strings.xml文件的目录
      //  String rootPath = "/Users/liviadmin/Desktop/Livi bank/GitLab/livi_main2/livi_android";
        AppClient main = new AppClient();
        main.launch(AppConfigs.SOURCE_PATH);
    }

    private void launch(String rootPath) {

        File file = new File(rootPath);

        if (!file.exists() || !file.isDirectory()) {
            return;
        }

        // 所有的字段及语言翻译
        List<StringBean> allData = new ArrayList<>();
        Map<String, StringBean> stringsMap = new HashMap<>();
        // 目标路径，记录每个字段所在的文件
        String targetPath = null;

        // 所有的res目录的路径
        List<String> resFilePaths = new ArrayList<String>();
        loadResFilePath(rootPath, resFilePaths);
        System.out.println("launch: resFilePaths: " + resFilePaths);

        // 遍历所有的res目录
        for (String resFilePath : resFilePaths) {

            // res目录
            File resFile = new File(resFilePath);
            String resFileName = resFile.getName();
            System.out
                    .println("start resFilePath: " + resFilePath + ", resFileName: " + resFileName);

            // 所有values目录
            File[] valuesListFiles = resFile.listFiles();
            for (File valuesFile : valuesListFiles) {
                String valuesFilePath = valuesFile.getAbsolutePath();
                String valueFileName = valuesFile.getName();
                System.out.println("start valuesFilePath: " + valuesFilePath + ", valueFileName: "
                        + valueFileName);

                if(valueFileName.equalsIgnoreCase("values-zh-rCN")
                        || valueFileName.equalsIgnoreCase("values-zh-rHK")
                || valueFileName.equalsIgnoreCase("values")){
                    // values目录对应的语言
                    String locale = getLocale(valueFileName);
                    if (locale == null) {
                        return;
                    }

                    // strings.xml和arrays.xml文件
                    File[] stringListFiles = valuesFile.listFiles();
                    for (File stringFile : stringListFiles) {
                        String stringFilePath = stringFile.getAbsolutePath();
                        String stringFileName = stringFile.getName();
                        System.out.println(
                                "start stringFilePath: " + stringFilePath + ", stringFileName: "
                                        + stringFileName);
                        if (stringFileName.toLowerCase().contains("string")) {
                            // string.xml文件
                            if (locale.equalsIgnoreCase(AppConfigs.LOCALE_EN)) {
                                targetPath = stringFilePath;
                            }

                            // stringFilePath: 是strings.xml文件的路径, 现在开始解析xml文件
                            getLocaleStrings(stringFilePath, targetPath, locale, stringsMap);
                        } else {
                            // arrays.xml文件， 目前没有进行处理
                        }
                    }
                }
                // strings end
            } // values end
        } // res end

        // 获取到的所有string字段
        allData.addAll(stringsMap.values());

        System.out.println("allData:");
        System.out.println(allData.toString());

        // 添加到队列, 生成Excel文件, 使用到poi包
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("languages");
        sheet.setDefaultColumnWidth(32);
        Row titleRow = sheet.createRow(0);
        // name, en, ca , es, eu, gl, pt, path
        Cell nameCell = titleRow.createCell(0);
        nameCell.setCellValue("name");
        Cell enCell = titleRow.createCell(1);
        enCell.setCellValue("en");
        Cell zhRCNCell = titleRow.createCell(2);
        zhRCNCell.setCellValue("zh-rCN");
        Cell zhRHK = titleRow.createCell(3);
        zhRHK.setCellValue("zh-rHK");

        for (int i = 0; i < allData.size(); i++) {
            int index = i + 1;

            titleRow = sheet.createRow(index);

            StringBean bean = allData.get(i);
            nameCell = titleRow.createCell(0);
            nameCell.setCellValue(bean.mName);
            enCell = titleRow.createCell(1);
            enCell.setCellValue(bean.mEnContent);

            zhRCNCell = titleRow.createCell(2);
            zhRCNCell.setCellValue(bean.mZhRCNContent);
            zhRHK = titleRow.createCell(3);
            zhRHK.setCellValue(bean.mZhRHKContent);

        }

        // 删除空行
        sheet = PIOUtils.getAccuracyContextNum(sheet);


        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(AppConfigs.OUTPUT_PATH);
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                outputStream = null;
            }

            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                workbook = null;
            }
        }

    }


    private void loadResFilePath(String filePath, List<String> resFilePaths) {
        File rootFile = new File(filePath);
        File[] rootListFiles = rootFile.listFiles();
        for (File rootListFile : rootListFiles) {
            boolean isDirectory = rootListFile.isDirectory();
            String name = rootListFile.getName();
            System.out.println("getResFilePath: isDirectory: " + isDirectory + ", name: " + name);

            boolean hasIgnore = false;

            // 忽略指定目录
            for (String s : AppConfigs.IGNORE_FILE_LIST) {
                if(name.contains(s)){
                    hasIgnore = true;
                    break;
                }
            }

            if(hasIgnore){
                continue;
            }

            if (isDirectory) {
                if (name.toLowerCase().equals("res")) {
                    File[] listFiles = rootListFile.listFiles();
                    System.out.println("getResFilePath listFiles: " + listFiles.length);
                    if (listFiles.length > 0) {
                        System.out.println("getResFilePath listFiles[0].getName().toLowerCase(): "
                                + listFiles[0].getName().toLowerCase());
                        boolean contains = listFiles[0].getName().toLowerCase().contains("values");
                        System.out.println("getResFilePath contains: " + contains);
                        if (contains) {
                            resFilePaths.add(rootListFile.getAbsolutePath());
                        } else {
                            loadResFilePath(rootListFile.getAbsolutePath(), resFilePaths);
                        }
                    }
                } else {
                    loadResFilePath(rootListFile.getAbsolutePath(), resFilePaths);
                }
            }
        }
    }

    private void getLocaleStrings(String stringFilePath, String targetPath, String locale,
                                  Map<String, StringBean> stringsMap) {
        System.out.println("getLocaleStrings stringFilePath: " + stringFilePath + ", targetPath: "
                + targetPath);

        // 创建一个SAX解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 创建一个SAX转换工具
        try {
            SAXParser saxParser = factory.newSAXParser();
            // 解析XML
            saxParser.parse(new File(stringFilePath), new ParseSAX(stringsMap, targetPath, locale));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getLocale(String fileName) {
        String locale = null;
        String tmpFileName = fileName.toLowerCase();
        if (tmpFileName.equals("values")) {
            locale = AppConfigs.LOCALE_EN;
        }else {
            String fileNameStr = tmpFileName.replace("values", "");
            if (fileNameStr.contains(AppConfigs.LOCALE_ZH_RCN)) {
                locale = AppConfigs.LOCALE_ZH_RCN;
            }else if (fileNameStr.contains(AppConfigs.LOCALE_ZH_RHK)) {
                locale = AppConfigs.LOCALE_ZH_RHK;
            }
        }
        return locale;
    }

    public static class ParseSAX extends DefaultHandler {
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
                System.out.println("startElement index: "+index);
                if(index != -1) {
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
            if (!"\r\n".equals(str) && mCurrentBean != null) {
                System.out.println(str);
                if (AppConfigs.LOCALE_EN.equals(mLocale)) {
                    mCurrentBean.mEnContent += str;
                }else if (AppConfigs.LOCALE_ZH_RCN.equals(mLocale)) {
                    mCurrentBean.mZhRCNContent += str;
                }else if (AppConfigs.LOCALE_ZH_RHK.equals(mLocale)) {
                    mCurrentBean.mZhRHKContent += str;
                }
            }
        }
    }
}


