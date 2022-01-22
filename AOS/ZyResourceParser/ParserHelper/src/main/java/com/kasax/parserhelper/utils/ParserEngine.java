package com.kasax.parserhelper.utils;


import com.kasax.parserhelper.configs.AppConfigs;
import com.kasax.parserhelper.entry.StringBean;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 解析引擎
 */
public class ParserEngine {
    private String sourceDirPath, outputPath;
    private List<String> supportLanguageList;
    private List<String> ignoreFileList;
    private Map<String, String> defLanuageList;

    public ParserEngine(String[] configs) {
        sourceDirPath = configs[0];
        outputPath = configs[1];
        String supportLanguages = configs[2];
        supportLanguageList = Arrays.asList(supportLanguages.split(","));
        String ignoreFiles = configs[3];
        ignoreFileList = Arrays.asList(ignoreFiles.split(","));
        defLanuageList = new LinkedHashMap<>();
        String defLanguages = configs[4];
        List<String> defLans = Arrays.asList(defLanguages.split(","));
        for (String lan : defLans) {
            if (lan.equals("values")) {
                defLanuageList.put(lan, AppConfigs.LOCALE_EN);
            } else {
                defLanuageList.put(lan, lan.replace("values-", ""));
            }
        }
    }

    public void launch() {
        File file = new File(sourceDirPath);
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
        loadResFilePath(sourceDirPath, resFilePaths);
        System.out.println("launch: resFilePaths: " + resFilePaths);

        if (resFilePaths.isEmpty()) {
            System.out.println("launch: resFilePaths.isEmpty return ");
            return;
        }

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
                System.out.println("start valuesFilePath: " + valuesFilePath + " , valueFileName = " + valueFileName);

                if (supportLanguageList.contains(valueFileName)) {
                    // values目录对应的语言
                    // String locale = getLocale(valueFileName);
                    String locale = defLanuageList.get(valueFileName);
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

        if (stringsMap.isEmpty()) {
            System.out.println("stringsMap.isEmpty。。。");
            return;
        }
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
        Cell ja = titleRow.createCell(4);
        ja.setCellValue("zh-ja");

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
            ja = titleRow.createCell(4);
            ja.setCellValue(bean.mJaContent);
        }

        // 删除空行
//        sheet = PIOUtils.getAccuracyContextNum(sheet);

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputPath);
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
            if (!isDirectory) {
                System.out.println("getResFilePath: 忽略文件 -- name: " + name);
                continue;
            }

            if (ignoreFileList.contains(name)) {
                continue;
            }

            if (isDirectory) {
                if (name.toLowerCase().equals("res")) {
                    File[] listFiles = rootListFile.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            // 只添加支持的列表，其它全部忽略
                            if (supportLanguageList.contains(name)) {
                                return true;
                            }
                            return false;
                        }
                    });
                    System.out.println("getResFilePath listFiles: " + listFiles.length);
                    if (listFiles.length > 0) {
                        // 添加的是以res结尾的路径，只会有一个
                        resFilePaths.add(rootListFile.getAbsolutePath());
                        System.out.println("getResFilePath resFilePaths.len = " + resFilePaths.size());
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
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        sourceDirPath = null;
        outputPath = null;
        supportLanguageList.clear();
        supportLanguageList = null;
        ignoreFileList.clear();
        ignoreFileList = null;
        defLanuageList.clear();
        defLanuageList = null;
    }

}
