package com.csci5408.distributeddatabase.user;

import com.csci5408.distributeddatabase.analytics.UserAnalyticsConstants;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

public class Login {
    Logger logger = new Logger();
    public boolean loginuser(Scanner sc) throws IOException, NoSuchAlgorithmException {
        Scanner s = new Scanner(System.in);
        System.out.println("==================== Login here ====================");

        System.out.println("Enter your UserId:");
        String userid = s.nextLine();

        System.out.println("Enter your Password:");
        String password = toHexString(getSHA(s.nextLine()));

        System.out.println("==================== Answer the security questions ====================");

        System.out.println("What is your favourite colour:");
        String que1 = toHexString(getSHA(s.nextLine()));

        System.out.println("What is the name of your primary school:");
        String que2 = toHexString(getSHA(s.nextLine()));

        System.out.println("What is your born place:");
        String que3 = toHexString(getSHA(s.nextLine()));

        List<String> lines = Files.readAllLines(Path.of("user.txt"));
        boolean userchecker= false;
        for(String line: lines){
            String[] parts = line.split("\\|");
            String[] subparts = parts[1].split(":");
            String recordUserId = subparts[1].substring(1, subparts[1].length() - 1);

            subparts = parts[2].split(":");
            String recordPassword = subparts[1].substring(1, subparts[1].length() - 1);

            subparts = parts[3].split(":");
            String recordque1 = subparts[1].substring(1, subparts[1].length() - 1);

            subparts = parts[4].split(":");
            String recordque2 = subparts[1].substring(1, subparts[1].length() - 1);

            subparts = parts[5].split(":");
            String recordque3 = subparts[1].substring(1, subparts[1].length());

            if(recordUserId.equals(userid) && recordPassword.equals(password) && recordque1.equals(que1) && recordque2.equals(que2) && recordque3.equals(que3)){
                userchecker=true;
                System.out.println(userchecker);
            }
        }
        if(userchecker)
        {
            System.getProperties().setProperty(UserAnalyticsConstants.LOGGED_IN_USER_NAME, userid);
            System.err.println(System.getProperties());
            System.out.println("Login Successful.");
            System.out.println("Welcome to DDBMS");
            logger.generalLogger("User with id "+userid+" logged in at"+System.currentTimeMillis());
            return true;


        }
        else{
            System.out.println("Login Unsuccessful.");
            logger.generalLogger(userid+ "Made unsuccessful login attempt");
            Logger.eventLogger("Login failed");
            return false;

        }

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
