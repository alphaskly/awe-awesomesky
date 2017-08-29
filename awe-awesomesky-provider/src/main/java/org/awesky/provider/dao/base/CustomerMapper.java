package org.awesky.provider.dao.base;

import org.apache.ibatis.annotations.Select;
import org.awesky.api.entity.Customer;

import java.util.List;

/**
 * Created by vme on 2017/8/22.
 */
public interface CustomerMapper {

    @Select({" select u_id id,u_name name,u_password password,u_role role from tb_users where u_name=#{username, jdbcType=VARCHAR}"})
    Customer findCustomerByName(String name);

}
