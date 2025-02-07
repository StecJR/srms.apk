package com.stec.srms.util;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Sha256 {
    public static String hash(String password, String salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] byte_hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(byte_hash);
    }
}
