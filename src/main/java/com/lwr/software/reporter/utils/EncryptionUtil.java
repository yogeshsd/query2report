package com.lwr.software.reporter.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.lwr.software.reporter.DashboardConstants;

public class EncryptionUtil {
	
	public static String encrypt(String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(DashboardConstants.INIT_VECTOR.getBytes(DashboardConstants.ENCODING));
			SecretKeySpec skeySpec = new SecretKeySpec(DashboardConstants.INIT_KEY.getBytes(DashboardConstants.ENCODING), DashboardConstants.ALGORITHM);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(value.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(DashboardConstants.INIT_VECTOR.getBytes(DashboardConstants.ENCODING));
			SecretKeySpec skeySpec = new SecretKeySpec(DashboardConstants.INIT_KEY.getBytes(DashboardConstants.ENCODING), DashboardConstants.ALGORITHM);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
