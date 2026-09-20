package com.awesomengwin.kingfisher.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final SecretKey secretKey;

    public EncryptionService(@Value("${kingfisher.secret-key}") String secretKey) {
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);
        this.secretKey = new SecretKeySpec(secretKeyBytes, "AES");
    }

    public String encrypt(String plain) {
        try {
            byte[] iv = new byte[12];
            SECURE_RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));

            byte[] cipherText = cipher.doFinal(plain.getBytes());

            return Base64.getEncoder().encodeToString(
                    ByteBuffer.allocate(iv.length + cipherText.length)
                            .put(iv).put(cipherText).array());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encrypt", e);
        }
    }

    public String decrypt(String encrypted) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);
            ByteBuffer buf = ByteBuffer.wrap(encryptedBytes);

            byte[] iv = new byte[12];
            buf.get(iv);

            byte[] cipherText = new byte[buf.remaining()];
            buf.get(cipherText);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));

            return new String(cipher.doFinal(cipherText));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt", e);
        }
    }
}
