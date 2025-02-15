package com.liqaa.server.controllers.services.implementations;

import com.liqaa.server.controllers.reposotories.implementations.UserImplementation;
import com.liqaa.server.controllers.services.interfaces.UserServicesInt;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.models.enums.CurrentStatus;
import com.liqaa.shared.models.enums.Gender;
import com.liqaa.shared.network.Client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class UserServicesImpl implements UserServicesInt {
    private static UserServicesImpl instance;

    private UserServicesImpl() {}

    public static UserServicesImpl getInstance() {
        if (instance == null) {
            instance = new UserServicesImpl();
        }
        return instance;
    }
        // user Authentication
        @Override
    public User signIn (String userPhone, String userPassword)
        {
              User user=new User();
              //updateUserMode
            UserImplementation.getUserImplementationobject().updateUserMode (userPhone,true);
            user= UserImplementation.getUserImplementationobject().getUserbyPhoneAndPassword(userPhone, userPassword);
            if(user==null)
            {
                System.out.println("Wrong Phone or password");
                return null;
            }
              //do not retrun user's password
             //user.setPasswordHash("0");
              return user ;
        }
    @Override
        public boolean logout(String userPhone)
        {
            return UserImplementation.getUserImplementationobject().updateUserMode(userPhone,false);
        }
    @Override
        public User signUp(User user)
        {
            System.out.println("User before in sign up: "+user);

            if(UserImplementation.getUserImplementationobject().insertNewUser(user))
            {
                System.out.println("User aftter in sign up: "+user);

                User userresult= UserImplementation.getUserImplementationobject().getUserbyPhone(user.getPhoneNumber());
                //do not retrun user's password
//                userresult.setPasswordHash(null);
//                registerClient(Client.getInstance(),userresult);
                System.out.println("User registered successfully"+ userresult);
                return userresult;
            }
            return null;
        }

        @Override
       public User getUserInfo(String userPhone)
        {
            //User getUserbyPhone(String userPhone);
            User user=new User();
            user= UserImplementation.getUserImplementationobject().getUserbyPhone(userPhone);
            user.setPasswordHash(null); //do not return pass value
            if(user==null)
            {
                return null;
            }
            return user;
        }
        @Override
    public User getUserInfoById(int userId)
    {
        User user=new User();
        user= UserImplementation.getUserImplementationobject().getUserById(userId);
        user.setPasswordHash(null); //do not return pass value
        if(user==null)
        {
            return null;
        }
        return user;
    }
        @Override
        public boolean updateUserInfo (User user)
        {
            //int id=UserImplementation.getUserImplementationobject().getIdByPhoneNumber(user.getPhoneNumber());
            //user.setId(id);
            return UserImplementation.getUserImplementationobject().updateUser(user);
        }
        @Override
        public boolean updateUserImage ( String phone, byte[] img)
        {
               return UserImplementation.getUserImplementationobject().updateUserImage(phone,img);
        }
        @Override
        public boolean deleteUser (int userId)
        {

            return UserImplementation.getUserImplementationobject().deleteUser(userId);
        }

        // user Statistics
      @Override
    public  int getNumberAllUsers()
        {
            return UserImplementation.getUserImplementationobject().getNumbersAllUsers();
        }
        @Override
        public  int getNumberAllMaleUsers()
        {
            return UserImplementation.getUserImplementationobject().getNumberAllMaleUsers();
        }
        @Override
        public  int getNumberAllFemaleUsers()
        {
            return UserImplementation.getUserImplementationobject().getNumberAllFemaleUsers();
        }
        @Override
        public  int getNumberAllOnlineUsers()
        {
            return UserImplementation.getUserImplementationobject().getNumberAllOnlineUsers();
        }
        @Override
        public  int getNumberAllOfflineUsers()
        {
            return UserImplementation.getUserImplementationobject().getNumberAllOfflineUsers();
        }
        @Override
        public  int getNumberAllCountryOfUsers()
        {
            return UserImplementation.getUserImplementationobject().getNumberAllCountryOfUsers();
        }
        @Override
        public   Map<String, Integer> getTopCountriesOfUsers()
        {
            Map<String, Integer> list = UserImplementation.getUserImplementationobject().getTopCountries();
            Map<String, Integer> sortedMap = list.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));
            return sortedMap;
        }
        @Override
        public List<User> getAllUsers()
        {
            List<User> list=UserImplementation.getUserImplementationobject().getAllUsers();
             return  list;
        }


    }


