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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.admin.usermgmt.UserManager;

public class AuthenticationFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		boolean auth=false;
		HttpServletRequest hReq = (HttpServletRequest) request;
		Cookie[] cookies = hReq.getCookies();
		if(cookies == null){
			auth=false;
		}else{
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("username"))
				{
					String value = cookie.getValue();
					String[] patterns = value.split("_0_");
					if(patterns.length!=2)
					{
						auth=false;
						break;
					}else{
						String userName = patterns[0];
						String password = patterns[1];
						auth = UserManager.getUserManager().authUser(userName,password);
						break;
					}
				}
			}
		}
		if(!auth && !isLoginRequest(hReq)){
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
			dispatcher.forward(request, response);
		}else{  
			chain.doFilter(request, response);
		}
	}

	private boolean isLoginRequest(HttpServletRequest hReq) {
		String uri = hReq.getRequestURI();
		Set<String> loginResources = new HashSet<String>();
		loginResources.add("/lwr/rest/");
		loginResources.add("/lwr/test");
		loginResources.add("/lwr/login");
		loginResources.add("/lwr/logout");
		loginResources.add("/lwr/doLogin");
		loginResources.add("/lwr/CSS/lwr-dash.css");
		loginResources.add("/lwr/images/lwr_logo_small.png");
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
