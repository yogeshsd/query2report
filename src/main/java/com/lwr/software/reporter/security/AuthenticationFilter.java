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
		System.out.println(hReq.getRequestURL());
		System.out.println(hReq.getRequestURI());
		Cookie[] cookies = hReq.getCookies();
		if(cookies == null){
			auth=false;
		}else{
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("username"))
				{
					String value = cookie.getValue();
					String[] patterns = value.split("_0_");
					if(patterns.length!=3)
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
		if(auth){
			if(hReq.getRequestURI().equals("/q2r/")){
				RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
				dispatcher.forward(request, response);
			}
			chain.doFilter(request, response);
		}
		else{
			if(!isLoginRequest(hReq)){
				RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
				dispatcher.forward(request, response);
			}else{  
				chain.doFilter(request, response);
			}
		}
	}

	private boolean isLoginRequest(HttpServletRequest hReq) {
		String uri = hReq.getRequestURI();
		Set<String> loginResources = new HashSet<String>();
		loginResources.add("/login");
		loginResources.add("/logout");
		loginResources.add("/doLogin");
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
			if(uri.contains(resource))
				return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
