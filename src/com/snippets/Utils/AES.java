package com.snippets.Utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


public class AES
{
    private static String algorithm = "AES";
    private static byte[] keyValue = "mZLMrpR96CKzb7UjmQ2PpHbK".getBytes();

    // Performs Encryption
    public static String encrypt(String plainText) throws Exception
    {
        Key key = generateKey();
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = chiper.doFinal(plainText.getBytes());
        String encryptedValue = new String(Base64.encode(encVal, Base64.DEFAULT));
        return encryptedValue;
    }

    // Performs decryption
    public static String decrypt(String encryptedText) throws Exception
    {
        // generate key
        Key key = generateKey();
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] decValue = chiper.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    //generateKey() is used to generate a secret key for AES algorithm
    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(keyValue, algorithm);
        return key;
    }
}