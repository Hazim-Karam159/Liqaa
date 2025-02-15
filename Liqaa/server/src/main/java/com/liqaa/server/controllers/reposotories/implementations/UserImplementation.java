package com.liqaa.server.controllers.reposotories.implementations;

import com.liqaa.server.controllers.reposotories.interfaces.UserInterface;
import com.liqaa.shared.models.entities.User;
import com.liqaa.server.util.DatabaseManager;
import com.liqaa.shared.models.enums.CurrentStatus;
import com.liqaa.shared.models.enums.Gender;
import com.liqaa.shared.models.enums.FriendRequestStatus;

import java.sql.*;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.*;
import java.text.SimpleDateFormat;
public class UserImplementation implements UserInterface {

    private  static UserImplementation UserImplementationobject;
    private UserImplementation() {}
    public static UserImplementation getUserImplementationobject()
    {
        if( UserImplementationobject==null)
        {
            UserImplementationobject = new UserImplementation();
        }
        return UserImplementationobject;
    }
public  static void main(String args[])
{
    UserImplementation s=new UserImplementation();

    System.out.println(s.updateUserImage ("1234567890",s.getUserById(5).getProfilepicture()));
}
    @Override
   public boolean isPhoneNumberExists(String phoneNumber)
    {
        String query = "Select * from users where phone_number = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            phoneNumber = phoneNumber.trim(); // Normalize input
            statement.setString(1, phoneNumber);

            System.out.println("Checking phone: " + phoneNumber); // Debug print

            try (ResultSet result = statement.executeQuery()) {
                boolean exists = result.next();
                System.out.println("Exists? " + exists); // Debug print
                return exists;
            }
        } catch (SQLException e) {
            System.err.println("Database error in isPhoneNumberExists: " + e.getMessage());
        }
        
