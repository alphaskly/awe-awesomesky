package org.awesky.provider.common;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Created by vme on 2017/8/26.
 */
public class AwesomeDruidDataSource extends DruidDataSource {

    @Override
    public void setPassword(String password) {
        String realPassword = PasswdEncode.aesDecode(password, PasswdEncode.USER_PWD_KEY);
        super.setPassword(realPassword);
    }

    @Override
    public void setUsername(String username) {
        String realUserName = PasswdEncode.aesDecode(username, PasswdEncode.USER_NAME_KEY);
        super.setUsername(realUserName);
    }

}
