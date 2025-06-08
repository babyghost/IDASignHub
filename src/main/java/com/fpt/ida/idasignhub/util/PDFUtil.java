package com.fpt.ida.idasignhub.util;

import com.fpt.ida.idasignhub.bussiness.SignHub;
import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.data.FileEntry;
import com.fpt.ida.idasignhub.data.model.DataResult;
import com.fpt.ida.idasignhub.data.model.DataVanBan;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PDFUtil {
    private static final double MAX_IMAGE_WIDTH = 4000.0;
    private static final double MAX_IMAGE_HEIGHT = 4000.0;

    private static final double SCALE_FACTOR_HIGH = 0.5;
    private static final double SCALE_FACTOR_MEDIUM = 0.6;
    private static final double SCALE_FACTOR_LOW = 0.7;

    private static final double QUALITY_HIGH = 0.75;
    private static final double QUALITY_MEDIUM = 0.6;
    private static final double QUALITY_LOW = 0.4;


    public static void exportExcelResultScan(String path) throws  IOException {


        // bước 1 từ folder đọc tên các folder con. mỗi folder con là 1 bộ hồ sơ
        List<FileEntry> fileEntries = new ArrayList<>();
        File folder_root = new File(path);
        System.out.println("folder_root: "+ folder_root);
        if(folder_root.exists()) {
            fileEntries = FileUtils.getFolders(folder_root); //  sẽ lấy file pdf, nếu file zip thì unzip và dequy tiếp để lấy hết file
        }
        List<DataResult> results = new ArrayList<>();
        int num = 0;
        for(FileEntry fileEntry : fileEntries) {
            num = num + 1;
            System.out.println("folder: "+ fileEntry.getFileName());
            DataResult dataResult = new DataResult();
            dataResult.setStt(num);
            dataResult.setHoSo(fileEntry.getFileName());
            dataResult.setHoSoId(UUID.randomUUID().toString());
            File f = new File(fileEntry.getFilePath());
            if(f.exists()) {
                List<FileEntry> arrs = FileUtils.getFilesRecursively(f);
                int dem = 0;
                List<DataVanBan> filePDFs = new ArrayList<DataVanBan>();
                for(FileEntry fEntry : arrs) {
                    dem++;
                    DataVanBan a = new DataVanBan();
                    a.setHoSoId(dataResult.getHoSoId());
                    a.setVanBanId(UUID.randomUUID().toString());
                    a.setTenVanBan(fEntry.getFileName());
                    a.setPath(fEntry.getFilePath());
                    a.setStt(dem);
                    Map<String, Integer> soTrang = FileUtils.countPagesBySize(fEntry.getFilePath());
                    a.setSoToA0(soTrang.get("A0"));
                    a.setSoToA1(soTrang.get("A1"));
                    a.setSoToA2(soTrang.get("A2"));
                    a.setSoToA3(soTrang.get("A3"));
                    System.out.println("A4 = " + soTrang.get("A4"));
                    a.setSoToA4(soTrang.get("A4"));
                    a.setSizeOther(soTrang.get("Other"));
                    int st = soTrang.get("A0") + soTrang.get("A1") + soTrang.get("A2") + soTrang.get("A3") + soTrang.get("A4") + soTrang.get("Other");
                    a.setSoTrang(st);
                    filePDFs.add(a);
                }
                dataResult.setDataVanBans(filePDFs);
            }
            results.add(dataResult);
        }

        try {
            // Create workbook and sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Sheet1");
            int rowNum = 0;
            String[] headers = {"STT", "Tên Văn Bản", "Số Trang", "A0", "A1", "A2", "A3", "A4", "Size khác", "Đường Dẫn"};
            // 2. Tạo header row
            Row headerRow = sheet.createRow(rowNum);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);

                // Có thể style header (in đậm)
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            for (DataResult rowData : results) {
                rowNum++;
                XSSFRow row = sheet.createRow(rowNum);
                if(rowData.getStt() != null) {
                    XSSFCell cell = row.createCell(0);
                    cell.setCellValue(" "+rowData.getStt());
                    // Có thể style header (in đậm)
                    CellStyle style = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setBold(true);
                    style.setFont(font);
                    cell.setCellStyle(style);
                }
                if(rowData.getHoSo() != null) {
                    XSSFCell cell1 = row.createCell(1);
                    cell1.setCellValue(rowData.getHoSo());
                    // Có thể style header (in đậm)
                    CellStyle style = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setBold(true);
                    style.setFont(font);
                    cell1.setCellStyle(style);
                }
                if(rowData.getTenVanBan() != null) {
                    XSSFCell cell2 = row.createCell(2);
                    cell2.setCellValue(rowData.getTenVanBan());
                    // Có thể style header (in đậm)
                    CellStyle style = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setBold(true);
                    style.setFont(font);
                    cell2.setCellStyle(style);
                }
                if(!rowData.getDataVanBans().isEmpty()) {
                    for (DataVanBan r : rowData.getDataVanBans()) {
                        rowNum++;
                        row = sheet.createRow(rowNum);
                        if(r.getStt() != null) {
                            XSSFCell cell = row.createCell(0);
                            String stt = " "+ rowData.getStt() + "." + r.getStt();
                            cell.setCellValue(stt);
                        }
                        if(r.getTenVanBan() != null) {
                            XSSFCell cell2 = row.createCell(1);
                            cell2.setCellValue(r.getTenVanBan());
                        }
                        if(r.getSoTrang() != null) {
                            XSSFCell cell44 = row.createCell(2);
                            cell44.setCellValue(r.getSoTrang());
                        }
                        if(r.getSoToA0() != null) {
                            XSSFCell cell4 = row.createCell(3);
                            cell4.setCellValue(r.getSoToA0());
                        }
                        if(r.getSoToA1() != null) {
                            XSSFCell cell5 = row.createCell(4);
                            cell5.setCellValue(r.getSoToA1());
                        }
                        if(r.getSoToA2() != null) {
                            XSSFCell cell6 = row.createCell(5);
                            cell6.setCellValue(r.getSoToA2());
                        }
                        if(r.getSoToA3() != null) {
                            XSSFCell cell7 = row.createCell(6);
                            cell7.setCellValue(r.getSoToA3());
                        }
                        if(r.getSoToA4() != null) {
                            XSSFCell cell8 = row.createCell(7);
                            cell8.setCellValue(r.getSoToA4());
                        }
                        if(r.getSizeOther() != null) {
                            XSSFCell cell99 = row.createCell(8);
                            cell99.setCellValue(r.getSizeOther());
                        }
                        if(r.getPath() != null) {
                            XSSFCell cell3 = row.createCell(9);
                            cell3.setCellValue(r.getPath());
                        }
                    }
                }
            }
            for (int i = 0; i <= 9; i++) {
                sheet.autoSizeColumn(i);
            }
            String outputPath =  ConstantUtil.IDA_ROOT_FOLDER + "/dataresults.xlsx";
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
                System.out.println("Đã lưu file Excel tại: " + outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Đừng quên đóng workbook để giải phóng tài nguyên
                try {
                    workbook.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void compressFileFromFolderPDF() throws IOException {
        String folderDaNenXong = ConstantUtil.IDA_ROOT_FOLDER + "/folder_filedanenxong";
        String folderFileCanNen = ConstantUtil.IDA_ROOT_FOLDER + "/folder_filecannen";
        // step 1: đọc danh sách file đệ quy và list ra danh sách

        List<FileEntry> fileEntries = new ArrayList<>();
        File folder_root = new File(folderFileCanNen);
        if(folder_root.exists()) {
            fileEntries = FileUtils.getFilesRecursively(folder_root); //  sẽ lấy file pdf, nếu file zip thì unzip và dequy tiếp để lấy hết file
        }
        for(FileEntry fileEntry : fileEntries) {
            String pathOld = fileEntry.getFilePathOnServer().substring(0, fileEntry.getFilePathOnServer().lastIndexOf("\\")).replace("IDASignHubTool\\folder_filecannen\\", "");
            System.out.println("pathOld: "+pathOld);
            String extesion = fileEntry.getFileName().substring(fileEntry.getFileName().lastIndexOf(".") + 1);
            String fileName = fileEntry.getFileName().substring(0, fileEntry.getFileName().lastIndexOf("."));

            String fileResultPAth = folderDaNenXong + "/" + pathOld + "/"+fileName+"."+extesion;
            System.out.println("compress: "+fileResultPAth);
            File fileResult = new File(fileResultPAth);
            if(!Files.exists(fileResult.toPath())) {
                FileUtils.createFileAndFolder(fileResultPAth);
                File file = new File(fileEntry.getFilePath());
                System.out.println("file "+file.getAbsolutePath());
                if(extesion.equalsIgnoreCase("pdf")) {
                    PDFUtil.compressPdf(fileEntry.getFilePath(), fileResultPAth, "low");
                }
            }

        }

    }

    public static void filterFileFromFolderPDF() throws IOException {
//        String folder = ConstantUtil.IDA_ROOT_FOLDER + "/folder_split";
        String folder = "E:\\[IDA-KYSO]SXD_2018_p2";
        // step 1: đọc danh sách file đệ quy và list ra danh sách

        List<FileEntry> fileEntries = new ArrayList<>();
        File folder_root = new File(folder);
        if(folder_root.exists()) {
            fileEntries = FileUtils.getFilesRecursively(folder_root); //  sẽ lấy file pdf, nếu file zip thì unzip và dequy tiếp để lấy hết file
        }
        for(FileEntry fileEntry : fileEntries) {
            String pathOld = fileEntry.getFilePathOnServer().substring(0, fileEntry.getFilePathOnServer().lastIndexOf("\\")).replace("IDASignHubTool\\folder_split\\", "");
            System.out.println("pathOld: "+pathOld);
            String extesion = fileEntry.getFileName().substring(fileEntry.getFileName().lastIndexOf(".") + 1);
            String fileName = fileEntry.getFileName().substring(0, fileEntry.getFileName().lastIndexOf("."));
            String pathFile = pathOld + "/" + fileName;
            PDFUtil.filterPdf(fileEntry.getFilePath(), pathFile, 200);
        }
    }
    public static void splitFileFromFolderPDF() throws IOException {
//        String folder = ConstantUtil.IDA_ROOT_FOLDER + "/folder_split";
        String folder = "D:\\code\\IDASignHub\\IDASignHubTool\\folder_split\\New folder";
        // step 1: đọc danh sách file đệ quy và list ra danh sách

        List<FileEntry> fileEntries = new ArrayList<>();
        File folder_root = new File(folder);
        if(folder_root.exists()) {
            fileEntries = FileUtils.getFilesRecursively(folder_root); //  sẽ lấy file pdf, nếu file zip thì unzip và dequy tiếp để lấy hết file
        }
        for(FileEntry fileEntry : fileEntries) {
            String pathOld = fileEntry.getFilePathOnServer().substring(0, fileEntry.getFilePathOnServer().lastIndexOf("\\")).replace("D:\\code\\IDASignHub\\New folder\\", "");
            System.out.println("pathOld: "+pathOld);
            String extesion = fileEntry.getFileName().substring(fileEntry.getFileName().lastIndexOf(".") + 1);
            String fileName = fileEntry.getFileName().substring(0, fileEntry.getFileName().lastIndexOf("."));
            String pathFile = pathOld + "/" + fileName;
            PDFUtil.splitPdf(fileEntry.getFilePath(), pathFile, 0);
        }
    }

    public static void filterPdf(String pathFile, String name, int size) throws IOException {
        System.out.println(" splitPdf ");
        String folder = ConstantUtil.IDA_ROOT_FOLDER + "/folder_filter";
        List<File> documents = new ArrayList<>();
        try {
            File file = Paths.get(pathFile).toFile();
            long fileSizeInBytes = file.length();
            double megabytes = fileSizeInBytes / (1024.0 * 1024.0);
            if(megabytes > size) {
                documents.add(file);
            }
            System.out.println(" documents size = "+documents.size());
            for (int j = 0; j < documents.size(); j++) {
                String filePDFPath = folder + "/result/" + name.replace("_signed","_").replace("E:\\[IDA-KYSO]SXD_2018_p2", "") + ".pdf";
                File pdfPath = new File(filePDFPath);
                // Kiểm tra và tạo thư mục cha nếu chưa tồn tại
                if(pdfPath.exists()) {
                    continue;
                }
                File parentDir = pdfPath.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    boolean created = parentDir.mkdirs();
                    if (created) {
                        System.out.println("Đã tạo thư mục cha: " + parentDir.getAbsolutePath());
                    } else {
                        System.out.println("Không thể tạo thư mục cha!");
                    }
                }
                try (
                        FileInputStream inputStream = new FileInputStream(documents.get(j));
                        FileOutputStream outputStream = new FileOutputStream(filePDFPath)
                ) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static File removeSignature(File file) {
        // Truy cập AcroForm
        try (PDDocument document = PDDocument.load(file)) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            if (acroForm != null) {
                // Lấy tất cả các trường trong AcroForm
                List<PDField> fields = acroForm.getFields();
                // Duyệt qua các trường để tìm chữ ký
                Iterator<PDField> fieldIterator = fields.iterator();
                while (fieldIterator.hasNext()) {
                    PDField field = fieldIterator.next();
                    if (field.getFieldType() != null && field.getFieldType().equals("Sig")) {
                        System.out.println("Đã tìm thấy chữ ký số: " + field.getFullyQualifiedName());
                        // Xóa trường chữ ký số
                        fieldIterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public static void splitPdf(String pathFile, String name, int size) throws IOException {
        System.out.println(" splitPdf ");
        String folder = ConstantUtil.IDA_ROOT_FOLDER + "/folder_split";
        List<PDDocument> documents = new ArrayList<>();
        try (PDDocument document = PDDocument.load(new File(pathFile))) {
            // Truy cập AcroForm
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

            if (acroForm != null) {
                // Lấy tất cả các trường trong AcroForm
                List<PDField> fields = acroForm.getFields();
                // Duyệt qua các trường để tìm chữ ký
                Iterator<PDField> fieldIterator = fields.iterator();
                while (fieldIterator.hasNext()) {
                    PDField field = fieldIterator.next();
                    if (field.getFieldType() != null && field.getFieldType().equals("Sig")) {
                        System.out.println("Đã tìm thấy chữ ký số: " + field.getFullyQualifiedName());
                        // Xóa trường chữ ký số
                        fieldIterator.remove();
                    }
                }
            }
            // Lấy số lượng trang
            int totalPages = document.getNumberOfPages();
            // Duyệt qua từng trang và lưu thành file PDF tmp
            PDDocument doc = new PDDocument();
            System.out.println(" totalPages = "+totalPages);
            double filesize = 0;
            for (int i = 0; i < totalPages; i++) {
                // Tạo một tài liệu PDF mới chứa trang hiện tại
                PDDocument singlePageDocument = new PDDocument();
                singlePageDocument.addPage(document.getPage(i));
                // Đường dẫn file PDF đầu ra
                String tempFilePath = folder + "/tmp/part_" + (i + 1) + ".pdf";
                File tempFile = new File(tempFilePath);
                singlePageDocument.save(tempFilePath);
                System.out.println("Đã lưu trang tạm " + (i + 1) + " vào: " + tempFilePath);
                // Đóng tài liệu nhỏ
                singlePageDocument.close();

                long fileSizeInBytes = tempFile.length();
                double megabytes = fileSizeInBytes / (1024.0 * 1024.0);
                filesize += megabytes;
                System.out.println(" fileSizeInBytes = "+filesize );
                doc.addPage(document.getPage(i));
                if(size == 0) {
                    System.out.println(" i = "+i );
                } else {
                    if(filesize > 150.0) {
                        documents.add(doc);
                        doc = new PDDocument();
                        filesize = 0;
                    }
                }
                Files.delete(Path.of(tempFilePath));
            }
            if(doc.getNumberOfPages() > 0) {
                documents.add(doc);
            }
            System.out.println(" documents size = "+documents.size());
            for (int j = 0; j < documents.size(); j++) {
                String filePDFPath = folder + "/result/" + name.replace("_signed","_").replace("D:\\code\\IDASignHub\\IDASignHubTool\\folder_split\\New folder", "") + ".pdf";
                System.out.println("filePDFPath : "+filePDFPath.replace("D:\\code\\IDASignHub\\IDASignHubTool\\folder_split\\New folder", ""));
                File pdfPath = new File(filePDFPath);
                // Kiểm tra và tạo thư mục cha nếu chưa tồn tại
                File parentDir = pdfPath.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    boolean created = parentDir.mkdirs();
                    if (created) {
                        System.out.println("Đã tạo thư mục cha: " + parentDir.getAbsolutePath());
                    } else {
                        System.out.println("Không thể tạo thư mục cha!");
                    }
                }
                PDDocument pageDocument = documents.get(j);
                pageDocument.save(pdfPath);
                pageDocument.close();
            }
            document.close();
        }
    }
    public static void compressPdf(String pathFile, String dest, String compressionLevel) throws IOException {
        try (PDDocument document = PDDocument.load(new File(pathFile))) {
            System.out.println(document.getNumberOfPages());
            for (PDPage page : document.getPages()) {
                System.out.println(page.getResources().getXObjectNames());
                page
                        .getResources()
                        .getXObjectNames()
                        .forEach(name -> {
                            try {
                                PDXObject xObject = page.getResources().getXObject(name);

                                if (xObject instanceof PDImageXObject) {
                                    PDImageXObject image = (PDImageXObject) xObject;
                                    BufferedImage bufferedImage = image.getImage();

                                    double scaleFactor = getScaleFactorForCompressionLevel(compressionLevel);
                                    double outputQuality = getQualityForCompressionLevel(compressionLevel);
                                    System.out.println(scaleFactor);
                                    System.out.println(outputQuality);

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    Thumbnails
                                            .of(bufferedImage)
                                            .scale(scaleFactor)
                                            .outputQuality(outputQuality)
                                            .outputFormat("jpg")
                                            .toOutputStream(baos);

                                    byte[] imageBytes = baos.toByteArray();

                                    PDImageXObject compressedImage = PDImageXObject.createFromByteArray(
                                            document,
                                            imageBytes,
                                            "compressed_image"
                                    );
                                    page.getResources().put(name, compressedImage);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
            System.out.println(dest);
            document.save(new FileOutputStream(dest));
        }
    }

    public static PDPage pdfLandscapeMode(PDPage page) {
        PDRectangle mediaBox = page.getMediaBox();
        // Đổi chiều rộng và chiều cao để tạo trang landscape
        page.setMediaBox(new PDRectangle(mediaBox.getHeight(), mediaBox.getWidth()));
        return page;
    }
    public static float scaleWidth(float widthPage, float scaleRatio) {
       try {
           return widthPage / scaleRatio;
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public static float scaleHeight(float heightPage, float scaleRatio) {
        try {
            return heightPage / scaleRatio;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static double getScaleFactorForCompressionLevel(String compressionLevel) {
        switch (compressionLevel.toLowerCase()) {
            case "high":
                return SCALE_FACTOR_HIGH;
            case "medium":
                return SCALE_FACTOR_MEDIUM;
            case "low":
                return SCALE_FACTOR_LOW;
            default:
                throw new IllegalArgumentException("Invalid compression level: " + compressionLevel);
        }
    }

    public static double getQualityForCompressionLevel(String compressionLevel) {
        switch (compressionLevel.toLowerCase()) {
            case "high":
                return QUALITY_LOW;
            case "medium":
                return QUALITY_MEDIUM;
            case "low":
                return QUALITY_HIGH;
            default:
                throw new IllegalArgumentException("Invalid compression level: " + compressionLevel);
        }
    }

}
