package com.liqaa.server.controllers.reposotories.implementations;

import com.liqaa.server.controllers.reposotories.interfaces.GroupRepo;
import com.liqaa.server.util.DatabaseManager;
import com.liqaa.shared.models.entities.Group;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupRepoImpl implements GroupRepo {
    private static GroupRepoImpl instance;

    private GroupRepoImpl() {}

    public static synchronized GroupRepoImpl getInstance() {
        if (instance == null) {
            instance = new GroupRepoImpl();
        }
        return instance;
    }

    @Override
    public int createGroup(Group group) {
        int id = 0;
        String query = "INSERT INTO groups_ (name, image, description, created_by) VALUES (?, ?, ?, ?);";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (group == null) {
                System.err.println("Error creating group: Group is null");
                return 0;
            }

            statement.setString(1, group.getName());
            statement.setBytes(2, group.getImage());
            statement.setString(3, group.getDescription());
            statement.setInt(4, group.getCreatedBy());

            if (statement.executeUpdate() == 0)
                System.err.println("Error creating group: No rows affected");
            else {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        id = generatedKeys.getInt(1);
                    else
                        System.err.println("Error creating group: No ID returned");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating group, Failed to create statement: " + e.getMessage());
        }
        return id;
    }

    @Override
    public Group getGroup(int id) {
        if (id <= 0) {
            System.err.println("Error fetching group: wrong id");
            return null;
        }

        String query = "SELECT * FROM groups_ WHERE id = ?;";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                System.err.println("Error fetching group: No group found with id: " + id);
                return null;
            }

            String name = result.getString(2);
            byte[] image = result.getBytes(3);
            String description = result.getString(4);
            int createdBy = result.getInt(5);
            LocalDateTime createdAt = result.getTimestamp(6).toLocalDateTime();

            return new Group(id, name, image, description, createdBy, createdAt);
        } catch (SQLException e) {
            System.err.println("Error fetching group, Failed to create statement: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int getGroupOwnerId(int groupId)
    {
        String query = "SELECT created_by FROM groups_ WHERE id = ?;";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                System.err.println("Error fetching group: No group found with id: " + groupId);
                return 0;
            }

            return result.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error fetching group, Failed to create statement: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean updateGroup(Group group) {
        if (group == null || group.getId() <= 0) {
            System.err.println("Error updating group: Invalid group or ID");
            return false;
        }

        String query = "UPDATE groups_ SET name = ?, image = ?, description = ?, created_by = ? WHERE id = ?;";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, group.getName());
                statement.setBytes(2, group.getImage());
                statement.setString(3, group.getDescription());
                statement.setInt(4, group.getCreatedBy());
                statement.setInt(5, group.getId());

                if (statement.executeUpdate() == 0) {
                    System.err.println("Error updating group: No rows affected");
                    return false;
                }
                return true;
            }
         catch (SQLException e) {
            System.err.println("Error updating group_, Failed to create statement: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteGroup(int groupId) {
        if (groupId <= 0) {
            System.err.println("Error deleting group: Invalid group ID");
            return false;
        }
        String query = "DELETE FROM groups_ WHERE id = ?;";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);

            if (statement.executeUpdate() == 0) {
                System.err.println("Error deleting group: No rows affected");
                return false;
            }
            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Group> getGroups(Integer[] ids) {
        if (ids == null || ids.length == 0) {
            System.err.println("Error fetching groups: Invalid ids");
            return List.of();
        }

        String placeholders = String.join(",", Collections.nCopies(ids.length, "?"));
        String query = "SELECT * FROM groups_ WHERE id IN (" + placeholders + ");";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < ids.length; i++) {
                statement.setInt(i + 1, ids[i]);
            }

            ResultSet result = statement.executeQuery();
            List<Group> groups = new ArrayList<>();

            while (result.next()) {
                groups.add(new Group(result.getInt(1),
                        result.getString(2), result.getBytes(3),
                        result.getString(4), result.getInt(5),
                        result.getTimestamp(6).toLocalDateTime()));
            }

            if (groups.isEmpty()) {
                System.err.println("Error fetching groups: No groups found with ids: " + Arrays.toString(ids));
            }

            return groups;

        } catch (SQLException e) {
            System.err.println("Error fetching groups, Failed to create statement: " + e.getMessage());
        }
        return List.of();
    }

    public List<Group> getGroups(int userId)
    {
        String query = "SELECT g.* FROM groups_ g " +
                "JOIN conversations c ON g.id = c.group_id " +
                "JOIN conversationparticipants cp ON c.id = cp.conversation_id " +
                "WHERE cp.user_id = ? AND c.type = 'Group';";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            List<Group> groups = new ArrayList<>();

            while (result.next()) {
                groups.add(new Group(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getBytes("image"),
                        result.getString("description"),
                        result.getInt("created_by"),
                        result.getTimestamp("created_at").toLocalDateTime()
                ));
            }

            if (groups.isEmpty()) {
                System.err.println("No groups found for user ID: " + userId);
            }

            return groups;

        } catch (SQLException e) {
            System.err.println("Error fetching groups: " + e.getMessage());
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}