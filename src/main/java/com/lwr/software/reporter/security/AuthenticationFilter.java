/* 
	Query2Report Copyright (C) 2018  Yogesh Deshpande
	
	This file is part of Query2Report.
	
	Query2Report is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	Query2Report is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with Query2Report.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lwr.software.reporter.security;

import java.io.IOException;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
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
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.openqa.selenium.remote.http.HttpResponse;

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
		System.out.println(hReq.getRequestURI()+"--"+hReq.getRequestURL());
		
		
		if(isLoginRequest(hReq)){
			chain.doFilter(request, response);
		}else{  
			Cookie[] cookies = hReq.getCookies();
			if(cookies == null){
				auth=false;
			}else{
				for (Cookie cookie : cookies) {
					if(cookie.getName().equals("Q2R_AUTH_INFO"))
					{
						String token = URLDecoder.decode(cookie.getValue(), "UTF-8");
						String tokenPatterns[] = token.split("_0_");
						if(tokenPatterns == null || tokenPatterns.length!=3){
							auth=false;
							break;
						}else{
							auth = UserManager.getUserManager().validateToken(tokenPatterns[0],token);
							break;
						}
					}
				}
			}
			if(auth){
				chain.doFilter(request, response);
			}
			else{
				HttpServletResponse httpResponse = ((HttpServletResponse)(response));
				if(hReq.getRequestURI().contains("/rest/")){
					httpResponse.sendError(401);
				} else{
					System.out.println("Request is not authenticated...Redirecting to login page...");
					httpResponse.sendRedirect("/q2r/login.html");
				}
			}
		}
	}

	private boolean isLoginRequest(HttpServletRequest hReq) {
		String uri = hReq.getRequestURI();
		Set<String> loginResources = new HashSet<String>();
		loginResources.add("/auth/");
		loginResources.add("/login/");
		loginResources.add("login.html");
		loginResources.add("/images/q2r.png");
		loginResources.add("/images/youtube.png");
		loginResources.add("/images/github.png");
		loginResources.add("/images/sourceforge.png");
		loginResources.add("/images/wall.jpg");
		loginResources.add("/images/user.png");
		loginResources.add("/CSS/");
		loginResources.add("/JS/");
		loginResources.add("/fonts/");
		for (String resource : loginResources) {
			if(uri.contains(resource)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
