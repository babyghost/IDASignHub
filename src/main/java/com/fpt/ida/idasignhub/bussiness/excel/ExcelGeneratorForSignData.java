package com.fpt.ida.idasignhub.bussiness.excel;
import com.fpt.ida.idasignhub.data.FileEntry;
import com.fpt.ida.idasignhub.data.PageStatisticsVM;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class ExcelGeneratorForSignData {
    private static final Path rootLocation = Paths.get("/IDASignHubTool/folder_filecanky");
    public ExcelGeneratorForSignData() {
    }
    public static byte[] exportReportSignData(List<FileEntry> pageStatistics) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Page Statistics");

            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {
                    "#",
                    "File Path",
                    "Sign Status"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowNum = 1;
            int num = 0;
            for (FileEntry page : pageStatistics) {
                XSSFRow row = sheet.createRow(rowNum++);
                num++;
                row.createCell(0).setCellValue(num);
                row.createCell(1).setCellValue(page.getFilePath());
                row.createCell(2).setCellValue("");
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

    public static void saveFileToPath(byte[] excelBytes, Path folderPath) throws IOException {
        try {
            // Tạo ByteArrayInputStream từ mảng byte
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(excelBytes);

            // Mở workbook từ byte array
            Workbook workbook = new XSSFWorkbook(byteArrayInputStream);

            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            int numberResult = Objects.requireNonNull(folderPath.toFile().listFiles()).length;
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = currentDate.format(formatter).replace("/", "_");
            String fileReportName = folderPath.toString() + "/[" +(numberResult + 1)+ "]" + "_report_" + formattedDate + ".xlsx";

            // Ghi workbook vào file trên đĩa
            FileOutputStream fileOut = new FileOutputStream(fileReportName);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteFolder(Path folderPath) throws IOException {
        System.out.println("File Excel đã được tạo thành công từ byte array!");
        if (Files.exists(folderPath)) {
            deleteDirectoryRecursion(folderPath.toFile());
            Files.createDirectories(folderPath);
        }
    }
    public static void deleteDirectoryRecursion(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectoryRecursion(file); // Xóa các thư mục và tệp con
            }
        }
        directoryToBeDeleted.delete();
    }
}
