/* 
	Query2Report Copyright (C) 2018  Yogesh Deshpande
	
	This file is part of Query2Report.
	
	Query2Report is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	Query2Report is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with Query2Report.  If not, see <http://www.gnu.org/licenses/>.
*/

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
	public static void main(String[] args) {
		System.out.println(EncryptionUtil.decrypt("fDnrWPQ3YEfyYLJxgmqbINlC25eDP835xvxxoVZqPbU="));
	}
}
