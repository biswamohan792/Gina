package com.myHome.gina.Gina.utils;

import com.myHome.gina.Gina.constants.AuthConstants;
import com.myHome.gina.Gina.constants.FileConstants;
import com.myHome.gina.Gina.models.Service;
import com.myHome.gina.Gina.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;

public class AuthUtils {
    private static final String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";

    public static String getRandomId(int length){
        StringBuilder stringBuilder = new StringBuilder();
        while (length-->0)
            stringBuilder.append(ALPHABETS.charAt((int)Math.floor(Math.random()*ALPHABETS.length())));
        return stringBuilder.toString();
    }

    public static boolean checkPassword(String provided,String actual){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(provided, actual);
    }

    public static String hashPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static String jwtTokenGenerate(User user){
        // Set the expiration time for the token (e.g., 1 hour from now)
        long expirationTimeInMillis = System.currentTimeMillis() + 3600000*6; // 6 hour
        Date expirationDate = new Date(expirationTimeInMillis);

        // Build the JWT token

        return Jwts.builder()
                .setSubject(user.getUserId()) // You can use any unique identifier for the subject
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, AuthConstants.JWT_SECRET) // Replace "yourSecretKey" with a strong secret key
                .compact();
    }

    public static String jwtTokenGenerate(Service user){
        // Set the expiration time for the token (e.g., 1 hour from now)
        long expirationTimeInMillis = System.currentTimeMillis() + 3600000*6; // 6 hour
        Date expirationDate = new Date(expirationTimeInMillis);

        // Build the JWT token

        return Jwts.builder()
                .setSubject(user.getServiceId()) // You can use any unique identifier for the subject
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, AuthConstants.JWT_SECRET) // Replace "yourSecretKey" with a strong secret key
                .compact();
    }

    public static boolean checkJwtToken(String jwt){
        try {
            // Parse and validate the token
            Jwts.parser().setSigningKey(AuthConstants.JWT_SECRET).parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getIdFromJWTToken(String jwt){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(AuthConstants.JWT_SECRET)
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e){
            return null;
        }
    }

    public static boolean anyNull(Object ...objects){
        for(Object crawl:objects)
            if(Objects.isNull(crawl))
                return true;
        return false;
    }

    public static boolean deleteFile(String name){
        try{
            Files.deleteIfExists(Path.of(FileConstants.FILE_SAVE_PATH,name));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
