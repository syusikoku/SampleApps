package com.kasax.parserhelper.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ParserHelper {
    /**
     * 解析配置文件
     *
     * @return
     */
    public String[] loadAppConfig(String configFilePath) {
        String[] r = new String[5];
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(configFile);
                if (fis != null) {
                    Properties properties = new Properties();
                    properties.load(fis);
                    String sourcePath = properties.getProperty("SOURCE_DIR_PATH");
                    System.out.println("sourcePath = " + sourcePath);
                    r[0] = sourcePath;
                    String xlsPath = properties.getProperty("OUT_XLS_DIR_PATH");
                    System.out.println("xlsPath = " + xlsPath);
                    r[1] = xlsPath;
                    String supportLanugageList = properties.getProperty("SUPPORT_LANUGAGE_LIST");
                    System.out.println("supportLanugageList = " + supportLanugageList);
                    r[2] = supportLanugageList;
                    String ignoreFileList = properties.getProperty("IGNORE_FILE_LIST");
                    System.out.println("ignoreFileList = " + ignoreFileList);
                    r[3] = ignoreFileList;
                    String defaultLanuageList = properties.getProperty("DEFAULT_LANUAGE_LIST");
                    System.out.println("defaultLanuageList = " + defaultLanuageList);
                    r[4] = defaultLanuageList;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return r;
    }
}
