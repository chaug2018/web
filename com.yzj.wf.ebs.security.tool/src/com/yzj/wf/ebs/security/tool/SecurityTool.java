package com.yzj.wf.ebs.security.tool;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;

/**
 * 创建于:2012-11-27<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 简单的数据加解密工具
 * 
 * @author 陈林江
 * @version 1.0
 */
public class SecurityTool {

	private static String strDefaultKey = "1";
	public static int des = 1;
	public static int base64 = 2;

	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;

	private sun.misc.BASE64Encoder base64Encoder = null;
	private sun.misc.BASE64Decoder base64Decoder = null;
	private int securityType = 1;
	private String provideName = null;

	/**
	 * 指定密钥构造方法
	 * 
	 * @param securityType
	 *            加解密类型 请使用SecurityTool.des或SecurityTool.base64
	 * @throws Exception
	 */
	public SecurityTool(int securityType) throws Exception {
		this.securityType = securityType;
		if (SecurityTool.des == securityType) {
			com.sun.crypto.provider.SunJCE provider = new com.sun.crypto.provider.SunJCE();
			provideName = provider.getName();
			Security.addProvider(provider);
			Key key = getKey(strDefaultKey.getBytes());
			encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);
			decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
		} else if (SecurityTool.base64 == securityType) {
			base64Encoder = new sun.misc.BASE64Encoder();
			base64Decoder = new sun.misc.BASE64Decoder();
		} else {
			throw new Exception("不支持的数据加解密类型~~~~(>_<)~~~~ ");
		}
	}

	/**
	 * 加密字符串
	 * 
	 * @param string
	 *            需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public String encrypt(String string) throws Exception {
		if (this.securityType == SecurityTool.des) {
			return byteArr2HexStr(encrypt(string.getBytes("utf-8")));
		} else {
			return base64Encoder.encode(string.getBytes("utf-8"));
		}
	}

	/**
	 * 解密字符串
	 * 
	 * @param string
	 *            需解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public String decrypt(String string) throws Exception {
		if (this.securityType == SecurityTool.des) {
			return new String(decrypt(hexStr2ByteArr(string)));
		} else {
			return new String(base64Decoder.decodeBuffer(string), "utf-8");
		}
	}

	/**
	 * 销毁方法，释放资源
	 */
	public void destroy() {
		if (provideName != null) {
			Security.removeProvider(com.sun.crypto.provider.SunJCE.class
					.getName());
		}
	}

	/**
	 * 将byte数组转换为表示16进制值的字符串
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 * 
	 */
	private String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 * 
	 */
	private byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes("utf-8");
		int iLen = arrB.length;
		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/**
	 * 加密字节数组
	 * 
	 * @param arrB
	 *            需加密的字节数组
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	private byte[] encrypt(byte[] arrB) throws Exception {
		return encryptCipher.doFinal(arrB);
	}

	/**
	 * 解密字节数组
	 * 
	 * @param arrB
	 *            需解密的字节数组
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	private byte[] decrypt(byte[] arrB) throws Exception {
		return decryptCipher.doFinal(arrB);
	}

	/**
	 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
	 * 
	 * @param arrBTmp
	 *            构成该字符串的字节数组
	 * @return 生成的密钥
	 * @throws java.lang.Exception
	 */
	private Key getKey(byte[] arrBTmp) throws Exception {
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];
		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		// 生成密钥
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
		return key;
	}

}