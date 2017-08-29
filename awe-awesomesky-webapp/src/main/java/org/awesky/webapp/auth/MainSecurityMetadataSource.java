package org.awesky.webapp.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.awesky.api.auth.Resources;
import org.awesky.api.auth.Role;
import org.awesky.provider.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

@Service
public class MainSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private AuthService authService;
	
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	
	@PostConstruct
	private void loadResourceDefine() {
		//System.err.println(" ---------------MaxSecurityMetadataSource loadResourceDefine--------------- ");
		if (resourceMap == null) {
			resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
			List<Resources> resources = authService.fetchAllResources();
			for (Resources resource : resources) {
				Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
				Set<Role> roles = resource.getRoles();
				for(Role r : roles) {
					ConfigAttribute configAttribute = new SecurityConfig(r.getRoleKey());
					configAttributes.add(configAttribute);
				}
				resourceMap.put(resource.getResUrl(), configAttributes);
			}
		}
	}
	
	//返回所请求资源所需要的权限
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		//System.err.println(" ---------------MaxSecurityMetadataSource getAttributes--------------- ");
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		if(resourceMap == null) {
			loadResourceDefine();
		}
		if(requestUrl.indexOf("?") > -1) {
			requestUrl=requestUrl.substring(0,requestUrl.indexOf("?"));
		}
		//System.out.println(requestUrl);
		Collection<ConfigAttribute> configAttributes = resourceMap.get(requestUrl);
		return configAttributes;
	}
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

}
