package com.briup.mapper;

import java.util.List;

import com.briup.domain.Category;
import com.briup.domain.Order;
import com.briup.domain.OrderItem;
import com.briup.domain.Product;

/** 
* @author Wang Yanyang
* @date   CreateTime:	2017年9月27日 上午10:44:30
* @since 1.8
*/
public interface ProductMapper {
	//查询热门商品
	public List<Product> findHotProductList();
	//查询最新商品
	public List<Product> findNewProductList();
	//导航栏
	public List<Category> allCategory();
	//查询不同分类的分页信息
	public List<Product> findCategoryList(String cid, int index, int cPage);
	//查询不同类型的商品的总数
	public int findAllProductByCid(String cid);
	//显示某一商品的具体信息
	public Product findProductByPid(String pid);
	//提交顶单
	public void addOrder(Order order);
	//提交订单项
	public void addOrderItem(OrderItem orderItem);
	//确认订单
	public void confirmOrder(Order order);
	//所有订单集合
	public List<Order> findAllOrders(String uid);
	//订单--->订单项
	public List<OrderItem> findAllOrderItemByOid(String oid);
	//添加Category项
	public void addCategory(Category category);
	//编辑Category信息
	public void editCategory(Category category);
	//删除某一个Category信息
	public void deleteCategory(String cid);
	//添加商品
	public void addProduct(Product product);
	//得到商品总数量
	public int findAllProductCount();
	//得到全部商品
	public List<Product> ProductList(int index, int everyPageNumber);
	//删除某一商品
	public void deleteProductByPid(String pid);
	//修改商品
	public void updateProduct(Product product);
	//得到所有的订单项--->管理员可以得到所有的订单项
	public List<Order> orderList();
}
