package com.example.sofsis.remindercustomer;

import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.opengl.ETC1.isValid;

/**
 * Created by SOFSIS on 03/07/2017.
 */

public class validation {
    private static final String PHONE_REGEX = "^[7-9][0-9]{9}$";
    public static boolean checkEmail(String email)
    {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }


    public static boolean checkPhoneNo(String phone)
    {

        return Patterns.PHONE.matcher(phone).matches();

    }

    public static boolean isPhoneValid(String phone) {

        try {
            Pattern patt = Pattern.compile(PHONE_REGEX);
            Matcher matcher = patt.matcher(phone);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }

    }

}
