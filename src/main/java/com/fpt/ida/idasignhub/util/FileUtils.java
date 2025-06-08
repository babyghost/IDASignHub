package com.fpt.ida.idasignhub.util;

import com.fpt.ida.idasignhub.data.FileEntry;
import com.fpt.ida.idasignhub.data.FileEntryExportExcel;
import com.fpt.ida.idasignhub.data.FileEntryExportExcelVanBan;
import com.fpt.ida.idasignhub.data.SKHCNTrackingData;
import com.fpt.ida.idasignhub.data.model.MauImportVanBan;
import com.fpt.ida.idasignhub.sheduler.CountFileScheduler;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPFile;

import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
public class FileUtils {
    private static final String FTP_SERVER = "103.161.119.75";
    private static final String FTP_USER = "nhantv16";
    private static final String FTP_PASSWORD = "Abc@12345";
    private static final String REMOTE_DIRECTORY = "upload";

    // Kích thước A-series tính bằng mm
    private static final Map<String, double[]> PAGE_SIZES_MM = new LinkedHashMap<>();
    static {
        PAGE_SIZES_MM.put("A0", new double[]{841, 1189});
        PAGE_SIZES_MM.put("A1", new double[]{594, 841});
        PAGE_SIZES_MM.put("A2", new double[]{420, 594});
        PAGE_SIZES_MM.put("A3", new double[]{297, 420});
        PAGE_SIZES_MM.put("A4", new double[]{210, 297});
    }
    private static double ptToMm(double pt) {
        return pt * 25.4 / 72.0;
    }
    private static boolean isSize(double wMm, double hMm, double targetW, double targetH, double tolMm) {
        // xét cả hai chiều gốc và xoay 90°
        boolean matchNormal = Math.abs(wMm - targetW) <= tolMm && Math.abs(hMm - targetH) <= tolMm;
        boolean matchRotated = Math.abs(wMm - targetH) <= tolMm && Math.abs(hMm - targetW) <= tolMm;
        return matchNormal || matchRotated;
    }
    private static boolean matchesSize(double wMm, double hMm,
                                       double targetW, double targetH,
                                       double tolMm) {
        boolean normal  = Math.abs(wMm - targetW) <= tolMm && Math.abs(hMm - targetH) <= tolMm;
        boolean rotated = Math.abs(wMm - targetH) <= tolMm && Math.abs(hMm - targetW) <= tolMm;
        return normal || rotated;
    }
    static Logger log = LoggerFactory.getLogger(FileUtils.class.getName());
    private static final Path rootLocation = Paths.get("IDASignHubTool");

    public static String sanitizeFileName(String fileName) {
        String normalizedFileName = Normalizer.normalize(fileName, Normalizer.Form.NFC);
        return normalizedFileName.replaceAll("[^\\p{ASCII}]", "_");
    }

    public static String renameFile(Path folderPath, String fileName) {
        try {
            Path path = folderPath.resolve(fileName);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                StringJoiner time = new StringJoiner("-");
                LocalDateTime local = LocalDateTime.now();
                time.add(String.valueOf(local.getYear()));
                time.add(String.valueOf(local.getMonthValue()));
                time.add(String.valueOf(local.getDayOfMonth()));
                time.add(String.valueOf(local.getHour()));
                time.add(String.valueOf(local.getMinute()));
                time.add(String.valueOf(local.getSecond()));
                String[] fileDetail = fileName.split("\\.");
                fileName = fileDetail[0].concat(time.toString()).concat(".").concat(fileDetail[1]);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileName;
    }

    public static byte[] getFileAsBytesFromFolder(String inputFolderPath, String fileName) throws IOException {
        Path folderPath = rootLocation.resolve(inputFolderPath);
        log.debug("fileName = {}", fileName);
        try (Stream<Path> files = Files.walk(folderPath)) {
            Optional<Path> zipFilePath = files
                .filter(Files::isRegularFile)
                .filter(filePath -> filePath.getFileName().toString().toLowerCase().equals(fileName.toLowerCase()))
                .findFirst();

            if (zipFilePath.isPresent()) {
                Path zipPath = zipFilePath.get();
                log.info("download zip file found: " + zipPath.toString());
                return Files.readAllBytes(zipPath);
            } else {
                log.error("No file found in the specified folder");
            }
        }
        return new byte[0];
    }

    public static Map<String, String> checkFileNumberFromLocalFolderByCurrentDate() throws IOException {
        LocalDate currentDate = LocalDate.now();
        Path pathCountFile = rootLocation.resolve("tmp_downloads/json_data");
        int count = Objects.requireNonNull(pathCountFile.toFile().listFiles()).length;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter).replace("/", "_");
        Map<String, String> map = new HashMap<>();
        Path configPath = rootLocation.resolve("tmp_downloads/json_data/["+(count + 1)+"]data_" + formattedDate + ".json");

        if (!Files.exists(configPath)) {
            Files.createFile(configPath);
        }
        map.put("configPath", configPath.toAbsolutePath().toString());
        return map;
    }

