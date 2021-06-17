package com.hk.es;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAEncryptUtil {
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
    public static void main(String[] args) throws Exception {

        //加密字符串
        String message = "中国China中国China中国China中国China中国China中国China中国China";
        String priKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALopvYqo3zzjTTHYQf74QvYACQmD" +
                "eZEok4MkX5iE0HyD+Ba35BOjvx05Yvtm7EYuGgf4Ms2YdQiaUs/kNHUtntfWlHABZPrhLC89W4/7" +
                "9d9ubrt/KJe2uAzforCe+qpa/Hs1Pi/vL25Yzvs3VWK+LaOQrgdiOvxG+ofNP38fkp9ZAgMBAAEC" +
                "gYAb6KAKxpBZD3aGL3aNY+id+plgPL6f3WZn9a8DLX4X/KzXsw/VReoj1nOZXu+zAbxTT4lGO0Ho" +
                "XUJm+j5HBuTWEr11djTu5BxV52e/8QwAuvn8/y5SRjxDAxDZhjzFnMNA/r+8nugHfkZjcBAD3+ti" +
                "bBNQdkwHekmMY+K+KqYt0QJBAPn8biDJbwUvGpnpiZiZphZdvn/gDAl82UWnXXsKP2KEk9TN4CiA" +
                "nrFGI/r9KiSsFBnrT48aXmX5SI9xKgzySx0CQQC+pD+WLnKIInHLm1CMemBnB6KGxCSwXrxIWhnd" +
                "+Jp06ku+iKoRwl5oP2V9V6zw+CHcSBWplNUOp34aayLGRfRtAkBf7s1ca7DdLmxo8ERLeXtRTfxT" +
                "GmnEIlNG9alcFFyqs/H/UlpZcLJ1mVMpeIn5tMeqArFvW1EqlPWRhn36pejJAkBpjYtfXawwT9Ht" +
                "x7rbbM5/fSyxrVwbAvkAnlKAIity3F6/ye9QEewvkBpjQe4RJXGqA4dq82rbyET736HLYeQpAkEA" +
                "8J25wdmfk3qo4IZjDHbhXnnEOKhkrIZy3AeI6gQaplJoHPkeg4BaORHLqJUwNChm3sCLo60p0fqR" +
                "koahYqAehQ==";
        String pubKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6Kb2KqN88400x2EH++EL2AAkJg3mRKJODJF+Y" +
                "hNB8g/gWt+QTo78dOWL7ZuxGLhoH+DLNmHUImlLP5DR1LZ7X1pRwAWT64SwvPVuP+/Xfbm67fyiX" +
                "trgM36KwnvqqWvx7NT4v7y9uWM77N1Vivi2jkK4HYjr8RvqHzT9/H5KfWQIDAQAB";
        String messageEn = encrypt(message,pubKey);
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn,priKey);
        System.out.println("还原后的字符串为:" + messageDe);
    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }
    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

}

