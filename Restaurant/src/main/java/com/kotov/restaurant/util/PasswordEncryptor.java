package com.kotov.restaurant.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {
    private static final Logger logger = LogManager.getLogger();
    private static final String HASH_FUNCTION = "SHA-256";
    private static final String SALT_KEY = "5hr8Uh32Hr";

    private PasswordEncryptor() {
    }

    public static String encrypt(String password) {
        StringBuilder hash = new StringBuilder();
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] saltBytes = SALT_KEY.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_FUNCTION);
            digest.update(saltBytes);
            byte[] resultBytes = digest.digest(passwordBytes);
            for (byte next : resultBytes) {
                hash.append(next);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.WARN, "Password wasn't encrypted.", e);
        }
        return hash.toString();
    }
}