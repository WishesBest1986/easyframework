package com.neusoft.cas.jdbc;

import com.neusoft.cas.utils.DigestUtils;
import com.neusoft.cas.utils.EncodeUtils;
import org.jasig.cas.authentication.handler.PasswordEncoder;

/**
 * Created by neusoft on 15-5-26.
 */
public class UCPasswordEncoder implements PasswordEncoder {
    private String salt;

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String encode(String password) {
        byte[] saltBytes = EncodeUtils.hexDecode(salt);
        byte[] hashPassword = DigestUtils.sha1(password.getBytes(), saltBytes, 1024);
        return EncodeUtils.hexEncode(hashPassword);
    }
}
