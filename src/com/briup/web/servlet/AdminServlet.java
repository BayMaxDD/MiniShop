package com.briup.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.briup.domain.Category;
import com.briup.domain.Manager;
import com.briup.domain.Order;
import com.briup.domain.OrderItem;
import com.briup.domain.PageBean;
import com.briup.domain.Product;
import com.briup.service.ProductService;
import com.briup.service.UserService;
import com.briup.util.CommonsUtil;
import com.briup.util.MD5Utils;
import com.google.gson.Gson;

public class AdminServlet extends BaseServlet {
	//根据订单id查询订单项和商品信息   		
	public void findOrderInfoByOid(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//模拟延迟
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//获得oid
		String oid = request.getParameter("oid");
		ProductService service = 
				new ProductService();
		List<OrderItem> list = service.findAllOrderItemByOid(oid);
		
		Gson gson = new Gson();
		String json = gson.toJson(list);
		
		System.out.println(json);
		
		//设置传输数据的编码值
		response.setContentType("text/html;charset=UTF-8");
		//将数据传到jsp页面
		response.getWriter().write(json);
		
	}
	//显示全部订单  
	public void orderList(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		ProductService service = 
				new ProductService();
		List<Order> orderlist = service.orderList();
		request.setAttribute("orderlist", orderlist);
		request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
	}
	//----------------------------------------------
	//删除某一个商品 
	public void deleteProductByPid(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//获得pid
		String pid = request.getParameter("pid");
		ProductService service = 
				new ProductService();
		service.deleteProductByPid(pid);
		response.sendRedirect(request.getContextPath()+"/AdminServlet?method=productList");
	}
	//显示全部Product信息 ---分页显示
	public void productList(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String currentPageStr =
				request.getParameter("currentPage");
		if(currentPageStr == null){
			currentPageStr = "1";
		}
		int currentPage = Integer.parseInt(currentPageStr);
		int everyPageNumber = 5;
		
		ProductService service = 
				new ProductService();
		PageBean<Product> pb = service.ProductList(currentPage,everyPageNumber);
		
		request.setAttribute("pageBean", pb);
		request.getRequestDispatcher("/admin/product/list.jsp").forward(request, response);	
	}
	//------------------------------------------------
	//管理员登陆  
	public void managerLogin(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		//获得用户名及密码
		String username =
				request.getParameter("username");
		String password =
				MD5Utils.md5(request.getParameter("password"));
		
		UserService service =
				new UserService();
		
		Manager ma = service.managerLogin(username,password);
		if(ma != null){
			session.setAttribute("manager", ma);
			response.sendRedirect(request.getContextPath()+"/admin/home.jsp");
		} else {
			request.setAttribute("loginError", "用户名或密码错误");
			request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
		}
	}
	//-------------------------------------------------------
	//删除Category 
	public void deleteCategory(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		
		ProductService service =
				new ProductService();
		service.deleteCategory(cid);
		
		response.sendRedirect(request.getContextPath()+"/AdminServlet?method=categoryList");
		
	}
	//编辑Category  
	public void EditCategory(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//获得属性
		String cid = request.getParameter("cid");
		String cname = request.getParameter("cname");
		
		Category category = new Category();
		category.setCid(cid);
		category.setCname(cname);
		
		ProductService service =
				new ProductService();
		service.editCategory(category);
		
		response.sendRedirect(request.getContextPath()+"/AdminServlet?method=categoryList");
	}
	//遍历Category
	public void categoryList(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		ProductService service = 
				new ProductService();
		List<Category> allCategory = service.allCategory();
		
		request.setAttribute("allCategory", allCategory);
		request.getRequestDispatcher("/admin/category/list.jsp").forward(request, response);
	}
	//所有的Category项--->ajax使用
	public void findAllCategory(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		ProductService service = 
				new ProductService();
		List<Category> allCategory = service.allCategory();
		
		Gson gson = new Gson();
		String json = gson.toJson(allCategory);
		
		//设置传输数据的编码值
		response.setContentType("text/html;charset=UTF-8");
		//将数据传到jsp页面
		response.getWriter().write(json);
	}
	//添加Category项
	public void addCategory(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//获得Category名字
		String cname = request.getParameter("cname");
		
		//新建Category对象
		Category category = new Category();
		category.setCid(CommonsUtil.getUUid());
		category.setCname(cname);
		
		ProductService service = 
				new ProductService();
		service.addCategory(category);
		
		response.sendRedirect(request.getContextPath()+"/AdminServlet?method=categoryList");
	}
}
