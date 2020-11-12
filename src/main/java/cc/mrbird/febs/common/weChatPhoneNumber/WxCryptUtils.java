package cc.mrbird.febs.common.weChatPhoneNumber;

import cn.hutool.core.codec.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;


/**
 * @author yyb
 * @since 2018-01-10
 */
public class WxCryptUtils {

    /**
     * 小程序 数据解密
     *
     * @param encryptData 加密数据
     * @param iv          对称解密算法初始向量
     * @param sessionKey  对称解密秘钥
     * @return 解密数据
     */
    public static String decrypt(String encryptData, String iv, String sessionKey) {
        // 被加密的数据
        byte[] dataByte = cn.hutool.core.codec.Base64.decode(encryptData);
        // 加密秘钥
        byte[] keyByte = cn.hutool.core.codec.Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new String(resultByte, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//        try {
//            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
//            params.init(new IvParameterSpec(Base64.decodeBase64(iv)));
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(sessionKey), "AES"), params);
//
//            return new String(PKCS7Encoder.decode(cipher.doFinal(Base64.decodeBase64(encryptData))), StandardCharsets.UTF_8);
//        } catch (Exception e) {
//            throw new RuntimeException("AES解密失败", e);
//        }
    }

//    /**
//     * 数据加密
//     *
//     * @param data          需要加密的数据
//     * @param iv            对称加密算法初始向量
//     * @param sessionKey    对称加密秘钥
//     * @return  加密数据
//     */
    /*public static String encrypt(String data, String iv, String sessionKey) throws Exception {
        AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("AES");
        algorithmParameters.init(new IvParameterSpec(Base64.decodeBase64(iv)));
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(sessionKey), "AES"), algorithmParameters);
        byte[] textBytes = json.getBytes(StandardCharsets.UTF_8);
        ByteGroup byteGroup= new ByteGroup();
        byteGroup.addBytes(textBytes);
        byte[] padBytes = PKCS7Encoder.encode(byteGroup.size());
        byteGroup.addBytes(padBytes);
        byte[] encryptBytes = cipher.doFinal(byteGroup.toBytes());
        return Base64.encodeBase64String(encryptBytes);
    }*/


}
