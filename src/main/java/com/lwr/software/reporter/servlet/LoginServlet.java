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

package com.lwr.software.reporter.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lwr.software.reporter.DashboardConstants;
import com.lwr.software.reporter.admin.usermgmt.User;
import com.lwr.software.reporter.admin.usermgmt.UserManager;

public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = (String) req.getParameter(DashboardConstants.USERNAME);
		String password = (String) req.getParameter(DashboardConstants.PASSWORD);
		User user = UserManager.getUserManager().getUser(username);
		boolean auth = false;
		String token = null;
		if(user == null){
			auth=false;
		}else{
			 token = UserManager.getUserManager().authUser(username, password);
			if(token != null)
				auth=true;
			else
				auth=false;
		}
		if(auth){
			Cookie cookie = new Cookie("Q2R_AUTH_INFO",token);
			cookie.setMaxAge(user.getSessionTimeout());
			resp.addCookie(cookie);
			resp.sendRedirect("index.html");
		}else{
			req.getSession().putValue("errmsg", "Invalid Username or Password. Try again!!");
			getServletContext().getRequestDispatcher("/login").forward(req, resp);
		}
	}
}
