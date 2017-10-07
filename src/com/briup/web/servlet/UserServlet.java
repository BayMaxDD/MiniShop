package com.briup.web.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import com.briup.domain.User;
import com.briup.service.UserService;
import com.briup.util.CommonsUtil;
import com.briup.util.MD5Utils;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends BaseServlet {
	//用户注销
	public void exit(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		
		Cookie cookie_username =
				new Cookie("cookie_username","");
		cookie_username.setMaxAge(0);
		//创建密码的Cookie
		Cookie cookie_password =
				new Cookie("cookie_password", "");
		cookie_password.setMaxAge(0);
		
		response.addCookie(cookie_username);
		response.addCookie(cookie_password);
		
		response.sendRedirect(request.getContextPath()+"/login.jsp");
	}
	
	//用户登陆
	public void login(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		//获得用户名及密码
		String username = request.getParameter("username");
		String password = MD5Utils.md5(request.getParameter("password"));
		
		//判断是否能登陆
		UserService service = new UserService();
		User user = null;
		try {
			user = service.login(username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(user != null){
			//登陆成功
			session.setAttribute("user", user);
			//request.removeAttribute("loginError");
			
			//-----用户是否自动登录-------
			String auto_Submit =
					request.getParameter("auto_submit");
			if("true".equals(auto_Submit)){
				//自动登录
				//创建存储用户的Cookie
				Cookie cookie_username =
						new Cookie("cookie_username",user.getUsername());
				cookie_username.setMaxAge(10*60);
				//创建密码的Cookie
				Cookie cookie_password =
						new Cookie("cookie_password", user.getPassword());
				cookie_password.setMaxAge(10*60);
				
				response.addCookie(cookie_username);
				response.addCookie(cookie_password);
			}
			
			//session.setAttribute("user", user);
			response.sendRedirect(request.getContextPath()+"/ProductServlet?method=IndexProduct");
		} else {
			request.setAttribute("loginError", "用户名或密码错误");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
	//用户注册
	public void regist(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		//设置编码
		request.setCharacterEncoding("UTF-8");
		//获得表单数据
		Map<String, String[]> params =
				request.getParameterMap();
		User user = new User();
		
		//在BeanUtils.populate(user, params)中,遇到Date类型,无法转化
		//需要自定义类型转化器-->ConvertUtils.register();  能够书写自己的转化规则
		try {
			//自己指定一个类型转化器(将String转为Date)
			ConvertUtils.register(new Converter() {
				@Override
				public Object convert(Class clazz, Object value) {
					//将String转为date
					SimpleDateFormat sdf = 
							new SimpleDateFormat("yyyy-MM-dd");
					Date parse = null;
					try {
						parse = sdf.parse(value.toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return parse;
				}
			}, Date.class);
			//映射封装
			BeanUtils.populate(user, params);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//密码md5加密
		user.setPassword(MD5Utils.md5(user.getPassword()));
		//设置uid
		user.setUid(CommonsUtil.getUUid());
		//设置电话
		user.setTelephone(null);
		//设置是否激活
		user.setState(0);
		//设置激活码
		String activeCode = CommonsUtil.getUUid();
		user.setCode(activeCode);
		
		//将user传递给service层
		UserService service = new UserService();
		boolean isRegistSuccess = service.regist(user);
		
		//是否注册成功
		if(isRegistSuccess){
			//发送激活邮件
//			String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户"
//					+ "<a href='http://localhost:8080/MiniShop/UserServlet?method=activeEmail&activeCode="+activeCode+"'>"
//							+ "http://localhost:8080/MiniShop/UserServlet?method=activeEmail&activeCode="+activeCode"</a>";
//		try {
//			MailUtils.sendMail(user.getEmail(), emailMsg);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
			
		//跳转到成功页面
			session.setAttribute("user", user);
			response.sendRedirect(request.getContextPath()+"/ProductServlet?method=IndexProduct");
//			response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
		} else {
		//跳转到失败页面
			response.sendRedirect(request.getContextPath()+"/register.jsp");
		}
	
	}
	//用户激活邮箱
	public void activeEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得激活码
		String activeCode =
				request.getParameter("activeCode");
		UserService service = new UserService();
		service.active(activeCode);
		
		//跳转到登陆页面
		response.sendRedirect(request.getContextPath()+"/login.jsp");
	}
	//验证用户名是否重复
	public void checkName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得用户名
		String name =
				request.getParameter("username");
		
		UserService us = new UserService();
		boolean isExist = us.checkUsername(name);
				
		String json = "{\"isExist\":"+isExist+"}";
		response.getWriter().write(json);
	}

}
