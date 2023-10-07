package com.devmetrix.dstuart;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class MuleAES {
	private String _key;
	
	public MuleAES(String key) {
		_key = key;
	}

	public MuleAES() {
		_key = "";
	}
	
	public String encrypt(String value) throws Exception {
		return _encrypt(this._key, value, true);
	}
	
	public String encrypt(String value, boolean useSalt) throws Exception {
		return _encrypt(this._key, value, useSalt);
	}
	
    public String encrypt(String key, String value) throws Exception {
        return _encrypt(key, value, true);
    }

    public String encrypt(String key, String value, boolean useSalt) throws Exception {
        return _encrypt(key, value, useSalt);
    }

    private String _encrypt(String key, String value, boolean useSalt) throws Exception {
        byte[] salt;

        if (useSalt) {
            SecureRandom sr = SecureRandom.getInstanceStrong();
            salt = new byte[16];
            sr.nextBytes(salt);
        } else {
            salt = key.getBytes();
        }

        IvParameterSpec iv = new IvParameterSpec(salt);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes());

        if (useSalt) {
            byte[] final_enc = new byte[ salt.length + encrypted.length ];
            System.arraycopy(salt, 0, final_enc, 0, salt.length);
            System.arraycopy(encrypted, 0, final_enc, salt.length, encrypted.length);
            return Base64.getEncoder().encodeToString(final_enc);
        }

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String value) throws Exception {
		return _decrypt(this._key, value, true);
	}
	
	public String decrypt(String value, boolean useSalt) throws Exception {
		return _decrypt(this._key, value, useSalt);
	}
	
    public String decrypt(String key, String encrypted) throws Exception {
    	return _decrypt(key, encrypted, true);
    }

    public String decrypt(String key, String encrypted, boolean useSalt) throws Exception {
    	return _decrypt(key, encrypted, useSalt);
    }

    private String _decrypt(String key, String encrypted, boolean useSalt) throws Exception {
        String final_dec;
        IvParameterSpec iv;

        byte[] decoded = Base64.getDecoder().decode(encrypted);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

        if(useSalt) {
            byte[] salt = Arrays.copyOfRange(decoded, 0, 16);
            byte[] encdata = Arrays.copyOfRange(decoded, salt.length, decoded.length);

            iv = new IvParameterSpec(salt);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            final_dec = new String(cipher.doFinal(encdata));
            return final_dec;
        } else {
            iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            final_dec = new String(cipher.doFinal(decoded));
            return final_dec;
        }
    }
}
