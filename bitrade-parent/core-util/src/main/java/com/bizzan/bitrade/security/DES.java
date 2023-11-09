package com.bizzan.bitrade.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {
    private static final String DES = "DES";

    public DES() {
    }

    public static String encrypt(String plainData, String key) {
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(1, generateKey(key));
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        } catch (NoSuchPaddingException var6) {
            var6.printStackTrace();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        try {
            byte[] buf = cipher.doFinal(plainData.getBytes());
            return Base64.encode(buf);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String secretData, String secretKey) throws Exception {
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(2, generateKey(secretKey));
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
            throw new Exception("NoSuchAlgorithmException", var6);
        } catch (NoSuchPaddingException var7) {
            var7.printStackTrace();
            throw new Exception("NoSuchPaddingException", var7);
        } catch (InvalidKeyException var8) {
            var8.printStackTrace();
            throw new Exception("InvalidKeyException", var8);
        }

        try {
            byte[] buf = cipher.doFinal(Base64.decode(secretData.toCharArray()));
            return new String(buf);
        } catch (IllegalBlockSizeException var4) {
            var4.printStackTrace();
            throw new Exception("IllegalBlockSizeException", var4);
        } catch (BadPaddingException var5) {
            var5.printStackTrace();
            throw new Exception("BadPaddingException", var5);
        }
    }

    private static SecretKey generateKey(String secretKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
        keyFactory.generateSecret(keySpec);
        return keyFactory.generateSecret(keySpec);
    }
}