    public static String getFileNameFromPath(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf("/");
        if (lastSlashIndex == -1) {
            lastSlashIndex = filePath.lastIndexOf("\\");
        }
        return filePath.substring(lastSlashIndex + 1);
    }
    public static String getExtentionFromFileName(String fileName) {
        int lastSlashIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastSlashIndex + 1);
    }

    // kiem tra ton tai cua file hoặc folder trong folder, type = 0 chi kiem tra file, type = 1 => cả file và folder
    public static boolean checkFileExistsRecursive(File folder, File fileToCheck) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (checkFileExistsRecursive(file, fileToCheck)) {
                        return true;
                    }
                } else if (file.getName().equals(fileToCheck.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getFileNameFromFileName(String fileName) throws IOException {
        int indexPath = fileName.lastIndexOf(".");
        return fileName.substring(0, indexPath);
    }

    public static boolean isFileExistsInFolder(String folderPath, String fileNameToCheck) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().equals(fileNameToCheck)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static void reNameFilePDF(File filee) throws IOException {
        if(filee.isDirectory()) {
            for(File file : Objects.requireNonNull(filee.listFiles())) {
                if(file.isDirectory()) {
                    reNameFilePDF(file);
                } else {
                    System.out.println("file : "+file.getName());
                    int extIndex     = file.getName().lastIndexOf(".");
                    String extString = file.getName().substring(extIndex+1);
                    if(extString.equalsIgnoreCase("pdf")) {
                        // Tạo tên file mới
                        String newFileName = file.getName().replace("000.00.18.", "").replace("000.00.07.", "");
                        String nnewFileName = newFileName;
                        if(newFileName.startsWith("H17")) {
                            System.out.println("b = " + newFileName);
                            nnewFileName =  newFileName.replace("H17", "H17.18");
                            System.out.println("bb = " + nnewFileName);
                        }
                        System.out.println("newFileName " + nnewFileName);
                        // Đường dẫn đầy đủ của file mới
                        File renamedFile = new File(file.getParent() + "\\" + nnewFileName);

                        // Đổi tên file
                        if (file.renameTo(renamedFile)) {
                            System.out.println("Đã đổi tên: " + file.getName() + " -> " + nnewFileName);
                        } else {
                            System.out.println("Không thể đổi tên file: " + file.getName());
                        }
                    }
                }
            }
        } else {
            int extIndex     = filee.getName().lastIndexOf(".");
            String extString = filee.getName().substring(extIndex+1);
            if(extString.equalsIgnoreCase("pdf")) {
                // Tạo tên file mới
                String newFileName = filee.getName().replace("000.00.08.", "").replace("000.00.07.", "");

                String nnewFileName = newFileName;

                if(newFileName.startsWith("H17")) {
                    System.out.println("a = " + nnewFileName);
                    nnewFileName = newFileName.replace("H17", "H17.18");
                    System.out.println("aa = " + nnewFileName);
                }
                System.out.println("newFileName " + nnewFileName);
                // Đường dẫn đầy đủ của file mới
                File renamedFile = new File(filee.getParent() + "\\" + nnewFileName);

                // Đổi tên file
                if (filee.renameTo(renamedFile)) {
                    System.out.println("Đã đổi tên: " + filee.getName() + " -> " + nnewFileName);
                } else {
                    System.out.println("Không thể đổi tên file: " + filee.getName());
                }
            }
        }

    }

    public static List<FileEntry> getFolders(File fileOrFolder) throws IOException {
        List<FileEntry> fileList = new ArrayList<>();
        if (fileOrFolder.isDirectory()) {
            File[] files = fileOrFolder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        FileEntry entry = new FileEntry();
                        entry.setFileName(f.getName());
                        entry.setFilePath(f.getPath());
                        entry.setFilePathOnServer(f.getPath());
                        fileList.add(entry);
                    }
                }
            }
        }
        return fileList;
    }
    public static List<FileEntry> getFilesRecursively(File fileOrFolder) throws IOException {
        List<FileEntry> fileList = new ArrayList<>();
        if (fileOrFolder.isDirectory()) {
            File[] files = fileOrFolder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        // Nếu là thư mục, gọi đệ quy
                        fileList.addAll(getFilesRecursively(f));
                    } else {
                        // Nếu là file, thêm vào danh sách
                        int extIndex     = f.getName().lastIndexOf(".");
                        String extString = f.getName().substring(extIndex+1);
                        if(extString.equalsIgnoreCase("zip")) {
                            // giải nén
                            File folderUnZip = FileUtils.unzipFile(f.getPath(), f.getParent());
                            fileList.addAll(getFilesRecursively(folderUnZip));
                            // xóa file nén
                            Files.delete(f.toPath());

                        } else if(extString.equalsIgnoreCase("pdf")) {
                            FileEntry entry = new FileEntry();
                            entry.setFileName(f.getName());
                            entry.setFilePath(f.getPath());
                            entry.setFilePathOnServer(f.getPath());
                            entry.setExtension(extString);
                            fileList.add(entry);
                        }
                    }
                }
            }
        } else {
            int extIndex     = fileOrFolder.getName().lastIndexOf(".");
            String extString = fileOrFolder.getName().substring(extIndex+1);
            FileEntry entry  = new FileEntry();
            entry.setFileName(fileOrFolder.getName());
            entry.setFilePath(fileOrFolder.getPath());
            entry.setFilePath(fileOrFolder.getPath());
            entry.setExtension(extString);
            fileList.add(entry);
        }
        return fileList;
    }
    public static Map<String, Integer> countPagesBySize(String pdfPath) throws IOException {
        // Khởi tạo map: chỉ A0–A4 và "Other"
        Map<String, Integer> counts = new LinkedHashMap<>();
        PAGE_SIZES_MM.keySet().forEach(k -> counts.put(k, 0));
        counts.put("Other", 0);

        try (PDDocument doc = PDDocument.load(new File(pdfPath))) {
            double tol = 5.0;  // sai số mm
            int total = doc.getNumberOfPages();
            for (int i = 0; i < total; i++) {
                PDRectangle media = doc.getPage(i).getMediaBox();
                double wMm = ptToMm(media.getWidth());
                double hMm = ptToMm(media.getHeight());
                boolean matched = false;
                for (Map.Entry<String, double[]> entry : PAGE_SIZES_MM.entrySet()) {
                    double[] dim = entry.getValue();
                    if (matchesSize(wMm, hMm, dim[0], dim[1], tol)) {
                        counts.put(entry.getKey(), counts.get(entry.getKey()) + 1);
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    counts.put("Other", counts.get("Other") + 1);
                }
            }
        }

        return counts;
    }
    public static int countPages(String pdfPath) {
        // PDDocument implements Closeable, dùng try-with-resources để tự đóng file
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file PDF: " + e.getMessage());
            return -1;
        }
    }
    public static File unzipFile(String filePath, String folderPath) {

        int soLuongFolder = SignHubUtill.countFolderFromParentFolder(folderPath);
        byte[] buffer = new byte[1024];
        File result = new File(folderPath);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(filePath))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(folderPath, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
                result = newFile;
            }
            zis.closeEntry();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
