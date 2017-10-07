package com.briup.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.briup.domain.Category;
import com.briup.domain.PageBean;
import com.briup.domain.Product;
import com.briup.service.ProductService;
import com.google.gson.Gson;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends BaseServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductServlet() {
    }
    
	//获得列表名
    public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ProductService service = 
				new ProductService();
		//导航栏
		List<Category> category = service.allCategory();
		
		//一般情况需要我们将得到的值拼成固定的字符串,但是我们此次结果太多,
		//可以使用Gson帮助我们简化操作
		//[{"cid":"xxx","cname":"xxx"},{...},{...},...]
		Gson gson = new Gson();
		String json = gson.toJson(category);
		
		//设置传输数据的编码值
		response.setContentType("text/html;charset=UTF-8");
		//将数据传到jsp页面
		response.getWriter().write(json);
	}
	
	
	//获得某一分类下的商品信息
	public void findProductByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//得到传过来的Category信息
		String cid = request.getParameter("cid");
		
		String currentPageStr = request.getParameter("currentPage");
		if("".equals(currentPageStr) || currentPageStr==null){
			currentPageStr = "1";
		} 
		int currentPage = Integer.parseInt(currentPageStr);
		int everyPageNumber = 12;
		
		//通过cid获得要显示的信息
		ProductService server = 
				new ProductService();
		PageBean<Product> pb = server.findCategoryListById(cid,currentPage,everyPageNumber);
		
		request.setAttribute("pageBean", pb);
		request.setAttribute("cid", cid);
		
		//定义一个历史集合存放pid信息
		List<Product> Histroylist = new ArrayList<Product>();
		
		//得到名为pids的cookie
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for (Cookie cookie : cookies) {
				if("pids".equals(cookie.getName())){
					String value = cookie.getValue();
					String[] split = value.split(",");
					for (String sPid : split) {
						Product pro = server.findProductByPid(sPid);
						Histroylist.add(pro);
					}
				}
			}
		}
		
		//将历史集合传递给jsp页面
		request.setAttribute("Histroylist", Histroylist);
		
		request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	}
	
	
	//首页的显示信息
	public void IndexProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service = 
				new ProductService();
		//准备热门商品
		List<Product> hotProductList = 
				service.findHotProductList();
		
		//准备最新商品
		List<Product> newProductList =
				service.findNewProductList();
		
		request.setAttribute("hotProductList", hotProductList);
		request.setAttribute("newProductList", newProductList);
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
	//某一商品的详细信息
	public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获得cid信息
		String cid = request.getParameter("cid");
		//获得currentPage信息
		String currentPage = request.getParameter("currentPage");
		
		//接受pid信息
		String pid = request.getParameter("pid");
		ProductService service =
				new ProductService();
		Product product = service.findProductByPid(pid);
		
		request.setAttribute("product", product);
		request.setAttribute("cid", cid);
		request.setAttribute("currentPage", currentPage);
		
		//获得客户端携带的cookies--->获得名字为pids的cookies
		String pids = pid;
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for (Cookie cookie : cookies) {
				if("pids".equals(cookie.getName())){
					pids = cookie.getValue();
					String[] split = pids.split(",");
					//将cookie进行处理,存放到集合中
					LinkedList<String> list = new LinkedList<String>();
					for (String str : split) {
						list.add(str);
					}
					//判断集合中是否存在-->达到目的:如果不存在,在第一个添加pid,反之将pid移到第一个
					//存在时,先移除,便于后面添加
					if(list.contains(pid)){
						list.remove(pid);
					}
					//无论是否原来有无,都添加
					list.addFirst(pid);
					
					//然后将集合转为字符串,便于后续传输
					StringBuffer sb = new StringBuffer();
					for(int i = 0; i < list.size() && i < 7; i++){
						sb.append(list.get(i)+",");
					}
					pids = sb.substring(0, sb.length() - 1);
				}
			}
		}
		//设置一个名字为pids的cookie,传递出去
		Cookie cookie_pids = new Cookie("pids", pids);
		response.addCookie(cookie_pids);
		
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
	}
}
