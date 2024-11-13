package com.fpt.ida.idasignhub.sheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ida.idasignhub.data.PageSizeVM;
import com.fpt.ida.idasignhub.data.PageStatisticsVM;
import com.fpt.ida.idasignhub.util.ExcelGenerator;
import com.fpt.ida.idasignhub.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@EnableScheduling
@Component
public class CountFileScheduler {

    private static final float CM_TO_POINTS = 28.35f;
    private static final List<PageSizeVM> STANDARD_SIZES = new ArrayList<>();

    static {
        STANDARD_SIZES.add(new PageSizeVM("4A0", 168f, 171f, 237f, 241f));
        STANDARD_SIZES.add(new PageSizeVM("2A0", 118f, 121f, 168f, 172f));
        STANDARD_SIZES.add(new PageSizeVM("A0", 83f, 85f, 118f, 120f));
        STANDARD_SIZES.add(new PageSizeVM("A1", 58f, 60f, 83f, 85f));
        STANDARD_SIZES.add(new PageSizeVM("A2", 41f, 43f, 59f, 61f));
        STANDARD_SIZES.add(new PageSizeVM("A3", 29f, 31f, 41f, 43f));
        STANDARD_SIZES.add(new PageSizeVM("A4", 20f, 22f, 29f, 31f));
        STANDARD_SIZES.add(new PageSizeVM("A5", 14f, 16f, 20f, 22f));
    }

    private static final Path rootLocation = Paths.get("filestorage_thongke");

    @Scheduled(fixedDelay = 2 * 60 * 1000)
    public void countFilePDFFromFolder() throws IOException {
        System.out.println("Starting scheduled Count PDF page file...");
        List<FileEntry> downloadedFiles = new ArrayList<>();

        try {
            Path folderPath = rootLocation.resolve("tmp_downloads/file_data");
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            Path folderConfigPath = rootLocation.resolve("tmp_downloads/json_data");
            if (!Files.exists(folderConfigPath)) {
                Files.createDirectory(folderConfigPath);
            }
            // nếu có path khác thì sẽ thay thế cho path default ở đây



            Map<String, String> checkFileAndFolderLocal = FileUtils.checkFileNumberFromLocalFolderByCurrentDate();
            int checkNumberFileFromFolderFileData = Objects.requireNonNull(folderPath.toFile().listFiles()).length;
            if(checkNumberFileFromFolderFileData > 0) {
                downloadedFiles = countFileFromFolderRecursion(folderPath.toFile().getPath());

                System.out.println("Writing downloaded file list to JSON.");
                System.out.println("downloadedFiles = " + downloadedFiles.size());
                String dataJsonFile = checkFileAndFolderLocal != null
                        ? checkFileAndFolderLocal.get("configPath")
                        : folderPath.toAbsolutePath().toString() + "/data.json";
                writeFileListToJson(downloadedFiles, dataJsonFile);
            }
        } catch (IOException e) {
            System.out.println("An error occurred during operation: " + e.getMessage());
        }
    }

    private List<FileEntry> countFileFromFolderRecursion(String localPath) throws IOException {
        System.out.println("Starting countFileFromFolderRecursion...");
        List<FileEntry> listFiles = new ArrayList<>();
        File files = new File(localPath);
        if(files.isDirectory()) {
            for (File fileFpt : Objects.requireNonNull(files.listFiles())) {
                System.out.println("download file Name: "+ fileFpt.getName());
                // get file hoặc folder về
                String localFilePath = fileFpt.getAbsolutePath();
                File localFile = new File(localFilePath);
                if (fileFpt.isDirectory()) {
                    System.out.println("Entering directory: "+ localFilePath);
                    listFiles.addAll(countFileFromFolderRecursion(fileFpt.getAbsolutePath()));
                } else {
                    System.out.println("local " + localFile.getName());
                    System.out.println("Downloading file: "+  localFilePath);
                    String fileExt = localFile.getAbsolutePath().substring(localFile.getAbsolutePath().lastIndexOf(".") + 1);
                    if (fileExt.equalsIgnoreCase("pdf")) {
                        listFiles.add(new FileEntry(localFile.getName(), "", localFile.getPath(), "DONE"));
                    }
                }
            }
        }
        return listFiles;
    }


