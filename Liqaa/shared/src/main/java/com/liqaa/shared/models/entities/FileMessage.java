package com.liqaa.shared.models.entities;

import java.io.Serializable;

public class FileMessage implements Serializable {
    private int id;
    private int messageId;
    private String fileName;
    private int fileSize;
    private String filePath;

    // Constructor
    public FileMessage() {}

    public FileMessage(int id, int messageId, String fileName, int fileSize, String filePath) {
        this.id = id;
        this.messageId = messageId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }

    public FileMessage(int messageId, String name, long length, String filePath)
    {
        this.messageId = messageId;
        this.fileName = name;
        this.fileSize = (int) length;
        this.filePath = filePath;
    }

    public FileMessage(String name, long length, String absolutePath)
    {
        this.fileName = name;
        this.fileSize = (int) length;
        this.filePath = absolutePath;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "FileMessage{" +
                "id=" + id +
                ", messageId=" + messageId +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
