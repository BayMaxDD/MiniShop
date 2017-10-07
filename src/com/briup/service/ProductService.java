package com.briup.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.briup.domain.Category;
import com.briup.domain.Order;
import com.briup.domain.OrderItem;
import com.briup.domain.PageBean;
import com.briup.domain.Product;
import com.briup.mapper.ProductMapper;
import com.briup.mapper.SessionPool;

/** 
* @author Wang Yanyang
* @date   CreateTime:	2017年9月27日 上午9:00:01
* @since 1.8
*/
public class ProductService {
	private SessionPool sessionPool;
	private SqlSession ss;
	
	public ProductService() {
		//SqlSession对象池
		sessionPool = new SessionPool();
	}
	//获得热门商品
	public List<Product> findHotProductList() {
		//得到SqlSession对象
		ss = sessionPool.getSqlSession();
		ProductMapper mapper =
				ss.getMapper(ProductMapper.class);
		List<Product> list = mapper.findHotProductList();
		ss.commit();
		ss.close();
		return list;
	}
	
	//获得最新商品
	public List<Product> findNewProductList() {
		ss = sessionPool.getSqlSession();
		ProductMapper mapper =
				ss.getMapper(ProductMapper.class);
		List<Product> list = mapper.findNewProductList();
		ss.commit();
		ss.close();
		return list;
	}
	
	//导航栏
	public List<Category> allCategory(){
		ss = sessionPool.getSqlSession();
		ProductMapper mapper =
				ss.getMapper(ProductMapper.class);
		List<Category> list = mapper.allCategory();
		ss.commit();
		ss.close();
		return list;
	}
	//按分类显示商品信息
	public PageBean<Product> findCategoryListById(String cid, 
			int cPage, int cCount) {
		ss = sessionPool.getSqlSession();
		ProductMapper mapper =
				ss.getMapper(ProductMapper.class);
		
		//封装一个PageBean 返回web层
		PageBean<Product> pb = new PageBean<Product>();
		//设置当前页面
		pb.setCurrentPage(cPage);
		//设置每页显示数量
		pb.setEveryPageNumber(cCount);
		//设置总条数
		int totalNumber = mapper.findAllProductByCid(cid);
		pb.setTotalNumber(totalNumber);
		//设置总页数
		pb.setTotalPage((int)(Math.ceil(1.0*totalNumber/cCount)));
		//当前页面数据
		//求索引
		int index = (cPage - 1) * cCount;
		List<Product> list = mapper.findCategoryList(cid,index,cCount);
		pb.setList(list);
		return pb;
	}
	//显示某一商品的具体信息
	public Product findProductByPid(String pid) {
		// TODO Auto-generated method stub
		ss = sessionPool.getSqlSession();
		ProductMapper mapper =
				ss.getMapper(ProductMapper.class);
		Product product = mapper.findProductByPid(pid);
		ss.commit();
		ss.close(); 
		return product;
	}
	//提交订单
	public void submitOrder(Order order){
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			mapper.addOrder(order);
			
			List<OrderItem> orderItems = order.getOrderItems();
			for (OrderItem orderItem : orderItems) {
				mapper.addOrderItem(orderItem);
			}
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
	}
	//确认订单
	public void confirmOrder(Order order) {
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			mapper.confirmOrder(order);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
	}
	//获得指定用户的订单集合
	public List<Order> findAllOrders(String uid) {
		List<Order> orderlist = null;
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			orderlist = mapper.findAllOrders(uid);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
		return orderlist;
	}
	//所有的订单信息-->订单项信息
	public List<OrderItem> findAllOrderItemByOid(String oid) {
		List<OrderItem> orderItemlist = null;
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			orderItemlist = mapper.findAllOrderItemByOid(oid);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
		return orderItemlist;
	}
	//添加Category项
	public void addCategory(Category category) {
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			mapper.addCategory(category);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
	}
	//编辑Category项信息
	public void editCategory(Category category) {
		// TODO Auto-generated method stub
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			mapper.editCategory(category);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
	}
	//删除Category项
	public void deleteCategory(String cid) {
		// TODO Auto-generated method stub
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			mapper.deleteCategory(cid);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
	}
	public void addProduct(Product product) {
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			mapper.addProduct(product);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
	}
	//显示商品信息->不分类
	public PageBean<Product> ProductList(int currentPage, int everyPageNumber) {
		ss = sessionPool.getSqlSession();
		ProductMapper mapper =
				ss.getMapper(ProductMapper.class);
		//封装一个PageBean
		PageBean<Product> pb =
				new PageBean<Product>();
		//设置当前界面
		pb.setCurrentPage(currentPage);
		//设置每页显示的条数
		pb.setEveryPageNumber(everyPageNumber);
		//设置总条数
		int total = mapper.findAllProductCount();
		pb.setTotalNumber(total);
		//设置总页数
		pb.setTotalPage((int)(Math.ceil(1.0*total/everyPageNumber)));
		//当前页面的数据
		int index = (currentPage - 1)*everyPageNumber;
		List<Product> list = mapper.ProductList(index,everyPageNumber);
		pb.setList(list);
		
		return pb;
	}
	//删除某一个商品
	public void deleteProductByPid(String pid) {
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			mapper.deleteProductByPid(pid);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
	}
	//修改商品
	public void updateProduct(Product product) {
		try {
			ss = sessionPool.getSqlSession();
			ProductMapper mapper =
					ss.getMapper(ProductMapper.class);
			mapper.updateProduct(product);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
		} finally{
			ss.commit();
			ss.close();
		}
	}
	//得到所有的订单项
	public List<Order> orderList() {
		ss = sessionPool.getSqlSession();
		ProductMapper mapper =
				ss.getMapper(ProductMapper.class);
		List<Order> list = mapper.orderList();
		ss.commit();
		ss.close();
		return list;
	}
}
