package org.awesky.webapp.auth;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

@Service
public class MainSecurityFilter extends AbstractSecurityInterceptor implements Filter {

	@Resource(name="mainSecurityMetadataSource")
	private MainSecurityMetadataSource mainSecurityMetadataSource;
	@Resource(name="mainAccessDecisionManager")
	private MainAccessDecisionManager mainAccessDecisionManager;
	@Resource(name="authenticationManager")
	private AuthenticationManager authenticationManager; 
	
	@PostConstruct
	public void init(){
		//System.err.println(" ---------------  MaxSecurityFilter init--------------- ");
		super.setAuthenticationManager(authenticationManager);
		super.setAccessDecisionManager(mainAccessDecisionManager);
	}
	
	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.mainSecurityMetadataSource;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//System.out.println("--------------------MaxSecurityFilter doFilter--------------");
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}
	
	private void invoke(FilterInvocation fi) throws IOException, ServletException {
		//System.out.println("--------------------MaxSecurityFilter invoke--------------");
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
	
	public void destroy() {
		
	}

	@Override
	public Class<? extends Object> getSecureObjectClass() {
		//下面的MyAccessDecisionManager的supports方面必须放回true,否则会提醒类型错误
		return FilterInvocation.class;
	}
	
}
