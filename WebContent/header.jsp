<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<script type="text/javascript">
	/*加载header.jsp以后,去服务器获得所有category信息  */
	$(function(){
		var content="";
		$.post(
			"${pageContext.request.contextPath}/ProductServlet?method=categoryList",
			function(data){
				//得到的值为
				//[{"cid":"xxx","cname":"xxx"},{...},{...},...]
				//动态创建<li><a href="#">xxx</a></li>
				for(var i = 0; i < data.length; i++){
					content+="<li><a href='${pageContext.request.contextPath}/ProductServlet?method=findProductByCid&cid="+data[i].cid+"'>"+data[i].cname+"</a></li>";
				}						
				//将拼好的li放到ul中
				$("#categoryUL").html(content);
			},
			"json"
		);
	});
</script>
<!-- 登录 注册 购物车... -->
<div class="container-fluid">
	<div class="col-md-4">
		<img src="img/logo.jpg" />
	</div>
	<div class="col-md-5">
		<img src="img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
			<c:if test="${!empty user }">
				<li style="color:red">欢迎您，${user.username }</li>
				<li><a href="${pageContext.request.contextPath }/UserServlet?method=exit">注销</a></li>
			</c:if>
			<c:if test="${empty user }">
				<li><a href="login.jsp">登录</a></li>
				<li><a href="register.jsp">注册</a></li>
			</c:if>
			<li><a href="cart.jsp">购物车</a></li>
			<li><a href="${pageContext.request.contextPath }/CartServlet?method=myOrders">我的订单</a></li>
		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="">首页</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav" id="categoryUL">					
					<%-- <c:forEach items="${categoryList}" var="category">
						<li><a href="#">${category.cname}</a></li>
					</c:forEach> --%>
				</ul>
				<form class="navbar-form navbar-right" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>
	</nav>
</div>