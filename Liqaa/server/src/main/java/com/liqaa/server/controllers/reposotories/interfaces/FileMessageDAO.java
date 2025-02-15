package com.liqaa.server.controllers.reposotories.interfaces;

import com.liqaa.shared.models.entities.FileMessage;

import java.nio.file.Path;
import java.util.List;

public interface FileMessageDAO {
    int save(FileMessage fileMessage);
    FileMessage findById(int id);
    List<FileMessage> findAll();
    void update(int messageId, Path filePath);
    void delete(int id);
}
