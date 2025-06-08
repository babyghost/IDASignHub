package com.fpt.ida.idasignhub.util;

import com.fpt.ida.idasignhub.bussiness.SignHub;
import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.data.*;
import com.fpt.ida.idasignhub.data.model.MauImportVanBan;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelFileUtil {


    public static byte[] createFileImportVanBanImportNew(List<MauImportVanBan> dsHoSo) throws IOException {
        System.out.println("MauImportVanBan");
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Import van ban");

            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {
                    "SỐ, KÝ HIỆU VĂN BẢN",
                    "NGÀY THÁNG NĂM VĂN BẢN",
                    "TRÍCH YẾU",
                    "TÁC GIẢ VĂN BẢN",
                    "TỪ TỜ",
                    "ĐẾN TỜ",
                    "LOẠI",
                    "ĐƯỜNG DẪN",
                    "ĐỘ MẬT",
                    "MỨC ĐỘ TIN CẬY",
                    "TÌNH TRẠNG VẬT LÝ",
                    "SỐ TRANG"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowNum = 1;
            for (MauImportVanBan page : dsHoSo) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(page.getSoKyHieuVanBan());
                row.createCell(1).setCellValue(page.getNgayThangNamVanBan());
                row.createCell(2).setCellValue(page.getTrichYeu());
                row.createCell(3).setCellValue(page.getTacGiaVanBan());
                row.createCell(4).setCellValue(page.getTuTo());
                row.createCell(5).setCellValue(page.getDenTo());
                row.createCell(6).setCellValue(page.getLoai());
                row.createCell(7).setCellValue(page.getDuongDan());
                row.createCell(8).setCellValue(page.getDoMat());
                row.createCell(9).setCellValue(page.getMucDoTinCay());
                row.createCell(10).setCellValue(page.getTinhTrangVatLy());
                row.createCell(11).setCellValue(page.getSoTrang());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public static byte[] createFileImportVanBan(List<MauImportVanBan> dsHoSo) throws IOException {
        System.out.println("createFileImportVanBan");
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Import van ban");

            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {
                    "SỐ, KÝ HIỆU VĂN BẢN",
                    "NGÀY THÁNG NĂM VĂN BẢN",
                    "TRÍCH YẾU",
                    "TÁC GIẢ VĂN BẢN",
                    "TỪ TỜ",
                    "ĐẾN TỜ",
                    "LOẠI",
                    "ĐƯỜNG DẪN",
                    "ĐỘ MẬT",
                    "MỨC ĐỘ TIN CẬY",
                    "TÌNH TRẠNG VẬT LÝ",
                    "SỐ TRANG"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            int rowNum = 1;
            for (MauImportVanBan page : dsHoSo) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(page.getSoKyHieuVanBan());

                try {
                    Date date = inputFormat.parse(page.getNgayThangNamVanBan());
                    row.createCell(1).setCellValue(dateFormat.format(date));
                } catch (ParseException e) {
                    row.createCell(1).setCellValue(page.getNgayThangNamVanBan());
                }

                String regex = "\\d{3}\\.\\d{2}\\.\\d{2}\\.[A-Za-z0-9]{3,4}\\.\\d{4}\\.\\d{4}\\.\\d{2}-";
                String trichYeu2 = page.getTrichYeu().replaceAll(regex, "");
                System.out.println(trichYeu2);
                String tenTacGia = page.getTacGiaVanBan();
                if(tenTacGia.equalsIgnoreCase("Sở XD")) {
                    tenTacGia = "Sở Xây Dựng";
                }
                row.createCell(2).setCellValue(trichYeu2);
                row.createCell(3).setCellValue(tenTacGia);
                row.createCell(4).setCellValue(page.getTuTo());
                row.createCell(5).setCellValue(page.getDenTo());
                row.createCell(6).setCellValue(page.getLoai());
                row.createCell(7).setCellValue(page.getDuongDan());
                row.createCell(8).setCellValue(page.getDoMat());
                row.createCell(9).setCellValue(page.getMucDoTinCay());
                row.createCell(10).setCellValue(page.getTinhTrangVatLy());
                row.createCell(11).setCellValue(page.getSoTrang());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    public static byte[] createFileImportHoSo(List<FileEntryExportExcel> dsHoSo) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Danhsachhoso");
            List<Integer> years = listYearFromData(dsHoSo);
            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {
                    "Hộp số",
                    "HS số",
                    "Tiêu đề hồ sơ",
                    "Ngày tháng BĐ và KT",
                    "Số tờ",
                    "Thời hạn bảo quản",
                    "Ghi chú"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowNum = 1;
            for (Integer nam : years) {
                if(nam != null) {
                    XSSFRow row = sheet.createRow(rowNum);
                    row.createCell(0).setCellValue(" NĂM "+nam);
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 6));
                    rowNum = rowNum + 1;
                    rowNum = createDataCellValueFromList(dsHoSo, sheet, rowNum, nam);
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
 private static int createDataCellValueFromList(List<FileEntryExportExcel> dsHoSo, XSSFSheet sheet, int rowNum, int namCheck) {
        for (FileEntryExportExcel page : dsHoSo) {
             if(namCheck == page.getNamHoSo()) {
                 XSSFRow row = sheet.createRow(rowNum);
                 row.createCell(0).setCellValue(page.getHopSo());
                 row.createCell(1).setCellValue(page.getHoSoSo());
                 row.createCell(2).setCellValue(page.getTieuDeHoSo());
                 row.createCell(3).setCellValue(page.getNgayThangBDvaKT());
                 row.createCell(4).setCellValue(page.getSoTo());
                 row.createCell(5).setCellValue(page.getThoiHanBaoQuan());
                 row.createCell(6).setCellValue(page.getGhiChu());
                 rowNum = rowNum + 1;
             }
        }
        System.out.println("rowNum = " + rowNum);
        return rowNum;
 }
    private static CellStyle createHeaderCellStyle(XSSFWorkbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerCellStyle.setFont(font);
        return headerCellStyle;
    }

    private static List<Integer> listYearFromData(List<FileEntryExportExcel> dsHoSo) {
        List<Integer> years = dsHoSo.stream()
                .map(FileEntryExportExcel::getNamHoSo)
                .distinct()
                .collect(Collectors.toList());
        return years;
    }

    public static List<SKHCNTrackingData> listTrackingDataSoKHCN() {
        List<SKHCNTrackingData> datas = new ArrayList<>();

        return datas;
    }
}
