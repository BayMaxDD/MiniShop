<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//作用：在点击退出的情况下，让所有的session会话失效，减轻服务器的压力
	//invalidate()只针对session对象
	
	session.invalidate();
	
	response.sendRedirect(request.getContextPath()+"/ProductServlet?method=IndexProduct");
%>
