package com.example.Blogging.Utility;

import com.example.Blogging.jwt.JwtConst;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final SecretKey secretKey = loadConstantKey(JwtConst.CONSTANT_KEY);

    // Load the constant key into a SecretKey object
    private static SecretKey loadConstantKey(String constantKey) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(constantKey);
            if (decodedKey.length != 16) { // AES-128 requires a 16-byte key
                throw new IllegalArgumentException("Invalid key length. Expected 16 bytes.");
            }
            return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            throw new RuntimeException("Error loading secret key: " + ex.getMessage(), ex);
        }
    }

    // Method to encrypt a String
    public static String encrypt(String input) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception ex) {
            throw new Exception("Encryption failed: " + ex.getMessage(), ex);
        }
    }

    // Method to decrypt a String
    public static String decrypt(String encryptedInput) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedInput);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception ex) {
            throw new Exception("Decryption failed: " + ex.getMessage(), ex);
        }
    }
}
