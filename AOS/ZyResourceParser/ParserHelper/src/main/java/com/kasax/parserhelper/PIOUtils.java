package com.kasax.parserhelper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class PIOUtils {

    public static Sheet getAccuracyContextNum(Sheet sheet) {
        // 删除空行
        for (int i = sheet.getLastRowNum(); i >= 0; i--) {
            Row row = sheet.getRow(i);
            int c = row.getFirstCellNum();
            Cell cell = row.getCell(c);
            if (cell.getCellTypeEnum() != CellType.STRING) {
                sheet.removeRow(row);
            } else {
                break;
            }
        }
        return sheet;
    }

}
