package com.liqaa.server.controllers.services.interfaces;
import com.liqaa.shared.models.entities.User;
import java.util.List;
import java.util.Map;

public interface UserServicesInt {
       // user Authentication

    public User signIn (String userPhone, String userPassword);//throws null
    public boolean logout(String userPhone);
    public User signUp(User user); // throws NULL exception if insertion failed

    //user info //do not return pass for instance  // hash password
    //public User getUserInfo(int userId);
    public User getUserInfo(String userPhone); //throws null exception if not found
    public User getUserInfoById(int userId); //throws null exception if not found
    public boolean updateUserInfo (User user);
    public boolean updateUserImage ( String phone, byte[] img);//throws null
    public boolean deleteUser ( int userId);
    //public boolean updateUserPassword (int userID, String password);

    // user Statistics
    public  int getNumberAllUsers();
    public  int getNumberAllMaleUsers();
    public  int getNumberAllFemaleUsers();
    public  int getNumberAllOnlineUsers();
    public  int getNumberAllOfflineUsers();
    public  int getNumberAllCountryOfUsers();
    public Map<String,Integer> getTopCountriesOfUsers();
    public List<User> getAllUsers();


}
