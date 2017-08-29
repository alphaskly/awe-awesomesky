package org.awesky.provider.services.auth;

import org.awesky.api.auth.Resources;
import org.awesky.api.entity.Customer;

import java.util.List;

/**
 * Created by vme on 2017/8/27.
 */
public interface AuthService {

    List<Resources> fetchAllResources();

    List<Resources> fetchCustomerResources(String account);

    Customer findCustomer(String account);
}
