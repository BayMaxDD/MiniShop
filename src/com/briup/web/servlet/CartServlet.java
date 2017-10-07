package com.briup.web.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.briup.domain.Cart;
import com.briup.domain.CartItem;
import com.briup.domain.Order;
import com.briup.domain.OrderItem;
import com.briup.domain.Product;
import com.briup.domain.User;
import com.briup.service.ProductService;
import com.briup.util.CommonsUtil;

/**
 * Servlet implementation class CartServlet
 */
public class CartServlet extends BaseServlet {
	//获得我的订单
	public void myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//判断用户是否登陆
		User user = (User)session.getAttribute("user");
		if(user == null){
			//没有登陆
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		
		//查询用户所有的订单信息
		ProductService service = new ProductService();
		List<Order> orderList = service.findAllOrders(user.getUid());
		if(orderList != null){
			//订单信息不为空
			for (Order order : orderList) {//遍历所有的订单信息
				//获得oid
				String oid = order.getOid();
				List<OrderItem> orderItems =
						service.findAllOrderItemByOid(oid);
				order.setOrderItems(orderItems);
			}
		}
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("/order_list.jsp").forward(request, response);
	}
	
	//确认订单,在线支付
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//更新收货人信息
		Map<String, String[]> props =
				request.getParameterMap();
		Order order = new Order();
		try {
			BeanUtils.populate(order, props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProductService service = new ProductService();
		service.confirmOrder(order);
		//在线支付-->先不写
		//....
		
		response.sendRedirect(request.getContextPath()+"/ProductServlet?method=IndexProduct");
	}
	
	//提交订单
	public void orderSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//判断用户是否登陆
		User user = (User)session.getAttribute("user");
		if(user == null){
			//没有登录
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		
		//封装好Order对象
		Order order = new Order();
		//该订单的订单号
		order.setOid(CommonsUtil.getUUid());
		//下单时间
		order.setOrdertime(new Date());
		//该订单的总金额
		Cart cart = (Cart)session.getAttribute("cart");
		order.setTotal(cart.getTotal());
		//订单支付状态 1代表已付款 0代表未付款
		order.setState(0);
		//收货地址
		order.setAddress(null);
		//收货人
		order.setName(null);
		//收货人电话
		order.setTelephone(null);
		//该订单属于哪个用户
		order.setUser(user);
		
		//该订单中有多少订单项
		Map<String, CartItem> cartItem = cart.getCart();
		Set<String> keySet = cartItem.keySet();
		for (String key : keySet) {
			//每一个购物项
			CartItem everyItem = cartItem.get(key);
			//新的订单项
			OrderItem orderItem = new OrderItem();
			//订单项的id
			orderItem.setItemid(CommonsUtil.getUUid());
			//订单项内商品的购买数量
			orderItem.setCount(everyItem.getBuyNum());
			//订单项小计
			orderItem.setSubtotal(everyItem.getSubTotal());
			//订单项内部的商品
			orderItem.setProduct(everyItem.getProduct());
			//该订单项属于哪个订单
			orderItem.setOrder(order);
			//将每一个购物项加到Order中
			order.getOrderItems().add(orderItem);
		}
		//order对象封装完毕
		ProductService service = new ProductService();
		service.submitOrder(order);
		
		session.setAttribute("order", order);
		
		response.sendRedirect(request.getContextPath()+"/order_info.jsp");
	}

	 //将商品添加到购物车
	 public void addProductToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 //获得session对象
		 HttpSession session = request.getSession();
		 
		 //获得要放入购物车的商品pid
		 String pid = request.getParameter("pid");
		 //获得购买商品的数量
		 int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		 
		 //获得product对象
		 ProductService service = new ProductService();
		 Product product = service.findProductByPid(pid);
		 //计算小结
		 double subTotal = buyNum * product.getShop_price();
		 //封装到CartItem中
		 CartItem cartItem = new CartItem();
		 cartItem.setProduct(product);
		 cartItem.setBuyNum(buyNum);
		 cartItem.setSubTotal(subTotal);
		 
		 //封装到购物车中
		 Cart cart = (Cart)session.getAttribute("cart");
		 if(cart == null){
			 cart = new Cart();
		 }
		 //将购物项放到购物车
		 //判断购物车中是否有购物项
		 Map<String, CartItem> cartItem2 = cart.getCart();
		 double newsubtotal = 0.0;
		 
		 if(cartItem2.containsKey(pid)){
			 //包含
			 //取出原有的数,相加
			 CartItem cartItem3 = cartItem2.get(pid);
			 //修改数
			 int oldBuyNum = cartItem3.getBuyNum();
			 oldBuyNum += buyNum;
			 cartItem3.setBuyNum(oldBuyNum);
			 //修改小计
			//原来该商品的小计
			double oldsubtotal = cartItem3.getSubTotal();
			//新买的商品的小计
			newsubtotal = buyNum*product.getShop_price();
			cartItem3.setSubTotal(oldsubtotal+newsubtotal);
			
		 } else {
			 cart.getCart().put(product.getPid(), cartItem);
			 newsubtotal = buyNum*product.getShop_price();
		 }

		 //计算总计
		 double total = cart.getTotal()+newsubtotal;
		 cart.setTotal(total);
		
		 //将购物车放到Session
		 session.setAttribute("cart", cart);
		 
		 //跳到购物车页面
		 response.sendRedirect(request.getContextPath()+"/cart.jsp");
	 }
	 
	 //删除某个商品
	 public void delProFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 //获得删除商品的pid
		 String pid = request.getParameter("pid");
		 //获得session对象
		 HttpSession session = request.getSession();
		 //获得Cart对象
		 Cart cart = (Cart)session.getAttribute("cart");
		 if(cart != null){
			 Map<String, CartItem> cartItem = cart.getCart();
			 //修改总价
			 cart.setTotal(cart.getTotal()-cartItem.get(pid).getSubTotal());
			 //删除选中项
			 cartItem.remove(pid);
			 cart.setCart(cartItem);
		 }
		 //将cart对象设置进session中
		 session.setAttribute("cart", cart);
		 
		 response.sendRedirect(request.getContextPath()+"/cart.jsp");
	 }
	 
	 //清除购物车
	 public void clearChart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 HttpSession session = request.getSession();
		 session.removeAttribute("cart");
		 response.sendRedirect(request.getContextPath()+"/cart.jsp");
	 }
}
