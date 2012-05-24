package org.slc.sli.dal.encrypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Encrypts/Decrypts data using AES
 * 
 * @author Ryan Farris <rfarris@wgen.net>
 * 
 */
public class AesCipher implements org.slc.sli.dal.encrypt.Cipher {
    
    private static final Logger LOG = LoggerFactory.getLogger(AesCipher.class);
    
    private ThreadLocal<Cipher> cachedCipher = new ThreadLocal<Cipher>();
    
    @Autowired
    CipherInitDataProvider initDataProvider;
    
    @Override
    public String encrypt(Object data) {
        if (data instanceof String) {
            return "ESTRING:" + encryptFromBytes(StringUtils.getBytesUtf8((String) data));
        } else {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(byteOutputStream);
            String type;
            try {
                if (data instanceof Boolean) {
                    dos.writeBoolean((Boolean) data);
                    type = "EBOOL:";
                } else if (data instanceof Integer) {
                    dos.writeInt((Integer) data);
                    type = "EINT:";
                } else if (data instanceof Long) {
                    dos.writeLong((Long) data);
                    type = "ELONG:";
                } else if (data instanceof Double) {
                    dos.writeDouble((Double) data);
                    type = "EDOUBLE:";
                } else {
                    throw new RuntimeException("Unsupported type: " + data.getClass().getCanonicalName());
                }
                dos.flush();
                dos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] bytes = byteOutputStream.toByteArray();
            return type + encryptFromBytes(bytes);
        }
    }
    
    @Override
    public Object decrypt(String data) {
        String[] splitData = org.apache.commons.lang3.StringUtils.split(data, ':');
        // String[] splitData = data.split(":");
        if (splitData.length != 2) {
            return null;
        } else {
            if (splitData[0].equals("ESTRING")) {
                return StringUtils.newStringUtf8(decryptToBytes(splitData[1]));
            } else if (splitData[0].equals("EBOOL")) {
                return decryptBinary(splitData[1], Boolean.class);
            } else if (splitData[0].equals("EINT")) {
                return decryptBinary(splitData[1], Integer.class);
            } else if (splitData[0].equals("ELONG")) {
                return decryptBinary(splitData[1], Long.class);
            } else if (splitData[0].equals("EDOUBLE")) {
                return decryptBinary(splitData[1], Double.class);
            } else {
                return null;
            }
        }
    }
    
    private Object decryptBinary(String data, Class<?> expectedType) {
        byte[] decoded = decryptToBytes(data);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(decoded));
        try {
            if (Boolean.class.equals(expectedType)) {
                return dis.readBoolean();
            } else if (Integer.class.equals(expectedType)) {
                return dis.readInt();
            } else if (Long.class.equals(expectedType)) {
                return dis.readLong();
            } else if (Double.class.equals(expectedType)) {
                return dis.readDouble();
            } else {
                throw new RuntimeException("Unsupported type: " + expectedType.getCanonicalName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                LOG.error("Unable to close DataInputStream!");
            }
        }
    }
    
    private String encryptFromBytes(byte[] data) {
        try {
            
            byte[] encrypted = buildEncryptCipher().doFinal(data);
            String encodedData = Base64.encodeBase64String(encrypted);
            return encodedData;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
    
    private byte[] decryptToBytes(String encodedData) {
        byte[] data = Base64.decodeBase64(encodedData);
        try {
            byte[] decrypted = buildDecryptCipher().doFinal(data);
            return decrypted;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Cipher buildEncryptCipher() {
        return buildCipher(Cipher.ENCRYPT_MODE);
    }
    
    private Cipher buildDecryptCipher() {
        return buildCipher(Cipher.DECRYPT_MODE);
    }
    
    /**
     * Builds a new cipher that can be used independently of other threads.
     * 
     * @param mode
     * @return
     */
    protected Cipher buildCipher(int mode) {
        
        CipherInitData initData = initDataProvider.getInitData();
        
        byte[] ivBytes;
        try {
            ivBytes = Hex.decodeHex(initData.getInitializationVector().toCharArray());
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
        
        IvParameterSpec ivspec = new IvParameterSpec(ivBytes);
        
        try {
            Cipher cipher = cachedCipher.get();
            if (cipher == null) {
                synchronized (this) {  // might be over-reacting, but we've been burned before...
                    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                    cipher.init(mode, initData.getSecretKey(), ivspec);
                    cachedCipher.set(cipher);
                }
            }

            return cipher;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
