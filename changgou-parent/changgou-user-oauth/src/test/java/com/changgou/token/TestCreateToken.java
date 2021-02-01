package com.changgou.token;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成令牌
 */
public class TestCreateToken {
    //证书文件路径
    String key_location="changgou.jks";
    //秘钥库密码
    String key_password="changgou";
    //秘钥密码
    String keypwd = "changgou";
    //秘钥别名
    String alias = "changgou";


    @Test
    public void createToken(){
        ClassPathResource resource = new ClassPathResource(key_location);
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, key_password.toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypwd.toCharArray());
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        Map<String,String> plain = new HashMap<>();
        plain.put("name","zhangSan");
        plain.put("phone","15390418974");
        Jwt jwt = JwtHelper.encode(JSONObject.toJSONString(plain), new RsaSigner(aPrivate));
        String token = jwt.getEncoded();
        System.out.println(token);

    }
}