//    downloadFileFromFTP
public static void downloadFilesFromFTP(Path folderPath) {
    log.info("Starting scheduled FTP download...");

    FTPClient ftpClient = new FTPClient();
    List<FileEntry> downloadedFiles = new ArrayList<>();
    try {
        log.info("Connecting to FTP server at {}...", FTP_SERVER);
        ftpClient.connect(FTP_SERVER);

        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            log.warn("FTP server refused connection with reply code: {}", replyCode);
            return;
        }

        log.info("Logging in as user '{}'", FTP_USER);
        boolean loggedIn = ftpClient.login(FTP_USER, FTP_PASSWORD);
        if (!loggedIn) {
            log.error("Failed to log in to FTP server with provided credentials.");
            return;
        }
        log.info("Successfully logged in.");

        ftpClient.enterLocalPassiveMode();
        // Set connect timeout to 30 seconds
        ftpClient.setConnectTimeout(60000);
        // Set data timeout to 30 seconds
        ftpClient.setDataTimeout(60000);
        // Set control keep-alive timeout to 5 minutes
        ftpClient.setControlKeepAliveTimeout(600);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setFileTransferMode(FTPClient.BLOCK_TRANSFER_MODE);

        log.info("Set file type to binary and entered passive mode.");

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        downloadedFiles = downloadFileFromFtpServer(ftpClient, REMOTE_DIRECTORY, folderPath.toFile().getPath());
        log.info("End scheduled FTP download, download size is {} ", downloadedFiles.size());
    } catch (IOException e) {
        log.error("An error occurred during FTP operation: {}", e.getMessage(), e);
    } finally {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
                log.info("Disconnected from FTP server.");
            }
        } catch (IOException ex) {
            log.error("Error during FTP client logout/disconnect: {}", ex.getMessage(), ex);
        }
    }
}

