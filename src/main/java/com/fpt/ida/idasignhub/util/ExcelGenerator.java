package com.fpt.ida.idasignhub.util;
import com.fpt.ida.idasignhub.data.PageStatisticsVM;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {
    public ExcelGenerator() {
    }
    public static byte[] exportPageStatistics(List<PageStatisticsVM> pageStatistics) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Page Statistics");

            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {
                    "File Path",
                    "Number of pages",
                    "A5",
                    "A4",
                    "A3",
                    "A2",
                    "A1",
                    "A0",
                    "NA"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowNum = 1;
            for (PageStatisticsVM page : pageStatistics) {
                XSSFRow row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(page.getFilePath());
                row.createCell(1).setCellValue(page.getTotalPages());
                row.createCell(2).setCellValue(page.getTotalA5Pages());
                row.createCell(3).setCellValue(page.getTotalA4Pages());
                row.createCell(4).setCellValue(page.getTotalA3Pages());
                row.createCell(5).setCellValue(page.getTotalA2Pages());
                row.createCell(6).setCellValue(page.getTotalA1Pages());
                row.createCell(7).setCellValue(page.getTotalA0Pages());
                row.createCell(8).setCellValue(page.getOther());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private static CellStyle createHeaderCellStyle(XSSFWorkbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerCellStyle.setFont(font);
        return headerCellStyle;
    }
}
