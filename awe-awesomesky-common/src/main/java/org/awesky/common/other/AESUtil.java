package org.awesky.common.other;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * Created by h3ilang on 16/3/21.
 * AES加密解密
 */
public class AESUtil {

    private static final String AES = "AES";

    private static final String ENCODING_UTF8 = "UTF-8";

    private static final int AES_LENGTH = 128;

    private static final String KEY = "awesky_1.0";

    /**
     * 加密
     *
     * @param content �?要加密的内容
     * @return
     */
    public static String encrypt(String content) throws Exception {
            KeyGenerator kgen = KeyGenerator.getInstance(AES);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(KEY.getBytes());
            kgen.init(AES_LENGTH, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
            Cipher cipher = Cipher.getInstance(AES);// 创建密码�?
            byte[] byteContent = content.getBytes(ENCODING_UTF8);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始�?
            byte[] result = cipher.doFinal(byteContent);
            return new BASE64Encoder().encode(result); // 加密
    }

    /**解密
     * @param content  待解密内�?
     * @return
     */
    public static String decrypt(String content) throws Exception  {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(KEY.getBytes());
        kgen.init(AES_LENGTH, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
        Cipher cipher = Cipher.getInstance(AES);// 创建密码�?
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始�?
        byte[] encrypted1 = new BASE64Decoder().decodeBuffer(content);
        byte[] result = cipher.doFinal(encrypted1);
        return new String(result,ENCODING_UTF8); // 加密
    }

    public static void main(String[] args) throws Exception {
        System.out.println("args = [" + decrypt("zghndF4ccqtR26hGN+hx1pVrpqo12iOT3cH3GJVQOMTC/9J0qPNlOCylNn/yAf4j") + "]");
    }
}
