package com.hk.es;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSASignatureUtil {
	/**
	 * 验签
	 * @param data 原文
	 * @param signData 密文
	 * @param publicKey 公钥
	 * @return
	 * @throws SignatureException
	 * @throws Exception
	 */
	public static boolean validSign(String data,String signData,String publicKey) throws SignatureException, Exception
	{
		//byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(publicKey.trim());
		byte[] keyBytes = Base64.getDecoder().decode(publicKey.trim());
		// 构造X509EncodedKeySpec对象  
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		// KEY_ALGORITHM 指定的加密算法  
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		// 取公钥匙对象  
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(pubKey);
		signature.update(data.getBytes("utf-8"));
		// 验证签名是否正常  
		//boolean ret = signature.verify((new BASE64Decoder()).decodeBuffer(signData.trim()));
		boolean ret = signature.verify(Base64.getDecoder().decode(signData.trim()));
		return ret;
	}
	
	public static String sign(String data,String privateKey) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException
	{
		//byte[] bytesKey = (new BASE64Decoder()).decodeBuffer(privateKey.trim());
		byte[] bytesKey = Base64.getDecoder().decode(privateKey.trim());
			 PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(bytesKey);
		     KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		     PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		     Signature signature = Signature.getInstance("MD5withRSA");
		     signature.initSign(priKey);
		     signature.update(data.getBytes("utf-8"));
		//String RSA = (new BASE64Encoder()).encodeBuffer(signature.sign());
		String RSA = Base64.getEncoder().encodeToString(signature.sign());
		return RSA;
	}

	
	public static void main(String[] args) throws Exception {
		String ss="中国China";
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
		String sign = sign(ss, priKey);
		System.out.println(sign);
		System.out.println(validSign(ss,sign,pubKey));
	}
}
