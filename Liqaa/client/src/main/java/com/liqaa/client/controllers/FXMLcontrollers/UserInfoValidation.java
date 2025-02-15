package com.liqaa.client.controllers.FXMLcontrollers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoValidation<T> {

    private static String regex;

    private static Pattern pattern;

    private static Matcher match;

    public static <E> boolean isNull(E value) {

        return (value == null);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {

        if (isNull(phoneNumber)) {
            return false;
        }

        regex = "^01[1205][0-9]{8}$";

        pattern = Pattern.compile(regex);

        match = pattern.matcher(phoneNumber);

        return match.find();

    }

    /**
     *
     * Name Regex include : Letters: A-Z, a-z ,space character ,Special
     * Characters: !@#$%^&*(), Underscores "_". it does not include >> numbers
     */
    public static boolean isVaildName(String userName) {
        if (isNull(userName)) {
            return false;
        }

        regex = "^[A-Za-z\\s\\W_]{1,25}$";

        pattern = Pattern.compile(regex);

        match = pattern.matcher(userName);

        return match.find();

    }

    /**
     * ^[A-Za-z]: Ensures the email starts with a letter. [\w.]+: Matches one or
     * more word characters (letters, digits, underscores) or dots (.).
     *
     * @gmail\.com: Matches the literal @gmail.com (escaped dot to match a
     * literal dot).
     */
    public static boolean isVaildEmail(String userEmail) {

        regex = "^[A-Za-z]\\w+@gmail.com$";

        pattern = Pattern.compile(regex);

        match = pattern.matcher(userEmail);

        return match.find();

    }

    /**
     * The password must contain at least: One letter
     * . One digit.
     * password must be at least 6 characters long.
     */
    public static boolean isValidPassword(String password) {

        regex ="^(?=.*[A-Za-z])(?=.*\\d).{6,}$";

        pattern = Pattern.compile(regex);

        match = pattern.matcher(password);

        return match.find();

    }

}