        return false;
    }


    @Override
    public boolean updateUserMode (String phoneNumber,boolean is_Active)
    {
        if (isPhoneNumberExists(phoneNumber)==true)
        {
            String query = "UPDATE users SET is_active = ? WHERE phone_number = ?";
            try (Connection connection = DatabaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                {
                    statement.setBoolean(1, is_Active);
                    statement.setString(2,phoneNumber);

                    if (statement.executeUpdate()==1)
                    {  //true: mode changed
                        return true;
                    }

                }
            }catch (SQLException e) {
                System.out.println("Error in isPhoneNumberExists ");
                e.printStackTrace(); // Handle or log the exception properly
            }
        }
            return false;

    }
    @Override
    public boolean isEmailExists(String email)
    {
        String query = "Select * from users where email = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            {
                statement.setString(1,email);
                ResultSet result = statement.executeQuery();
                if (result.next())
                {  return true; }

            }
        }catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception properly
        }
        return false; // Default to false if an error occurs
    }@Override
    public boolean insertNewUser(User user) {
        System.out.println("Attempting to insert user: " + user.getPhoneNumber());

        // Check if the phone number already exists
        if (isPhoneNumberExists(user.getPhoneNumber())) {
            System.out.println("User already exists! Skipping insertion.");
            return false;
        }

        // SQL query for inserting a new user
        String query = "INSERT INTO users (phone_number, name, email, profile_picture, password_hash, " +
                "gender, country, date_of_birth, bio, current_status, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (connection == null) {
                System.err.println("Database connection is NULL! Aborting insert.");
                return false;
            }

            // Set parameters
            statement.setString(1, user.getPhoneNumber());
            statement.setString(2, user.getDisplayName());
            statement.setString(3, user.getEmail());

            // Handle NULL profile picture
            if (user.getProfilepicture() != null) {
                statement.setBlob(4, new ByteArrayInputStream(user.getProfilepicture()));
            } else {
                statement.setNull(4, Types.BLOB);
            }

            statement.setString(5, user.getPasswordHash());

            // Ensure ENUM values are correctly set
            statement.setString(6, (user.getGender() != null) ? user.getGender().name() : null);
            statement.setString(7, user.getCountry());

            // Handle NULL date of birth
            if (user.getDateofBirth() != null) {
                statement.setDate(8, new java.sql.Date(user.getDateofBirth().getTime()));
            } else {
                statement.setNull(8, Types.DATE);
            }

            statement.setString(9, user.getBio());

            // Handle ENUM for `current_status`
            statement.setString(10, (user.getCurrentstatus() != null) ? user.getCurrentstatus().name() : null);

            statement.setBoolean(11, user.isActive());

            // Execute the query
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User inserted successfully!");
                return true;
            } else {
                System.out.println("Failed to insert user.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Database error in insertNewUser: " + e.getMessage());
            e.printStackTrace();
            return false; // Ensure failure is reflected
        }
    }


    @Override
    public boolean deleteUser (int userId)
    {
        boolean result= false;
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            {
                statement.setInt(1, userId);
                if(statement.executeUpdate()==1)
                    result= true;
            }
        }catch(SQLException e) {
            e.printStackTrace(); // Handle or log the exception properly
        }
        return result ;
    }


    @Override
    public boolean updateUser(User user)
    {
        boolean res=false;
        if (isPhoneNumberExists(user.getPhoneNumber()))
        {
            System.out.println("this user exists");
            //update
            String query = "UPDATE users SET  name = ?,  bio = ?," +
                    " current_status = ? ,profile_picture = ? WHERE id = ?";
            try (Connection connection = DatabaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                {

                    statement.setString(1, user.getDisplayName());
                    statement.setString(2, user.getBio());
                    statement.setString(3, user.getCurrentstatus().toString());
                    if (user.getProfilepicture() != null)
                    {
                        ByteArrayInputStream profilePhoto = new ByteArrayInputStream(user.getProfilepicture());
                        statement.setBlob(4, profilePhoto);
                    }
                    else {
                        statement.setNull(4, java.sql.Types.BLOB);
                    }
                    statement.setInt(5, user.getId());
                    if(statement.executeUpdate()==1)
                    {
                        return true;
                    }
                }
            }catch(SQLException e)
            {
                e.printStackTrace(); // Handle or log the exception properly
            }
        }
        else
        {
            System.out.println("this user does not exist");
        }
        return false;
    }
    @Override
    public boolean updateUserImage ( String phone, byte[] img)
    {
        boolean res=false;
        if (isPhoneNumberExists(phone))
        {
            System.out.println("this user exists");
            //update
            String query = "UPDATE users SET  profile_picture = ? WHERE phone_number = ?";
            try (Connection connection = DatabaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                {

                    ByteArrayInputStream profilePhoto = new ByteArrayInputStream(img);
                    statement.setBlob(1, profilePhoto);
                    statement.setString(2, phone);
                    if( statement.executeUpdate()==1)
                        res=true;
                }
            }catch(SQLException e) {
                e.printStackTrace(); // Handle or log the exception properly
            }

        }
        else
        {
            System.out.println("this user does not exist");

        }
        return res;

    }


    @Override
    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?";
        User user = new User();
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                user.setId(result.getInt("id"));
                user.setPhoneNumber(result.getString("phone_number"));
                user.setPasswordHash(result.getString("password_hash"));
                user.setEmail(result.getString("email"));
                user.setDisplayName(result.getString("name"));
                user.setGender(Gender.valueOf(result.getString("gender").toUpperCase()));
                user.setDateofBirth(result.getDate("date_of_birth") != null ? new java.sql.Date(result.getDate("date_of_birth").getTime()) : null);
                user.setCountry(result.getString("country"));
                user.setBio(result.getString("bio"));
                user.setIsActive(result.getBoolean("is_active"));
                user.setCurrentstatus(CurrentStatus.valueOf(result.getString("current_status").toUpperCase()));
                user.setCreatedAt(result.getTimestamp("created_at") != null ? LocalTime.from(result.getTimestamp("created_at").toLocalDateTime()) : null);
                Blob profilePhotoBlob = result.getBlob("profile_picture");
                if (profilePhotoBlob != null) {
                    int blobLength = (int) profilePhotoBlob.length();
                    byte[] profilePhotoBytes = profilePhotoBlob.getBytes(1, blobLength);
                    user.setProfilepicture(profilePhotoBytes);
                } else {
                    user.setProfilepicture(null);
                }
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
  @Override
    public User getUserbyPhone(String userPhone)
    {
        String query = "SELECT * FROM users WHERE phone_number = ?";
        User user = new User();
        if (isPhoneNumberExists(userPhone))
        {
            System.out.println("This user exists");
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setString(1, userPhone);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                user.setId(result.getInt("id"));
                user.setPhoneNumber(result.getString("phone_number"));
                user.setPasswordHash(result.getString("password_hash"));
                user.setEmail(result.getString("email"));
                user.setDisplayName(result.getString("name"));
                user.setGender(Gender.valueOf(result.getString("gender").toUpperCase()));
                user.setDateofBirth(result.getDate("date_of_birth") != null ? new java.sql.Date(result.getDate("date_of_birth").getTime()) : null);
                user.setCountry(result.getString("country"));
                user.setBio(result.getString("bio"));
                user.setIsActive(result.getBoolean("is_active"));
                user.setCurrentstatus(CurrentStatus.valueOf(result.getString("current_status").toUpperCase()));
                user.setCreatedAt(result.getTimestamp("created_at") != null ? LocalTime.from(result.getTimestamp("created_at").toLocalDateTime()) : null);
                Blob profilePhotoBlob = result.getBlob("profile_picture");
                if (profilePhotoBlob != null) {
                    int blobLength = (int) profilePhotoBlob.length();
                    byte[] profilePhotoBytes = profilePhotoBlob.getBytes(1, blobLength);
                    user.setProfilepicture(profilePhotoBytes);
                } else {
                    user.setProfilepicture(null);
                }
           return user;
            }
        }catch(SQLException e) {
            e.printStackTrace(); // Handle or log the exception properly
        }}
        return null;
    }
    @Override
    public int getIdByPhoneNumber(String phoneNumber)
    {
        String query = "SELECT * FROM users WHERE phone_number = ?";
       int userId = 0;
        if (isPhoneNumberExists(phoneNumber))
        {
            System.out.println("This user exists");

            try (Connection connection = DatabaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                {
                    statement.setString(1, phoneNumber);
                    ResultSet result = statement.executeQuery();
                    if (result.next())
                    {
                        userId = result.getInt("id");
                    }
                }
            }catch(SQLException e) {
            e.printStackTrace(); // Handle or log the exception properly
        }
        }
        return userId;
    }
    @Override
    public User getUserbyPhoneAndPassword(String userPhone, String userPassword)
    {
        User user=new User();
        if(isPhoneNumberExists(userPhone)) // Phone number valid
        {
            String query = "SELECT password_hash FROM users WHERE phone_number = ?";
            // check password
            try (Connection connection = DatabaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                {
                    statement.setString(1, userPhone);
                    ResultSet result = statement.executeQuery();
                    if (result.next())
                    {
                        if(result.getString("password_hash").equals(userPassword)) // Retrieve the password
                        {
                            System.out.println("This user exists");
                            user= getUserbyPhone(userPhone);
                            return user;
                        }
                    }
                }
            }catch(SQLException e) {
                e.printStackTrace(); // Handle or log the exception properly
            }
        }
            return null;
    }


    @Override
    public List<User> getAllUsers()
    {
        String query = "SELECT * FROM users";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet res = statement.executeQuery();
            List<User> users = new ArrayList<User>();
            while (res.next())
            {
                {
                    User user = new User();
                    user.setId(res.getInt("id"));
                    user.setPhoneNumber(res.getString("phone_number"));
                    user.setPasswordHash(res.getString("password_hash"));
                    user.setEmail(res.getString("email"));
                    user.setDisplayName(res.getString("name"));
                    user.setGender(Gender.valueOf(res.getString("gender").toUpperCase()));
                    user.setDateofBirth(res.getDate("date_of_birth") != null ? new java.sql.Date(res.getDate("date_of_birth").getTime()) : null);
                    user.setCountry(res.getString("country"));
                    user.setBio(res.getString("bio"));
                    user.setCurrentstatus(CurrentStatus.valueOf(res.getString("current_status").toUpperCase()));
                    user.setIsActive(res.getBoolean("is_active"));
                    user.setCreatedAt(res.getTimestamp("created_at")!= null ? LocalTime.from(res.getTimestamp("created_at").toLocalDateTime()) : null);
                    Blob profilePhotoBlob = res.getBlob("profile_picture");
                    if (profilePhotoBlob != null) {
                        int blobLength = (int) profilePhotoBlob.length();
                        byte[] profilePhotoBytes = profilePhotoBlob.getBytes(1, blobLength);
                        user.setProfilepicture(profilePhotoBytes);
                    } else {
                        user.setProfilepicture(null);
                    }

                    users.add(user);
                }
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Integer> getTopCountries()
    {
        Map<String, Integer> countries = new HashMap<>();
        String query = "SELECT country, COUNT(*) " +
                "AS user_count FROM users GROUP BY country ORDER BY user_count DESC LIMIT 5 ";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                String country = res.getString("country");
                int userCount = res.getInt("user_count");
                countries.put(country, userCount);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }
    public int getNumbersAllUsers() {
        String query = "SELECT COUNT(id) FROM users";
        int count = 0;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
@Override
        public int getNumberAllMaleUsers()
    {
        String query = "SELECT COUNT(id) FROM users WHERE gender = 'Male'";
        int count = 0;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    @Override
    public int getNumberAllFemaleUsers()
    {
        String query = "SELECT COUNT(id) FROM users WHERE gender = 'Female'";
        int count = 0;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    @Override
    public int getNumberAllOnlineUsers()
    {
        String query = "SELECT COUNT(id) FROM users WHERE is_active = '1' ";
        int count = 0;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            //check if the resultset has data
           while (resultSet.next()) {
                count = resultSet.getInt(1); // Get the count from the first column
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
   @Override
    public int getNumberAllOfflineUsers()
    {
        String query = "SELECT COUNT(id) FROM users WHERE is_active = '0'";
        int count = 0;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            //check if the resultset has data
            if (resultSet.next()) {
                count = resultSet.getInt(1); // Get the count from the first column
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;

    }
    @Override
    public int getNumberAllCountryOfUsers()
    {
        String query = "SELECT COUNT(DISTINCT country) AS country_count FROM users";
        int count = 0;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
           if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}