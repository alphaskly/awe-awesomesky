package org.awesky.webapp.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.awesky.api.auth.Resources;
import org.awesky.api.entity.Customer;
import org.awesky.provider.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MainUserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private AuthService authService;

	//登录
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		//System.err.println("-----------MyUserDetailServiceImpl loadUserByUsername ----------- ");
		//取得用户的权限
		Customer user = authService.findCustomer(userName);
		if  (user==null)  
            throw new UsernameNotFoundException(userName+" not exist!");  
		Collection<GrantedAuthority> grantedAuths = obtionGrantedAuthorities(user);
		// 封装成spring security的user
		User userdetail = new User(
				user.getName(), 
				user.getPassword(),
				true, 
				true, 
				true,
				true, 
				grantedAuths	//用户的权限
			);
		return userdetail;
	}
	
	// 取得用户的权限
	private Set<GrantedAuthority> obtionGrantedAuthorities(Customer user) {
		//System.err.println("-----------MyUserDetailServiceImpl obtionGrantedAuthorities----------- ");
		List<Resources> resources = authService.fetchCustomerResources(String.valueOf(user.getRole()));
		Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
		for (Resources res : resources) {
			// TODO:ZZQ 用户可以访问的资源名称（或者说用户所拥有的权限） 注意：必须"ROLE_"开头
			authSet.add(new SimpleGrantedAuthority(res.getResKey()));
		}
		return authSet;
	}

}
