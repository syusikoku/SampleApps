package com.kasax.parserhelper;

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
        String[] r = new String[2];
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
