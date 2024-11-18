package com.example.qingting.net.cyper;

import android.content.Context;

import com.example.qingting.R;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class AESUtils {
    private final static int KEY_RESOURCE_ID = R.raw.aes_key;
    private static SecretKey secretKey;
    // 加密
    public static String encrypt(Context context, String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, readBinaryPassword(context));
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // 解密
    public static String decrypt(Context context, String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, readBinaryPassword(context));
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }


    // 从 raw 目录读取二进制文件（例如密码文件）
    private static SecretKey readBinaryPassword(Context context) {
        if (secretKey != null) {
            return secretKey;
        }
        byte[] data = null;
        InputStream inputStream = null;
        try {
            // 获取 raw 目录下的文件输入流
            inputStream = context.getResources().openRawResource(KEY_RESOURCE_ID);
            // 获取文件长度
            int fileLength = inputStream.available();
            // 创建一个字节数组来存储密码
            data = new byte[fileLength];
            // 读取输入流中的数据
            inputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        secretKey = new SecretKeySpec(data, "AES");
        return secretKey;
    }


}
