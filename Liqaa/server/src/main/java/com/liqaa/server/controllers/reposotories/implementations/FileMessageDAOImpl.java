package com.liqaa.server.controllers.reposotories.implementations;

import com.liqaa.server.controllers.reposotories.interfaces.FileMessageDAO;
import com.liqaa.shared.models.entities.FileMessage;
import com.liqaa.server.util.DatabaseManager;

import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileMessageDAOImpl implements FileMessageDAO {

    @Override
    public int save(FileMessage fileMessage) {
        String sql = "INSERT INTO FileMessages (message_id, name, size, path) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, fileMessage.getMessageId());
            stmt.setString(2, fileMessage.getFileName());
            stmt.setInt(3, fileMessage.getFileSize());
            stmt.setString(4, fileMessage.getFilePath());

            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public FileMessage findById(int id) {
        String sql = "SELECT * FROM FileMessages WHERE message_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("FileMessage found with ID: " + rs.getInt("id"));
                return new FileMessage(
                        rs.getInt("id"),
                        rs.getInt("message_id"),
                        rs.getString("name"),
                        rs.getInt("size"),
                        rs.getString("path")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FileMessage> findAll() {
        List<FileMessage> fileMessages = new ArrayList<>();
        String sql = "SELECT * FROM FileMessages";
        try (Connection connection = DatabaseManager.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                fileMessages.add(new FileMessage(
                        rs.getInt("id"),
                        rs.getInt("message_id"),
                        rs.getString("name"),
                        rs.getInt("size"),
                        rs.getString("path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileMessages;
    }

    @Override
    public void update(int messageId, Path filePath)
    {
        String path = filePath.toString();
        String sql = "UPDATE FileMessages SET path = ? WHERE message_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, path);
            stmt.setInt(2, messageId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM FileMessages WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deleted FileMessage with ID: " + id);
            } else {
                System.out.println("No FileMessage found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while deleting FileMessage with ID: " + id);
        }
    }

}