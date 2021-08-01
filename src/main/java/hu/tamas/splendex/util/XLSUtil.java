package hu.tamas.splendex.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

public class XLSUtil {

    public static byte[] create(final Vector data, final String[] colNames, final String sheetName) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);

        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(BorderStyle.MEDIUM);

        addRowToSheet(sheet, colNames, true, 0, style1);

        int firstDataRowIndex = (colNames == null) ? 0 : 1;
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                addRowToSheet(sheet, (String[]) data.elementAt(i), false, i + firstDataRowIndex, style1);
            }
        }

        for (int i = 0; i < colNames.length; i++){
            sheet.autoSizeColumn(i);
        }

        try {
            wb.write(out);
        } finally {
            out.close();
        }
        return out.toByteArray();
    }

    private static void addRowToSheet(final HSSFSheet sheet, String[] row, boolean borderBottom, int rowIndex, HSSFCellStyle style) throws IOException {
        if (row == null) {
            return;
        }
        HSSFRow hssfRow = sheet.createRow(rowIndex);
        for (int i = 0; i < row.length; i++) {
            HSSFCell cell = hssfRow.createCell(i);
            cell.setCellValue(row[i]);

            if (borderBottom){
                cell.setCellStyle(style);
            }
        }
    }

}