    private List<FileEntry> getListFileEntryFromJson(String configFilePath) throws IOException {
        try (FileReader reader = new FileReader(configFilePath, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            Type typeOfList = new TypeToken<List<FileEntry>>() {}.getType();
            return gson.fromJson(reader, typeOfList);
        }
    }

    private List<FileEntry> getListFileEntryFromJsonByNameFile(String nameFile) throws IOException {
        Path folderPath = rootLocation.resolve("tmp_downloads/json_data");
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        File[] jsonFiles = folderPath.toFile().listFiles();
        List<FileEntry> listFiles = new ArrayList<>();
        if (jsonFiles != null) {
            for (File file : jsonFiles) {
                try (FileReader readerJson = new FileReader(file, StandardCharsets.UTF_8)) {
                    Gson gson = new Gson();
                    Type typeOfList = new TypeToken<List<FileEntry>>() {}.getType();
                    List<FileEntry> files = gson.fromJson(readerJson, typeOfList);
                    if (listFiles != null && files != null) {
                        listFiles.addAll(files);
                    } else {
                        listFiles = files;
                    }
                }
            }
        }
        if (listFiles != null && listFiles.size() > 0) {
            return listFiles.stream().filter(x -> x.fileName.equals(nameFile)).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private void writeFileListToJson(List<FileEntry> fileEntries, String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File configFile = new File(configFilePath);
            if (!configFile.exists()) {
                if (configFile.createNewFile()) {
                    System.out.println("Successfully wrote file list to JSON.");
                } else {
                    System.out.println("Fail wrote file list to JSON.");
                }
            }
            if (!fileEntries.isEmpty()) {
                try (FileReader reader = new FileReader(configFilePath, StandardCharsets.UTF_8)) {
                    Gson gson = new Gson();
                    Type typeOfList = new TypeToken<List<FileEntry>>() {}.getType();
                    List<FileEntry> files = gson.fromJson(reader, typeOfList);
                    if (files != null) {
                        files.addAll(fileEntries);
                    } else {
                        files = fileEntries;
                    }
                    mapper.writeValue(configFile, files);
                }
            }
            // create report
            System.out.println("Create report excel ...");
            List<PageStatisticsVM> pageStatistics = new ArrayList<>();
            for (FileEntry fileEntry : fileEntries) {
                    Path path = Paths.get(fileEntry.getFilePath());
                System.out.println("path = " + path);
                PageStatisticsVM pageStatisticsVM = processPDFFile(path);
                pageStatistics.add(pageStatisticsVM);
            }

            byte[] excelBytes = ExcelGenerator.exportPageStatistics(pageStatistics);

            try {
                // Tạo ByteArrayInputStream từ mảng byte
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(excelBytes);

                // Mở workbook từ byte array
                Workbook workbook = new XSSFWorkbook(byteArrayInputStream);

                Path folderPath = rootLocation.resolve("tmp_downloads/result");
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

                System.out.println("File Excel đã được tạo thành công từ byte array!");
                // xoa du lieu trong folder tmp_downloads
                Path deletePath = rootLocation.resolve("tmp_downloads/file_data");
                // tạo lại folder để chứa file lượt count sau
                if (Files.exists(deletePath)) {
                    deleteDirectoryRecursion(deletePath.toFile());
                    Files.createDirectories(deletePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("End create report excel ...");
            // end create report
        } catch (IOException e) {
            System.out.println("Failed to write file list to JSON: {}"+ e.getMessage());
        }
    }

    private boolean isFileAlreadyDownloaded(File file) {
        return file.exists();
    }

    public static class FileEntry {
        private String fileName;
        private String filePath;
        private String filePathOnServer;
        private String status;

        public FileEntry(String fileName, String filePathOnServer, String filePath, String status) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.filePathOnServer = filePathOnServer;
            this.status = status;
        }

        public void setPathOnServer(String pathOnServer) {
            this.filePathOnServer = pathOnServer;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getFileStatus() {
            return status;
        }

        public String getFilePathOnServer() {
            return filePathOnServer;
        }
    }

    private PageStatisticsVM processPDFFile(Path pdfFile) throws IOException {

        String fileExt = pdfFile.toFile().getAbsolutePath().substring(pdfFile.toFile().getAbsolutePath().lastIndexOf(".") + 1);
        if (fileExt.equalsIgnoreCase("pdf")) {
            PageStatisticsVM statistics = new PageStatisticsVM();
            statistics.setFilePath(pdfFile.toString());
            try (PDDocument document = PDDocument.load(pdfFile.toFile())) {
                int totalPages = document.getNumberOfPages();
                statistics.setTotalPages((long) totalPages);
                long a0Count = 0, a1Count = 0, a2Count = 0, a3Count = 0, a4Count = 0, a5Count = 0, twoA0Count = 0, fourA0Count = 0, otherCount = 0;

                for (int i = 0; i < totalPages; i++) {
                    PDRectangle pageSize = document.getPage(i).getMediaBox();
                    float width = pageSize.getWidth() / CM_TO_POINTS;
                    float height = pageSize.getHeight() / CM_TO_POINTS;

                    if  (width > height) {
                        float temp = width;
                        width = height;
                        height = temp;
                    }

                    String pageFormat = determinePageFormat(width, height);
                    switch (pageFormat) {
                        case "4A0": fourA0Count++; break;
                        case "2A0": twoA0Count++; break;
                        case "A0": a0Count++; break;
                        case "A1": a1Count++; break;
                        case "A2": a2Count++; break;
                        case "A3": a3Count++; break;
                        case "A4": a4Count++; break;
                        case "A5": a5Count++; break;
                        default: otherCount++; break;
                    }
                }

                statistics.setTotalA0Pages(a0Count);
                statistics.setTotalA1Pages(a1Count);
                statistics.setTotalA2Pages(a2Count);
                statistics.setTotalA3Pages(a3Count);
                statistics.setTotalA4Pages(a4Count);
                statistics.setTotalA5Pages(a5Count);
                statistics.setOther(otherCount);
            }
            return statistics;
        }
       return null;
    }

    private String determinePageFormat(float width, float height) {
        for (PageSizeVM standardSize : STANDARD_SIZES) {
            boolean widthMatches = width >= standardSize.minWidth && width <= standardSize.maxWidth;
            boolean heightMathces = height >= standardSize.minHeight && height <= standardSize.maxHeight;

            if (widthMatches && heightMathces) {
                return standardSize.name;
            }
        }
        return "OTHER";
    }
    private void deleteDirectory(Path directory) throws IOException {
        try {
            Files.delete(directory);
        } catch(IOException e) {
            System.out.println("Failed to delete " + directory + ": " + e.getMessage());
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