private static List<FileEntry> downloadFileFromFtpServer(FTPClient ftpClient, String remoteDirPath, String localPath) throws IOException {
    log.info("Starting scheduled FTP download...");

    FTPFile[] ftpFiles = ftpClient.listFiles(remoteDirPath);
    List<FileEntry> listFiles = new ArrayList<>();
    log.debug("ftpFiles size: {}", ftpFiles.length);
    for (FTPFile fileFpt : ftpFiles) {
        log.debug("download file Name: {}", fileFpt.getName());
        // get file hoặc folder về
        String remoteFilePath = remoteDirPath + "/" + fileFpt.getName();
        String localFilePath = localPath + "/" + fileFpt.getName();
        File localFile = new File(localFilePath);
        if (fileFpt.isDirectory()) {
            if (!localFile.exists() && !localFile.mkdirs()) {
                log.error("Failed to create local directory: {}", localFilePath);
                continue;
            }
            log.info("Entering directory: {}", remoteFilePath);
            listFiles.addAll(downloadDirectory(ftpClient, remoteFilePath, localFilePath));
        } else {
            log.info("local {} ", localFile.getName());
            if (isFileAlreadyDownloaded(localFile)) {
                log.info("File already exists locally, skipping download: {}", localFilePath);
                continue;
            }

            log.info("Downloading file: {}", remoteFilePath);
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                if (ftpClient.retrieveFile(remoteFilePath, fos)) {
                    String relativePath = localFile.getAbsolutePath().substring(rootLocation.toAbsolutePath().toString().length() + 1);
                    // check input ext; pdf convert 2 png
                    String fileExt = localFile.getAbsolutePath().substring(localFile.getAbsolutePath().lastIndexOf(".") + 1);
                    FileEntry entry = new FileEntry();
                    entry.setFileName(localFile.getName());
                    entry.setFilePath(remoteFilePath);
                    entry.setExtension(fileExt);
                    entry.setFilePathOnServer(relativePath.replace("\\", "/"));
                    entry.setStatus("DONE");
                    listFiles.add(entry);
                    log.info("Successfully downloaded: {}", localFile.getName());
                } else {
                    log.warn("Failed : {}", localFile.getPath());
                }
            }
            ftpClient.deleteFile(remoteFilePath);
        }
    }
    return listFiles;
}
private static List<FileEntry> downloadDirectory(FTPClient ftpClient, String remoteDirPath, String localDirPath) throws IOException {
    List<FileEntry> downloadedFiles = new ArrayList<>();
    FTPFile[] ftpFiles = ftpClient.listFiles(remoteDirPath);

    for (FTPFile ftpFile : ftpFiles) {
        String remoteFilePath = remoteDirPath + "/" + ftpFile.getName();
        String localFilePath = localDirPath + "/" + ftpFile.getName();
        File localFile = new File(localFilePath);

        if (ftpFile.isDirectory()) {
            if (!localFile.exists() && !localFile.mkdirs()) {
                log.error("Failed to create local directory: {}", localFilePath);
                continue;
            }
            log.info("Entering directory: {}", remoteFilePath);
            downloadedFiles.addAll(downloadDirectory(ftpClient, remoteFilePath, localFilePath));
        } else {
            log.info("local {} ", localFile.getName());
            if (isFileAlreadyDownloaded(localFile)) {
                log.info("File already exists locally, skipping download: {}", localFilePath);
                continue;
            }

            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                if (ftpClient.retrieveFile(remoteFilePath, fos)) {
                    String relativePath = localFile.getAbsolutePath().substring(rootLocation.toAbsolutePath().toString().length() + 1);
                    String fileExt = localFile.getAbsolutePath().substring(localFile.getAbsolutePath().lastIndexOf(".") + 1);

                    FileEntry entry = new FileEntry();
                    entry.setFileName(localFile.getName());
                    entry.setFilePath(remoteFilePath);
                    entry.setFilePathOnServer(relativePath.replace("\\", "/"));
                    entry.setStatus("DONE");
                    downloadedFiles.add(entry);
                    log.info("Successfully downloadDirectory: {}", localFilePath);
                } else {
                    log.warn("Failed to download: {}", remoteFilePath);
                }
            }
        }
    }
    return downloadedFiles;
}
    public static void createFileAndFolder(String path) {
        File file = new File(path);
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    System.out.println("Thư mục cha đã được tạo: " + parentDir.getAbsolutePath());
                } else {
                    System.err.println("Không thể tạo thư mục cha.");
                    return;
                }
            }

            // Tạo tệp
            if (file.createNewFile()) {
                System.out.println("Tệp đã được tạo: " + file.getAbsolutePath());

                // Ghi nội dung mẫu vào tệp
//                try (FileWriter writer = new FileWriter(file)) {
//                    System.out.println("Nội dung đã được ghi vào tệp.");
//                }
            } else {
                System.out.println("Tệp đã tồn tại: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            // Xử lý lỗi I/O
            e.printStackTrace();
        }
    }


    private static String replaceDateText(String input) {
        // Định nghĩa regex cho định dạng ngày
        String regex = "\\s*(\\d{1,2}/\\d{1,2}/\\d{4})\\s*-.*";

        // Tạo Pattern và Matcher
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Nếu khớp, thay thế bằng ngày đầu tiên
        if (matcher.matches()) {
            return matcher.group(1).trim();
        }

        // Nếu không khớp, trả về input ban đầu
        return input;
    }
    private static String getFirstMatch(String text) {
        // Định nghĩa regex
        String regex = "\\b\\d+\\s*/\\s*[A-Z]+-[A-Z]+\\b";

        // Tạo Pattern và Matcher
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // Tìm kết quả đầu tiên
        if (matcher.find()) {
            return matcher.group(); // Trả về kết quả đầu tiên
        }

        return ""; // Không tìm thấy, trả về null
    }
    public static List<MauImportVanBan> getDataFromFolderExcel(String localFolderHopHoSo) throws IOException {
        File files = new File(localFolderHopHoSo);
        System.out.println("Starting countFileFromFolderRecursion...");
        List<MauImportVanBan> listFiles = new ArrayList<>();
        if(files.isDirectory()) {
            for (File fileFpt : Objects.requireNonNull(files.listFiles())) {
                System.out.println("download file Name: "+ fileFpt.getName());
                // get file hoặc folder về
                String localFilePath = fileFpt.getAbsolutePath();
                File localFile = new File(localFilePath);
                if (fileFpt.isDirectory()) {
                    System.out.println("Entering directory: "+ localFilePath);
                    listFiles.addAll(getDataFromFolderExcel(fileFpt.getAbsolutePath()));
                } else {
                    System.out.println("local " + localFile.getName());
                    System.out.println("Downloading file: "+  localFilePath);
                    String fileExt = localFile.getAbsolutePath().substring(localFile.getAbsolutePath().lastIndexOf(".") + 1);

                    int lastSeparatorIndex = localFile.getAbsolutePath().lastIndexOf('/'); // For Unix-style paths
                    if (lastSeparatorIndex == -1) { // Handle Windows-style paths
                        lastSeparatorIndex = localFile.getAbsolutePath().lastIndexOf('\\');
                    }
                    // Extract the file name
                    String fileName = localFile.getAbsolutePath().substring(lastSeparatorIndex + 1);
                    if (fileExt.equalsIgnoreCase("xlsx") && !fileName.contains("DanhSachHoSo") && !fileName.contains("~$")) {
                        try (FileInputStream fis = new FileInputStream(localFile);
                             Workbook workbook = new XSSFWorkbook(fis)) {

                            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
                            List<MauImportVanBan> data = new ArrayList<>();

                            // Bỏ qua tiêu đề (giả định dòng đầu tiên là tiêu đề)
                            Iterator<Row> rowIterator = sheet.iterator();
                            if (rowIterator.hasNext()) {
                                rowIterator.next();
                            }

                            while (rowIterator.hasNext()) {
                                Row row = rowIterator.next();

                                String soTo = FileUtils.getStringValue(row.getCell(0));
                                String loai = FileUtils.getStringValue(row.getCell(1));
                                String tenVanBan = FileUtils.getStringValue(row.getCell(2));
                                String ngayThang = FileUtils.getStringValue(row.getCell(3));
                                String tacGia = FileUtils.getStringValue(row.getCell(4));
                                String trichYeu = FileUtils.getStringValue(row.getCell(5));
//                                int toSo = (int) row.getCell(6).getNumericCellValue();
                                String toSo = FileUtils.getStringValue(row.getCell(6));
                                String duongDanMoi = FileUtils.getStringValue(row.getCell(7));
                                String doMat = "Không mật";
                                String mucDoTinCay = "Bản chính";
                                String tinhTrangVatLy = "Bình thường";

                                MauImportVanBan d = new MauImportVanBan();
                                d.setLoai(loai);
                                d.setTuTo(toSo);
                                d.setDenTo(toSo);
                                d.setSoTrang(soTo);
                                d.setTinhTrangVatLy(tinhTrangVatLy);
                                d.setMucDoTinCay(mucDoTinCay);
                                d.setDoMat(doMat);
                                d.setDuongDan(duongDanMoi);
                                d.setTrichYeu(trichYeu);
                                d.setTacGiaVanBan(tacGia);
                                d.setSoKyHieuVanBan(tenVanBan);
                                d.setNgayThangNamVanBan(ngayThang);
                                data.add(d);
                            }

                            // In danh sách đối tượng ra console
                            for(int i = 0; i < data.size(); i++) {
                                MauImportVanBan d = data.get(i);
                                System.out.println("loai " + d.getLoai() + " maHoSo = "+d.getLoai());
                            }
                            if(data.size() > 0) {
                                listFiles.addAll(data);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            System.out.println("local " + localFolderHopHoSo);
            System.out.println("Downloading file: "+  localFolderHopHoSo);
            String fileExt = localFolderHopHoSo.substring(localFolderHopHoSo.lastIndexOf(".") + 1);

            int lastSeparatorIndex = localFolderHopHoSo.lastIndexOf('/'); // For Unix-style paths
            if (lastSeparatorIndex == -1) { // Handle Windows-style paths
                lastSeparatorIndex = localFolderHopHoSo.lastIndexOf('\\');
            }
            // Extract the file name
            String fileName = localFolderHopHoSo.substring(lastSeparatorIndex + 1);
            if (fileExt.equalsIgnoreCase("xlsx") && !fileName.contains("DanhSachHoSo") && !fileName.contains("~$")) {
                try (FileInputStream fis = new FileInputStream(localFolderHopHoSo);
                     Workbook workbook = new XSSFWorkbook(fis)) {

                    Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
                    List<MauImportVanBan> data = new ArrayList<>();

                    // Bỏ qua tiêu đề (giả định dòng đầu tiên là tiêu đề)
                    Iterator<Row> rowIterator = sheet.iterator();
                    if (rowIterator.hasNext()) {
                        rowIterator.next();
                    }

                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        String soTo = FileUtils.getStringValue(row.getCell(0));
                        String loai = FileUtils.getStringValue(row.getCell(1));
                        String tenVanBan = FileUtils.getStringValue(row.getCell(2));
                        String ngayThang = FileUtils.getStringValue(row.getCell(3));
                        String tacGia = FileUtils.getStringValue(row.getCell(4));
                        String trichYeu = FileUtils.getStringValue(row.getCell(5));
                        String toSo = FileUtils.getStringValue(row.getCell(6));
                        String duongDanMoi = FileUtils.getStringValue(row.getCell(7));
                        String doMat = "Không mật";
                        String mucDoTinCay = "Bản chính";
                        String tinhTrangVatLy = "Bình thường";

                        MauImportVanBan d = new MauImportVanBan();
                        d.setLoai(loai);
                        d.setTuTo(toSo);
                        d.setDenTo(toSo);
                        d.setSoTrang(soTo);
                        d.setTinhTrangVatLy(tinhTrangVatLy);
                        d.setMucDoTinCay(mucDoTinCay);
                        d.setDoMat(doMat);
                        d.setDuongDan(duongDanMoi);
                        d.setTrichYeu(trichYeu);
                        d.setTacGiaVanBan(tacGia);
                        d.setSoKyHieuVanBan(tenVanBan);
                        d.setNgayThangNamVanBan(ngayThang);
                        data.add(d);
                    }
                    if(data.size() > 0) {
                        listFiles.addAll(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return listFiles;
    }

    public static void fixFileExcelFromFolder(String localFolderHopHoSo) throws IOException {
        File files = new File(localFolderHopHoSo);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        if(files.isDirectory()) {
            // truy cap vao hop hoso ex: 2419.6646
            for(File file : files.listFiles()) {
                if(file.getName().substring(file.getName().lastIndexOf(".")).equalsIgnoreCase(".xlsx") && !file.getName().contains("_maumoi") && !file.getName().contains("~$")) {
//                    System.out.println(file.getAbsolutePath());
                    String oldName = file.getName().substring(0, file.getName().lastIndexOf("."));
                    String newName = oldName + "-" +dateFormat.format(new Date()) + "-maumoi.xlsx";

                    try (FileInputStream fis = new FileInputStream(file);
                             Workbook workbook = new XSSFWorkbook(fis)) {
                            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
                             List<MauImportVanBan> vanBans = new ArrayList<>();
                            // Bỏ qua tiêu đề (giả định dòng đầu tiên là tiêu đề)
                            Iterator<Row> rowIterator = sheet.iterator();
                            if (rowIterator.hasNext()) {
                                rowIterator.next();
                            }
                            while (rowIterator.hasNext()) {
                                Row row = rowIterator.next();
                                String soTo = FileUtils.getStringValue(row.getCell(0));
                                String loai = FileUtils.getStringValue(row.getCell(1));
                                String tenVanBan = FileUtils.getStringValue(row.getCell(2));
                                String ngayThang = FileUtils.getStringValue(row.getCell(3));
                                String tacGia = FileUtils.getStringValue(row.getCell(4));
                                String trichYeu = FileUtils.getStringValue(row.getCell(5));
                                String toSo  = FileUtils.getStringValue(row.getCell(6));
                                if(toSo.equalsIgnoreCase("") || toSo.equalsIgnoreCase("Không xác định")) {
                                    toSo = "0";
                                }
                                String dungDanMoi = FileUtils.getStringValue(row.getCell(7));
                                String doMat = "Không mật";
                                String matDoTinCay = "Bản chính";
                                String tinhTrangVatLy = "Bình thường";


                                MauImportVanBan vb = new MauImportVanBan();
                                vb.setSoKyHieuVanBan(tenVanBan);
                                vb.setNgayThangNamVanBan(ngayThang);
                                vb.setTrichYeu(trichYeu);
                                vb.setTacGiaVanBan(tacGia);
                                vb.setTuTo(toSo);
                                vb.setDenTo(toSo);
                                vb.setLoai(loai);
                                vb.setDuongDan(dungDanMoi.replace("000.00.07.", ""));
                                vb.setDoMat(doMat);
                                vb.setMucDoTinCay(matDoTinCay);
                                vb.setTinhTrangVatLy(tinhTrangVatLy);
                                vb.setSoTrang(soTo);
                                vanBans.add(vb);
//                                System.out.println(vb.toString());
                            }
                            System.out.println(newName);
                            byte[] excelBytes = ExcelFileUtil.createFileImportVanBan(vanBans);

                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(excelBytes);

                            Workbook workbook2 = new XSSFWorkbook(byteArrayInputStream);
                            Path folderPath = file.toPath().getParent();
                            String fileReportName = folderPath.toString() + "/" +newName;

                            FileOutputStream fileOut = new FileOutputStream(fileReportName);
                            workbook2.write(fileOut);
                            fileOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        } else {
            if(files.getName().substring(files.getName().lastIndexOf(".")).equalsIgnoreCase(".pdf")) {
                System.out.println(files.getAbsolutePath());
            }
        }
    }

    public static void checkFileFromFolder(String localFolderHopHoSo, String rootFolderTo, String rootFolderFrom) throws IOException {
//        System.out.println("checkFileFromFolder = " + localFolderHopHoSo);
        File files = new File(localFolderHopHoSo);
        if(files.isDirectory()) {
            // truy cap vao hop hoso ex: 2419.6646
            for(File file : files.listFiles()) {
                if(file.isDirectory()) {
                    checkFileFromFolder(file.getAbsolutePath(), rootFolderTo, rootFolderFrom);
                } else {
                    if(file.getName().substring(file.getName().lastIndexOf(".")).equalsIgnoreCase(".pdf")) {

                        String path1 = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\")).replace("E:\\[IDA-KYSO]SXD_2017_p2\\data2017part2", "");
                        String pathFrom = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"));
                        String path2 = rootFolderFrom + path1;

                        try {
                            Path p = Paths.get(path2);
                            if(!Files.exists(p)) {
                                System.out.println("Nothing to copy from : "+p);
                            } else {
                                File dir = p.toFile();
                                if(dir.exists()) {
                                    System.out.println(dir.getName());
                                    copyFilesToFolder(dir.toPath(), Paths.get(pathFrom));
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } else {
            if(files.getName().substring(files.getName().lastIndexOf(".")).equalsIgnoreCase(".pdf")) {
                System.out.println(files.getAbsolutePath());
            }
        }
    }
    public static void copyFilesToFolder(Path sourceDir, Path destinationDir) {
        try {
            // Create the destination directory if it doesn't exist
            if (Files.exists(destinationDir)) {
                // Walk through the source directory
                Files.walk(sourceDir).forEach(sourcePath -> {
                    try {
                        Path destinationPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.err.println("Error copying file: " + sourcePath + " -> " + e.getMessage());
                    }
                });

                System.out.println("Folder copied successfully!");
            }
        } catch (IOException e) {
            System.err.println("Error occurred while copying folder: " + e.getMessage());
        }
    }
    public static void createFileExcelImportVanBan(String localFolderHopHoSo) throws IOException {
        File files = new File(localFolderHopHoSo);
        // truy cap vao hop hoso ex: 2419
        if(files.isDirectory()) {
            for(File file : files.listFiles()) {
                // truy cap vao hoso
                if(file.isDirectory()) {
                    // truy cap vao hop hoso ex: 2419.1123
                    List<MauImportVanBan> vanBans = new ArrayList<>();
                    List<MauImportVanBan> listVanBans = new ArrayList<>();
                    for(File f : file.listFiles()) {
                        String fileExt = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(".") + 1);
                        if (!fileExt.equalsIgnoreCase("pdf")) {
                            continue;
                        }
                        String soKyHieuVanBan = "";
                        String ngayThangNamVanBan = "";
                        String trichYeu = "";
                        String tacGia = "";
                        String tuTo = "";
                        String denTo = "";
                        String loai = "";
                        String duongDanMoi = "";
                        String doMat = "Không mật";
                        String mucDoTinCay = "Bản chính";
                        String trangThaiVatLy = "Bình thường";
                        String soTrang = String.valueOf(getNumberFilePDF(f.getAbsoluteFile()));

                        int lastSeparatorIndex = f.getAbsolutePath().lastIndexOf('/'); // For Unix-style paths
                        if (lastSeparatorIndex == -1) { // Handle Windows-style paths
                            lastSeparatorIndex = f.getAbsolutePath().lastIndexOf('\\');
                        }
                        // Extract the file name
                        duongDanMoi = f.getAbsolutePath().substring(lastSeparatorIndex + 1).replace("000.00.18.H17", "H17.18");

                        MauImportVanBan vb = new MauImportVanBan();
                        vb.setTuTo(tuTo);
                        vb.setNgayThangNamVanBan(ngayThangNamVanBan);
                        vb.setTrichYeu(replaceTextTrichYeu(trichYeu));
                        vb.setTacGiaVanBan(tacGia);
                        vb.setDenTo(denTo);
                        vb.setLoai(loai.replace("GPXD", "Giấy phép xây dựng").replace("GPCT", "Giấy phép cải tạo"));
                        vb.setDuongDan(duongDanMoi.replace("000.00.18.H17", "H17.18"));
                        vb.setDoMat(doMat);
                        vb.setMucDoTinCay(mucDoTinCay);
                        vb.setTinhTrangVatLy(trangThaiVatLy);
                        vb.setSoTrang(soTrang);
                        vanBans.add(vb);
                    }

                    if(!vanBans.isEmpty()) {
                        System.out.println("Writing downloaded file list to excel.");

                        String pathFileTracking = file.getName() + ".xlsx";

                        try (FileInputStream fis = new FileInputStream(file.getParent() + "/"+pathFileTracking);
                             Workbook workbook = new XSSFWorkbook(fis)) {
                            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
                            List<FileEntryExportExcelVanBan> data = new ArrayList<>();

                            // Bỏ qua tiêu đề (giả định dòng đầu tiên là tiêu đề)
                            Iterator<Row> rowIterator = sheet.iterator();
                            if (rowIterator.hasNext()) {
                                rowIterator.next();
                            }

                            while (rowIterator.hasNext()) {
                                Row row = rowIterator.next();

                                String soTo = FileUtils.getStringValue(row.getCell(0));
                                String loai = FileUtils.getStringValue(row.getCell(1));
                                String tenVanBan = FileUtils.getStringValue(row.getCell(2));
                                String ngayThang = FileUtils.getStringValue(row.getCell(3));
                                String tacGia   = FileUtils.getStringValue(row.getCell(4));
                                String trichYeu = FileUtils.getStringValue(row.getCell(5));
                                String toSo = FileUtils.getStringValue(row.getCell(6));
                                String duongDanMoi  = FileUtils.getStringValue(row.getCell(7));
                                FileEntryExportExcelVanBan vanBan = new FileEntryExportExcelVanBan();
                                vanBan.setSoTo(soTo);
                                vanBan.setLoai(loai.replace("GPXD", "Giấy phép xây dựng").replace("GPCT", "Giấy phép cải tạo"));
                                vanBan.setTacGia(tacGia);
                                vanBan.setTenVanBan(tenVanBan);
                                vanBan.setNgayThang(ngayThang);
                                vanBan.setTrichYeu(replaceTextTrichYeu(trichYeu));
                                vanBan.setToSo(toSo);
                                vanBan.setDuongDanMoi(duongDanMoi.replace("000.00.18.H17", "H17.18"));
                                data.add(vanBan);
                            }

                            // bước 2 xử lý list data
                            for(int i = 0; i < vanBans.size(); i++) {
                                MauImportVanBan object = FileUtils.mappingDataVanBanTracking(vanBans.get(i), data);
                                if(checkBiaHoSo(object.getDuongDan(), "H17")) {
                                    listVanBans.add(object);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            listVanBans = vanBans;
                        }

                        byte[] excelBytes = ExcelFileUtil.createFileImportVanBan(listVanBans);

                        // Tạo ByteArrayInputStream từ mảng byte
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(excelBytes);

                        // Mở workbook từ byte array
                        Workbook workbook = new XSSFWorkbook(byteArrayInputStream);
                        Path folderPath = file.toPath().getParent();

                        int lastSeparatorIndex = file.getAbsolutePath().lastIndexOf('/'); // For Unix-style paths
                        if (lastSeparatorIndex == -1) { // Handle Windows-style paths
                            lastSeparatorIndex = file.getAbsolutePath().lastIndexOf('\\');
                        }
//                        // Extract the file name
                        String nameFile = file.getAbsolutePath().substring(lastSeparatorIndex + 1);
//
                        String fileReportName = folderPath.toString() + "/" + nameFile + "_newww.xlsx";

                        // Ghi workbook vào file trên đĩa
                        FileOutputStream fileOut = new FileOutputStream(fileReportName);
                        workbook.write(fileOut);
                        fileOut.close();
                        workbook.close();
                    }
                }
            }

        }
    }

    public static List<FileEntryExportExcel> getFileEntryExportExcelFromFolderRecursion(String localPath) throws IOException {
        System.out.println("Starting countFileFromFolderRecursion...");
        List<FileEntryExportExcel> listFiles = new ArrayList<>();
        File files = new File(localPath);
        if(files.isDirectory()) {
            for (File fileFpt : Objects.requireNonNull(files.listFiles())) {
                System.out.println("download file Name: "+ fileFpt.getName());
                // get file hoặc folder về
                String localFilePath = fileFpt.getAbsolutePath();
                File localFile = new File(localFilePath);
                if (fileFpt.isDirectory()) {
                    System.out.println("Entering directory: "+ localFilePath);
                    listFiles.addAll(getFileEntryExportExcelFromFolderRecursion(fileFpt.getAbsolutePath()));
                } else {
                    System.out.println("local " + localFile.getName());
                    System.out.println("Downloading file: "+  localFilePath);
                    String fileExt = localFile.getAbsolutePath().substring(localFile.getAbsolutePath().lastIndexOf(".") + 1);

                    int lastSeparatorIndex = localFile.getAbsolutePath().lastIndexOf('/'); // For Unix-style paths
                    if (lastSeparatorIndex == -1) { // Handle Windows-style paths
                        lastSeparatorIndex = localFile.getAbsolutePath().lastIndexOf('\\');
                    }
                    // Extract the file name
                    String fileName = localFile.getAbsolutePath().substring(lastSeparatorIndex + 1);
                    if (fileExt.equalsIgnoreCase("pdf")) {
                        String hopSo = "";
                        String hoSoSo = "";
                        String tieuDeHoSo = "";
                        String ngayThangBDvaKT = "";
                        String soTo = "";
                        String thoiGianBaoQuan = "";
                        String ghiChu = "";
                        String nam = "2018";
                        String macoquan = "18";
                        // xử lý tên file, parent folder để lấy thông tin file cho sở XD
                        // bước 1: lọc file
                        if(fileName.contains("000.00.18.H17") || fileName.contains("000.00.07.H17")) {
                            nam = fileName.substring(14, 18);
                            macoquan = fileName.substring(6, 8);
                            continue;
                        }
                        // neu ko phải file thành phần thì mới xử lý
                        String parentFolder = localFile.getParent();
                        int nameFolderIndex = parentFolder.lastIndexOf('/');
                        if (nameFolderIndex == -1) { // Handle Windows-style paths
                            nameFolderIndex = parentFolder.lastIndexOf('\\');
                        }
                        String nameFolder = parentFolder.substring(nameFolderIndex + 1);
                        String[] arrHopSoAndHoSoSo = nameFolder.split("\\.");
                        hopSo = arrHopSoAndHoSoSo[0];
                        hoSoSo = nameFolder;

                        tieuDeHoSo = "000.00."+macoquan+".H17." +nam+ "." +arrHopSoAndHoSoSo[1];
                        //end
                        FileEntryExportExcel file = new FileEntryExportExcel();
                        file.setHopSo(hopSo);
                        file.setHoSoSo(hoSoSo);
                        file.setTieuDeHoSo(tieuDeHoSo);
                        file.setNgayThangBDvaKT(ngayThangBDvaKT);
                        file.setSoTo(soTo);
                        file.setThoiHanBaoQuan(thoiGianBaoQuan);
                        file.setGhiChu(ghiChu);
                        file.setNamHoSo(Integer.parseInt(nam));
                        listFiles.add(file);
                    }
                }
            }
        }
        return listFiles;
    }

    public static MauImportVanBan mappingDataVanBanTracking(MauImportVanBan obj,  List<FileEntryExportExcelVanBan> data) {
        if(obj.getDuongDan().contains("_part_")) {
            System.out.println("_part_ obj : "+obj.getDuongDan());
            String maHoSo = obj.getDuongDan().split("_part_")[0];
            List<FileEntryExportExcelVanBan> filteredObjects = new ArrayList<>();
            for(int i = 0 ; i < data.size(); i++) {
                if(data.get(i).getDuongDanMoi().contains(maHoSo)) {
                    filteredObjects.add(data.get(i));
                }
            }
            System.out.println("size trùng khớp A : "+filteredObjects.size());
            if(!filteredObjects.isEmpty()) {
                obj.setLoai(filteredObjects.get(0).getLoai());
                obj.setSoKyHieuVanBan(filteredObjects.get(0).getTenVanBan());
                obj.setNgayThangNamVanBan(filteredObjects.get(0).getNgayThang());
                obj.setTacGiaVanBan(filteredObjects.get(0).getTacGia());
                obj.setTrichYeu(filteredObjects.get(0).getTrichYeu());
                obj.setTuTo(filteredObjects.get(0).getToSo());
                obj.setDenTo(filteredObjects.get(0).getToSo());
            }
        } else {

            String mahoso = obj.getDuongDan().replace("000.00.18.H17", "H17.18").replace(".pdf", "");
            System.out.println("mahoso : "+mahoso);
            List<FileEntryExportExcelVanBan> filteredObjects = new ArrayList<>();
            for(int i = 0 ; i < data.size(); i++) {
                if(data.get(i).getDuongDanMoi().contains(mahoso)) {
                    filteredObjects.add(data.get(i));
                    System.out.println("trùng khớp : "+data.get(i).getDuongDanMoi().replace("000.00.18.H17", "H17.18"));
                }
            }
            System.out.println("size trùng khớp B : "+filteredObjects.size());
            if(!filteredObjects.isEmpty()) {
                obj.setLoai(filteredObjects.get(0).getLoai());
                obj.setSoKyHieuVanBan(filteredObjects.get(0).getTenVanBan());
                obj.setNgayThangNamVanBan(filteredObjects.get(0).getNgayThang());
                obj.setTacGiaVanBan(filteredObjects.get(0).getTacGia());
                obj.setTrichYeu(filteredObjects.get(0).getTrichYeu());
                obj.setTuTo(filteredObjects.get(0).getToSo());
                obj.setDenTo(filteredObjects.get(0).getToSo());
            }
        }
        return obj;
    }
    private static String convertMaHoSoFromText(String input) {
        return input.replace("_signed.pdf","").replace("_signed.pdf.pdf", "").trim();
    }
    public static String mappingDataToSoVanBanTracking(FileEntryExportExcelVanBan obj,  List<SKHCNTrackingData> data) {
      String txt = convertMaHoSoFromText(obj.getDuongDanMoi());
        for(int i = 0; i < data.size(); i++) {
            SKHCNTrackingData d = data.get(i);
            if(txt.equalsIgnoreCase(d.getTep())) {
                System.out.println("objsss "+d.getTep() + " txt "+txt);
                return d.getToSo();
            }
        }
        return "";
    }
    public static FileEntryExportExcel mappingDataTracking(FileEntryExportExcel obj,  List<SKHCNTrackingData> data) {
        List<SKHCNTrackingData> filteredObjects = data.stream()
                .filter(d -> Objects.equals(d.getHoSoSo().replace(" ",""), obj.getHoSoSo())).toList();
        if(!filteredObjects.isEmpty()) {
            obj.setNamHoSo(Integer.parseInt(filteredObjects.get(0).getTep().substring(14, 18)));
            obj.setTieuDeHoSo(filteredObjects.get(0).getTep().substring(0, filteredObjects.get(0).getTep().lastIndexOf(".")));
            obj.setNgayThangBDvaKT(filteredObjects.get(0).getThoiGian().replace("-", "\n"));
            obj.setSoTo(filteredObjects.get(0).getSoTo());
            obj.setThoiHanBaoQuan("50 năm");
            System.out.println(obj.toString());
        }
        return obj;
    }
    private static boolean checkBiaHoSo(String nameFile, String textSoSanh) {
        if(nameFile.contains(textSoSanh)) {
            return true;
        }
        return false;
    }
    private static String replaceTextTrichYeu(String text) {
        String regex = "\\d{3}\\.\\d{2}\\.\\d{2}\\.[A-Z]\\d{2}\\.\\d{4}\\.\\d{5}\\.\\d{2}\\s?-?";
        return text.replaceAll(regex, "");
    }
    private static int getNumberFilePDF(File file) {
        try (PDDocument document = PDDocument.load(file)) {
            return document.getNumberOfPages();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static boolean isFileAlreadyDownloaded(File file) {
        return file.exists();
    }

    public static String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Nếu là kiểu ngày tháng, định dạng lại
                    return cell.getDateCellValue().toString();
                } else {
                    // Nếu là số, chuyển sang chuỗi
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Xử lý công thức
                try {
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "Không xác định";
        }
    }
    public static String getLoaiFromTenFile(String text, int index) {
        StringBuilder result = new StringBuilder();
        // Sử dụng phương thức split để tách từ
        String[] words = text.split(" "); // Tách theo khoảng trắng

        // Kiểm tra số lượng từ có trong mảng
        if (words.length >= index) {
            // Lấy từ đầu đến từ thứ 2
            for (int i = 0; i < index; i++) {
                result.append(" ").append(words[i]);
            }
//            System.out.println("Kết quả: " + result);

        } else {
            System.out.println("Chuỗi không đủ 2 từ.");
        }
        if(result.toString().contains("Phiếu nhận")) {
            return "Phiếu nhận xét";
        }
        if(result.toString().contains("Phiếu cho")) {
            return "Phiếu cho điểm";
        }
        if(result.toString().contains("Giấy chứng")) {
            return "Giấy chứng nhận";
        }
        return result.toString();
    }
    public static String normalizeToForwardSlash(String windowsPath) {
        if (windowsPath == null) {
            return null;
        }
        // \\ trong regex là 1 backslash, vậy \\\\ để match thật 2 dấu \\ trong chuỗi Java literal
        return windowsPath.replaceAll("\\\\", "/");
    }
}
