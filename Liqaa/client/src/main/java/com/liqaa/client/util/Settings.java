package com.liqaa.client.util;

import com.liqaa.shared.models.entities.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Settings {
    private static final File CONFIG_FILE = new File("./files/config/user.properties");
    private static final Properties props = new Properties();

    static {
        ensureConfigFileExists();
        loadProperties();
    }

    // Ensure the config file and directories exist before using them
    private static void ensureConfigFileExists() {
        try {
            File configDir = CONFIG_FILE.getParentFile();
            if (configDir != null && !configDir.exists() && !configDir.mkdirs()) {
                System.err.println("Failed to create config directory: " + configDir.getAbsolutePath());
            }
            if (!CONFIG_FILE.exists() && !CONFIG_FILE.createNewFile()) {
                System.err.println("Failed to create config file: " + CONFIG_FILE.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load properties from file
    private static void loadProperties() {
        if (CONFIG_FILE.exists()) {
            try (InputStream input = new FileInputStream(CONFIG_FILE)) {
                props.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Save user credentials and info
    public static void saveUser(User user, boolean rememberPassword) {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.setProperty("phone", user.getPhoneNumber());
            props.setProperty("name", user.getDisplayName());
            props.setProperty("id", String.valueOf(user.getId()));

            if (rememberPassword) {
                props.setProperty("password", user.getPasswordHash());
            } else {
                props.remove("password");
            }

            // Save profile picture if available
            if (user.getProfilepicture() != null) {
                String photoPath = getUserPhotoPath(user.getId());
                savePhoto(user.getProfilepicture(), photoPath);
                props.setProperty("photoPath", photoPath);
            }

            props.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearCredentials() {
        props.remove("password");
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasSavedUser() {
        return CONFIG_FILE.exists() && props.containsKey("phone");
    }

    // Get saved user info including the profile picture
    public static User getSavedUser() {
        if (!hasSavedUser()) {
            return null;
        }

        try {
            User user = new User();
            user.setPhoneNumber(props.getProperty("phone"));
            user.setDisplayName(props.getProperty("name"));
            user.setPasswordHash(props.getProperty("password", ""));
            user.setId(Integer.parseInt(props.getProperty("id")));

            // Load the user's profile picture if available
            String photoPath = props.getProperty("photoPath");
            System.out.println("before in the settings.java: photoPath: " + photoPath);
            if (photoPath != null && new File(photoPath).exists()) {
                System.out.println("after in the settings: photoPath: " + photoPath);
                user.setProfilepicture(loadPhoto(photoPath));

            }

            return user;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generate a structured path for the user's profile picture
    private static String getUserPhotoPath(int userId) {
        return "./files/users/" + userId + "_photo.png";
    }

    // Save user photo to the generated path
    private static void savePhoto(byte[] photoData, String path) {
        try {
            File file = new File(path);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(photoData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load user photo from the stored path
    private static byte[] loadPhoto(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            return null;
        }
    }
}
