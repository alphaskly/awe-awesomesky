package org.awesky.provider.services.auth;

import com.alibaba.dubbo.config.annotation.Service;
import org.awesky.api.auth.Resources;
import org.awesky.api.entity.Customer;
import org.awesky.provider.dao.auth.AuthResourcesMapper;
import org.awesky.provider.dao.base.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by vme on 2017/8/27.
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthResourcesMapper resourcesMapper;
    @Autowired
    private CustomerMapper customerMapper;

    public List<Resources> fetchAllResources() {
        return resourcesMapper.findAll();
    }

    public List<Resources> fetchCustomerResources(String account) {
        return resourcesMapper.getUserResources(account);
    }

    public Customer findCustomer(String account) {
        return customerMapper.findCustomerByName(account);
    }

}
