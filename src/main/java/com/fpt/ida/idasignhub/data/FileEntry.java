package com.fpt.ida.idasignhub.data;

public class FileEntry {
    private String fileName;
    private String extension;
    private String filePath;
    private String filePathOnServer;
    private String status;

    public FileEntry() {}
    public FileEntry(String status, String filePathOnServer, String filePath, String extension, String fileName) {
        this.status = status;
        this.filePathOnServer = filePathOnServer;
        this.filePath = filePath;
        this.extension = extension;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePathOnServer() {
        return filePathOnServer;
    }

    public void setFilePathOnServer(String filePathOnServer) {
        this.filePathOnServer = filePathOnServer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
