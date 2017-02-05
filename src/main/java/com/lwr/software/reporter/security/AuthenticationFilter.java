package com.lwr.software.reporter.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.lwr.software.reporter.DashboardConstants;

public class AuthenticationFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hReq = (HttpServletRequest) request;
		UserSecurityContext loginCode = (UserSecurityContext) hReq.getSession().getAttribute(DashboardConstants.SECURITY_CONTEXT);
		boolean isLogin = isLoginRequest(hReq);
		if(loginCode == null && !isLogin){
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
			dispatcher.forward(request, response);
		}else{
			chain.doFilter(request, response);
		}
	}

	private boolean isLoginRequest(HttpServletRequest hReq) {
		String uri = hReq.getRequestURI();
		Set<String> loginResources = new HashSet<String>();
		loginResources.add("/lwr/test");
		loginResources.add("/lwr/login");
		loginResources.add("/lwr/logout");
		loginResources.add("/lwr/doLogin");
		loginResources.add("/lwr/CSS/lwr-dash.css");
		loginResources.add("/lwr/images/lwr_logo.png");
		loginResources.add("/lwr/images/wall.jpg");
		loginResources.add("/lwr/images/user.png");
		loginResources.add("/lwr/JS/bootstrap-typeahead.js");
		loginResources.add("/lwr/JS/jquery.min.js");
		for (String resource : loginResources) {
			if(uri.contains(resource))
				return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
