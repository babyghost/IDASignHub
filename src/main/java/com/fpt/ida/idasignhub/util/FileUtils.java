package com.fpt.ida.idasignhub.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class FileUtils {

    static Logger log = LoggerFactory.getLogger(FileUtils.class.getName());
    private static final Path rootLocation = Paths.get("filestorage_thongke");

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
}
