package com.unionpay.marcus.mycreditcarddemo.tools;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by marcus on 17/2/23.
 */

public class RSAUtils {
    private static RSAPublicKey publicKey = null;

    public static void loadPublicKey(String pubKey) {
        try {
            byte[] buffer = Base64.decode(pubKey, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encryptWithRSA(String plainData) throws Exception {
        if (publicKey == null) {
            throw new NullPointerException("encrypt PublicKey is null !");
        }

        Cipher cipher = null;
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 此处如果写成"RSA"加密出来的信息JAVA服务器无法解析
        // Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] output = cipher.doFinal(plainData.getBytes("utf-8"));
        // 必须先encode成 byte[]，再转成encodeToString，否则服务器解密会失败
        // byte[] encode = Base64.encode(output, Base64.DEFAULT);
        return Base64.encodeToString(output, Base64.DEFAULT);
    }


    public static String decryptWithRSA(String encryedData) throws Exception {
        if (publicKey == null) {
            throw new NullPointerException("decrypt PublicKey is null !");
        }

        Cipher cipher = null;
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 此处如果写成"RSA"解析的数据前多出来些乱码
        // Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] output = cipher.doFinal(Base64.decode(encryedData, Base64.DEFAULT));
        return new String(output);
    }
}
