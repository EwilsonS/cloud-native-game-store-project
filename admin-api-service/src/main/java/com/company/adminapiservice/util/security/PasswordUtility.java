package com.company.adminapiservice.util.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This program takes a password String, applies the BCrypt algorithm, and then prints
 * the hashed String to the console. We can copy the value printed to the console and
 * paste it into a SQL script.
 */
public class PasswordUtility {

    public static void main(String[] args) {

        PasswordEncoder enc = new BCryptPasswordEncoder();

        String password = "password";

        String encodedPassword = enc.encode(password);

        System.out.println(encodedPassword);

    }
}
