package com.csci5408.distributeddatabase.user;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class RegUser {

    Logger log = new Logger();
    public void registerUser() throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        FileWriter userreg = new FileWriter("user.txt", true);

        Scanner s = new Scanner(System.in);
        System.out.println("==================== Register here ====================");

        System.out.println("Enter your User Name:");
        String username = s.nextLine();

        System.out.println("Enter your UserId:");
        String userid = s.nextLine();

        System.out.println("Enter your Password:");
        String password = s.nextLine();

        System.out.println("What is your favourite colour:");
        String que1 = s.nextLine();

        System.out.println("What is the name of your primary school:");
        String que2 = s.nextLine();

        System.out.println("What is your born place:");
        String que3 = s.nextLine();

        String newuser = "";
        newuser = newuser +"\""+"UserName"+"\""+":"+"\""+username+"\""+"|";
        newuser = newuser +"\""+"UserID"+"\""+":"+"\""+userid+"\""+"|";
        newuser = newuser +"\""+"Password"+"\""+":"+"\""+toHexString(getSHA(password))+"\""+"|";
        newuser = newuser +"\""+"Question1"+"\""+":"+"\""+toHexString(getSHA(que1))+"\""+"|";
        newuser = newuser +"\""+"Question2"+"\""+":"+"\""+toHexString(getSHA(que2))+"\""+"|";
        newuser = newuser +"\""+"Question3"+"\""+":"+"\""+toHexString(getSHA(que3))+"|\n";

        userreg.append(newuser);
        userreg.flush();
        userreg.close();
        System.out.println("Registered successfully");
        log.generalLogger("New user registered with id "+userid);
        Logger.eventLogger("Event : registration");
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

}
