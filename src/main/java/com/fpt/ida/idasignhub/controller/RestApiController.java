package com.fpt.ida.idasignhub.controller;

import com.fpt.ida.idasignhub.bussiness.SignHub;
import com.fpt.ida.idasignhub.constant.ConstantUtil;
import com.fpt.ida.idasignhub.data.*;
import com.fpt.ida.idasignhub.data.model.MauImportVanBan;
import com.fpt.ida.idasignhub.data.model.SignHubLocal;
import com.fpt.ida.idasignhub.sheduler.CountFileScheduler;
import com.fpt.ida.idasignhub.util.*;
import com.ruiyun.jvppeteer.cdp.core.Puppeteer;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class RestApiController {

    @GetMapping("/api/makePDF2Layer")
    public ResponseEntity<JSONObject> makePDF2Layer() throws IOException {
        SignHubLocalUtill.makePDF2LayerFromMockData();
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Tao file PDF thanh cong");
        return ResponseEntity.ok((JSONObject) rep);
    }
    @PostMapping("/api/runAutoScan")
    public ResponseEntity<JSONObject> runAutoScan() throws IOException {
        SignHub.signPDFAuto();
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kich hoạt autoScan");
        return ResponseEntity.ok((JSONObject) rep);
    }
    @PostMapping("/api/signFolderByPath")
    public ResponseEntity<JSONObject> signFolderByPath(@RequestBody ParamRequest param) throws IOException {
        System.out.println(param.getPathFolder());
        SignHub.signPDFAutoWithPath(param.getPathFolder());
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kich hoạt autoScan");
        return ResponseEntity.ok((JSONObject) rep);
    }


    @PostMapping("/api/run-pupperteer")
    public ResponseEntity<JSONObject> runPupperTeer(@RequestBody ParamRequest param) throws Exception {
        String folder = FileUtils.normalizeToForwardSlash(param.getPathFolder());
        PupperTeerUtils.runPupperTeer();
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kich hoạt PupperTeer");
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping("/api/countDataFromFolderRoot")
    public ResponseEntity<JSONObject> countDataFromFolderRoot(@RequestBody ParamRequest param) throws IOException {
        System.out.println(param.getPathFolder());
        String folder = FileUtils.normalizeToForwardSlash(param.getPathFolder());
        PDFUtil.exportExcelResultScan(folder);
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kich hoạt autoScan");
        return ResponseEntity.ok((JSONObject) rep);
    }

    @GetMapping("/api/list/config")
    public ResponseEntity<JSONObject> getConfigLocal() throws IOException {
        SignHubLocal obj = SignHubLocalUtill.getConfigFromFileConfig();
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Danh sách cấu hình");
        rep.put("requestObject", obj);
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping(value = "/api/switch/trangthai", consumes = {"application/json"})
    public ResponseEntity<JSONObject> switchTrangThaiHoatDong(@RequestBody ConfigDataRequest param) {
        String mess =  SignHubLocalUtill.swtichTrangThaiHoatDong(param.getIndex());
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", mess);
        return ResponseEntity.ok((JSONObject) rep);
    }
    @PostMapping("/api/pdf/compress")
    public ResponseEntity<JSONObject> compressPDF() throws IOException {
        PDFUtil.compressFileFromFolderPDF();
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kích hoạt CompressPDF Action !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping("/api/pdf/file-pdf-split")
    public ResponseEntity<JSONObject> splitFilePDF() throws IOException {
        PDFUtil.splitFileFromFolderPDF();
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kích hoạt Split file Action !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }
    @PostMapping("/api/pdf/file-pdf-filter")
    public ResponseEntity<JSONObject> filterFilePDF() throws IOException {
        PDFUtil.filterFileFromFolderPDF();
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kích hoạt Split file Action !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping("/api/create/file-import")
    public ResponseEntity<JSONObject> createFileImportHoSo() throws IOException {
        Path rootLocation = Paths.get("filestorage_thongke");
        List<FileEntryExportExcel> downloadedFiles = new ArrayList<>();
        try {
//            Path folderPath = rootLocation.resolve("tmp_data");
            Path folderPath = Paths.get("E:\\[IDA-KYSO]SXD_2018_p1");
//            if (!Files.exists(folderPath)) {
//                Files.createDirectories(folderPath);
//            }
            int checkNumberFileFromFolderFileData = Objects.requireNonNull(folderPath.toFile().listFiles()).length;
            if(checkNumberFileFromFolderFileData > 0) {
                downloadedFiles = FileUtils.getFileEntryExportExcelFromFolderRecursion(folderPath.toFile().getPath());
                List<FileEntryExportExcel> listHoSo = new ArrayList<>();
                System.out.println("Writing downloaded file list to excel.");
                System.out.println("downloadedFiles = " + downloadedFiles.size());
                // Trường hợp có file tracking sẽ tiến hành lấy dữ liệu từ file tracking để bổ sung vào file excel
                String pathFileTracking = "D:\\FIS\\Result Sở KHCN\\Tracking Deli.xlsx";
                try (FileInputStream fis = new FileInputStream(pathFileTracking);
                     Workbook workbook = new XSSFWorkbook(fis)) {

                    Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
                    List<SKHCNTrackingData> data = new ArrayList<>();

                    // Bỏ qua tiêu đề (giả định dòng đầu tiên là tiêu đề)
                    Iterator<Row> rowIterator = sheet.iterator();
                    if (rowIterator.hasNext()) {
                        rowIterator.next();
                    }

                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        String hopSo = FileUtils.getStringValue(row.getCell(0));
                        String hs = FileUtils.getStringValue(row.getCell(1));
                        String tieuDe = FileUtils.getStringValue(row.getCell(2));
                        String ghiChu = FileUtils.getStringValue(row.getCell(3));
                        String thoiGian = FileUtils.getStringValue(row.getCell(4));
                        String tenTep = FileUtils.getStringValue(row.getCell(5));
                        String tep  = FileUtils.getStringValue(row.getCell(6));
                        String toSo = FileUtils.getStringValue(row.getCell(7));
                        int soTrang = (int) row.getCell(8).getNumericCellValue();

                        if(hopSo != null && !hopSo.equals("Không xác định")) {
                            SKHCNTrackingData d = new SKHCNTrackingData();
                            String loai = FileUtils.getLoaiFromTenFile(tenTep, 2);
                            d.setLoai(loai);
                            d.setHopSo(hopSo);
                            d.setGhiChu(ghiChu);
                            d.setHoSoSo(hs);
                            d.setTieuDeHoSo(tieuDe);
                            d.setThoiGian(thoiGian);
                            d.setTenTep(tenTep);
                            d.setTep(tep);
                            d.setToSo(toSo);
                            d.setSoTo(String.valueOf(soTrang));
                            data.add(d);
                        }
                    }

                    // In danh sách đối tượng ra console
                    for(int i = 0; i < data.size(); i++) {
                        SKHCNTrackingData d = data.get(i);
                        System.out.println("loai " + d.getLoai() + " maHoSo = "+d.getTep());
                    }

                    // bước 2 xử lý list data

//                    for(int i = 0; i < downloadedFiles.size(); i++) {
//                        FileEntryExportExcel object = FileUtils.mappingDataTracking(downloadedFiles.get(i), data);
//                        listHoSo.add(object);
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] excelBytes = ExcelFileUtil.createFileImportHoSo(downloadedFiles);

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
                String fileReportName = folderPath.toString() + "/[" +(numberResult + 1)+ "]" + "_DanhSachHoSo_" + formattedDate + ".xlsx";

                // Ghi workbook vào file trên đĩa
                FileOutputStream fileOut = new FileOutputStream(fileReportName);
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();

                System.out.println("File Excel đã được tạo thành công từ byte array!");
                System.out.println("End create report excel ...");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kích hoạt CompressPDF Action !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping("/api/create/file-import-vanban")
    public ResponseEntity<JSONObject> createFileImportVanBan() throws IOException {
        try {
            Path folderPath = Paths.get("E:\\SXD2017\\[IDA-KYSO]SXD_2017_p1\\data2017part1\\p1");
            File files = folderPath.toFile();
            if(files.isDirectory()) {
                for(File file : Objects.requireNonNull(files.listFiles())) {
                    FileUtils.createFileExcelImportVanBan(file.getPath());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kích hoạt CompressPDF Action !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }
    @PostMapping("/api/pdf/file-copy-from-folder")
    public ResponseEntity<JSONObject> copyFileFromFolder() throws IOException {
        try {
            Path toFolder = Paths.get("E:\\[IDA-KYSO]SXD_2018_p1\\data2018part1");
            Path fromFolder = Paths.get("D:\\FIS\\Result Sở Xây Dựng\\result_kyso_2018\\data2018part1");
            File toFolderFile = toFolder.toFile();
            if(toFolderFile.isDirectory()) {
                for(File file : Objects.requireNonNull(toFolderFile.listFiles())) {
                    if(file.isDirectory()) {
                        FileUtils.checkFileFromFolder(file.getPath(), toFolder.toString(), fromFolder.toString());
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kích hoạt CompressPDF Action !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping("api/pdf/data-excel-json")
    public ResponseEntity<JSONObject> getDataFromFolderExcel() throws IOException {
        try {
            Path folderRoot = Paths.get("E:\\SXD2017");
            List<MauImportVanBan> aData =  FileUtils.getDataFromFolderExcel(folderRoot.toString());
            // tạo file excel import
            for(MauImportVanBan d : aData) {
                System.out.println(d.toString());
            }
            byte[] excelBytes = ExcelFileUtil.createFileImportVanBanImportNew(aData);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(excelBytes);

            Workbook workbook2 = new XSSFWorkbook(byteArrayInputStream);
            String fileImportExcelFull = "fileImportNew.xlsx";
            String fileReportName = folderRoot.toString() + "/" +fileImportExcelFull;

            FileOutputStream fileOut = new FileOutputStream(fileReportName);
            workbook2.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kích hoạt CompressPDF Action !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping(value = "api/pdf/rename-file", consumes = {"application/json"})
    public ResponseEntity<JSONObject> renameFile(@RequestBody ParamRequest param) throws IOException {
        try {
            System.out.println(param.getPathFolder());
//            Path folderRoot = Paths.get("E:\\[IDA-KYSO]SXD_2017_p1\\data2017part1");
            Path folderRoot = Paths.get(param.getPathFolder());
            File toFolderFile = folderRoot.toFile();
            if(toFolderFile.isDirectory()) {
                FileUtils.reNameFilePDF(toFolderFile);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Action done !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }


    // fix file excel version 2
    @PostMapping(value = "api/pdf/fix-file-excel-maumoi", consumes = {"application/json"})
    public ResponseEntity<JSONObject> fixFileExcelMauMoi(@RequestBody ParamRequest param) throws IOException {
        try {
//            Path folderRoot = Paths.get("E:\\[IDA-KYSO]SXD_2017_p1\\data2017part1");
            Path folderRoot = Paths.get(param.getPathFolder());
            File toFolderFile = folderRoot.toFile();
            if(toFolderFile.isDirectory()) {
                for(File file : Objects.requireNonNull(toFolderFile.listFiles())) {
                    if(file.isDirectory()) {
                        FileUtils.fixFileExcelFromFolder(file.getPath());
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Action done !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }

    //end fix file excel
    // fix file excel version 2
    @PostMapping(value = "api/pdf/fix-file-excel-maumoi-sxd", consumes = {"application/json"})
    public ResponseEntity<JSONObject> fixFileExcelMauMoiSXD(@RequestBody ParamRequest param) throws IOException {
        try {
            Path folderRoot = Paths.get(param.getPathFolder());
            File toFolderFile = folderRoot.toFile();
            if(toFolderFile.isDirectory()) {
                for(File file : Objects.requireNonNull(toFolderFile.listFiles())) {
                    if(file.isDirectory()) {
//                        FileUtils.fixFileExcelFromFolder(file.getPath());
                        FileUtils.createFileExcelImportVanBan(file.getPath());
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Action done !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping("api/pdf/fix-file-excel")
    public ResponseEntity<JSONObject> fixFileExcel() throws IOException {
        try {
            Path folderRoot = Paths.get("E:\\[IDA-KYSO]SXD_2017_p1\\data2017part1");
            File toFolderFile = folderRoot.toFile();
            if(toFolderFile.isDirectory()) {
                for(File file : Objects.requireNonNull(toFolderFile.listFiles())) {
                    if(file.isDirectory()) {
                        FileUtils.fixFileExcelFromFolder(file.getPath());
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kích hoạt CompressPDF Action !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }


    @PostMapping("api/pdf/fix-import-hoso")
    public ResponseEntity<JSONObject> fixFileExcelImportDsHoSo() throws IOException {
        try {
            Path folderRoot = Paths.get("E:\\SXD2017\\[IDA-KYSO]SXD_2017_p1\\data2017part1\\p1");
            File fileFromFolder = folderRoot.toFile();
            if(fileFromFolder.isDirectory()) {
                for(File file : Objects.requireNonNull(fileFromFolder.listFiles())) {
                    if(file.isDirectory()) {
                        FileUtils.fixFileExcelFromFolder(file.getPath());
                    }
                }
            }
            // lay data tracking
//            String pathFileTracking = "D:\\FIS\\Result Sở KHCN\\Tracking Deli.xlsx";
//            List<SKHCNTrackingData> data = new ArrayList<>();
//            try (FileInputStream fis = new FileInputStream(pathFileTracking);
//                 Workbook workbook = new XSSFWorkbook(fis)) {
//                Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
//                // Bỏ qua tiêu đề (giả định dòng đầu tiên là tiêu đề)
//                Iterator<Row> rowIterator = sheet.iterator();
//                if (rowIterator.hasNext()) {
//                    rowIterator.next();
//                }
//                while (rowIterator.hasNext()) {
//                    Row row = rowIterator.next();
//                    String hopSo = FileUtils.getStringValue(row.getCell(0));
//                    String hs = FileUtils.getStringValue(row.getCell(1));
//                    String tieuDe = FileUtils.getStringValue(row.getCell(2));
//                    String ghiChu = FileUtils.getStringValue(row.getCell(3));
//                    String thoiGian = FileUtils.getStringValue(row.getCell(4));
//                    String tenTep = FileUtils.getStringValue(row.getCell(5));
//                    String tep  = FileUtils.getStringValue(row.getCell(6));
//                    String toSo = FileUtils.getStringValue(row.getCell(7));
//                    int soTrang = (int) row.getCell(8).getNumericCellValue();
//
//                    if(hopSo != null && !hopSo.equals("Không xác định") && !tieuDe.equalsIgnoreCase("")) {
//                        SKHCNTrackingData d = new SKHCNTrackingData();
//                        String loai = FileUtils.getLoaiFromTenFile(tenTep, 2);
//                        d.setLoai(loai);
//                        d.setHopSo(hopSo);
//                        d.setGhiChu(ghiChu);
//                        d.setHoSoSo(hs);
//                        d.setTieuDeHoSo(tieuDe);
//                        d.setThoiGian(thoiGian);
//                        d.setTenTep(tenTep);
//                        d.setTep(tep);
//                        d.setToSo(toSo);
//                        d.setSoTo(String.valueOf(soTrang));
//                        data.add(d);
//                    }
//                }
//                System.out.println("size : " + data.size());
//                // In danh sách đối tượng ra console
//            for(int i = 0; i < data.size(); i++) {
//                SKHCNTrackingData d = data.get(i);
//                if(!d.getTep().equalsIgnoreCase("")) {
//                    System.out.println("tờ số " + d.getToSo() + " maHoSo = "+d.getTep().substring(0, d.getTep().lastIndexOf(".")));
//                }
//            }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            // end data tracking
//            if(toFolderFile.isDirectory()) {
//                for(File file : Objects.requireNonNull(toFolderFile.listFiles())) {
//                    if(file.isDirectory()) {
//                        FileUtils.fixFileExcelHoSoFromFolder(file.getPath(), data);
//                    }
//                }
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        JSONObject rep  = new JSONObject();
        rep.put("status", 200);
        rep.put("message", "Kích hoạt CompressPDF Action !!!");
        return ResponseEntity.ok((JSONObject) rep);
    }

    @PostMapping("/api/signPDFs")
    public ResponseEntity<JSONObject> signPDFs(
            @RequestParam(value = "xLocation", required = false) String xLocation,
            @RequestParam(value = "yLocation", required = false) String yLocation,
            @RequestParam(value = "height", required = false) Integer height,
            @RequestParam(value = "width", required = false) Integer width,
            @RequestParam(value = "fontSize", required = false) Integer fontSize,
            @RequestParam(value = "pageSign", required = false) Integer pageSign,
            @RequestParam(value = "showType", required = false) String showType,
            @RequestParam(value = "locationSign", required = false) String locationSign,
            @RequestParam(value = "signatureType", required = false) String signatureType,  // KY_SAOY; KY_SAOLUC; KY_TRICHSAO;
            @RequestParam(value = "signatureImg", required = false) MultipartFile signatureImg,
            @RequestParam(value = "signatureImgBase64", required = false) String signatureImgBase64,
            @RequestParam("file") MultipartFile uploadFile,
            HttpServletRequest request) {
        int st = 0;
        // kiểu hiển thị. 0,1,2 => 0 = hiển thị ảnh và mô tả, 1 chỉ hiển thị ảnh, 2 chỉ hiển thị mô tả.
        if(showType.equals("THONGTIN")) {
            st = 2;
        } else if(showType.equals("HINHANH")) {
            st = 1;
        }
        ConfigSignature configObject = SignHubUtill.generateConfigSignature(height, width, fontSize, xLocation, yLocation, pageSign, signatureImgBase64, signatureType, st, locationSign);

        JSONObject rep  = new JSONObject();
        String ext      = FilenameUtils.getExtension(uploadFile.getOriginalFilename());
        String fileName = FilenameUtils.getName(uploadFile.getOriginalFilename());
        Path pathSaveFileTmp = Paths.get(ConstantUtil.IDA_UPLOADFILE_FOLDER);
        BufferedOutputStream stream = null;
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        String file_tmp = randomUUIDString + "." + ext;

        try {
            // Bước 1: Save file zip vao thư mục upload
            String filePath = Paths.get(ConstantUtil.IDA_UPLOADFILE_FOLDER, file_tmp).toString();
            Files.createDirectories(pathSaveFileTmp);
            stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            stream.write(uploadFile.getBytes());
            stream.close();
            List<String> arr = new ArrayList<String>();
            if(ext != null && ext.equalsIgnoreCase("zip")) {
                String folder_tmp_path = SignHubUtill.unzipFile(filePath);
                arr = SignHub.signPDFFromFolder(folder_tmp_path, configObject);
                String zipFolder = ConstantUtil.IDA_ROOT_FOLDER + "/result";
                SignHubUtill.zipArrayResult(zipFolder, arr);
                SignHubUtill.deleteFileFromParentFolder(ConstantUtil.IDA_ROOT_FOLDER + "/sign");
            } else if(ext != null && ext.equalsIgnoreCase("pdf")) {
                String tmpFolder  = ConstantUtil.IDA_TMP_FOLDER;
                int soLuongFolder = SignHubUtill.countFolderFromParentFolder(tmpFolder);
                String nameFolder = tmpFolder+"/tmp_"+soLuongFolder;
                File dir = new File(nameFolder);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                UUID uuids = UUID.randomUUID();
                String randomUUIDStrings = uuids.toString();
                stream = new BufferedOutputStream(new FileOutputStream(new File(nameFolder+"/"+uploadFile.getName()+"_"+randomUUIDStrings+"."+ext)));
                stream.write(uploadFile.getBytes());
                stream.close();
                arr = SignHub.signPDFFromFolder(nameFolder, configObject);
                String zipFolder = ConstantUtil.IDA_ROOT_FOLDER + "/result";
                //zippppppppppp
                SignHubUtill.zipArrayResult(zipFolder, arr, fileName);
                SignHubUtill.deleteFileFromParentFolder(ConstantUtil.IDA_ROOT_FOLDER + "/sign");
            }

            rep.put("status", 200);
            rep.put("message", "Ký số thành công");
            rep.put("count", arr.size());
        } catch (IOException e) {
            e.printStackTrace();
            rep.put("status", 500);
            rep.put("message", "Ký số thất bại");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResponseEntity.ok((JSONObject) rep);
    }
    // Hàm lấy phần mở rộng của file
    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf(".");
        if (lastIndex > 0 && lastIndex < name.length() - 1) {
            return name.substring(lastIndex);
        }
        return ""; // Nếu không có phần mở rộng
    }
}
