package com.briup.domain;

import java.util.HashMap;
import java.util.Map;

/** 
* @author Wang Yanyang
* @date   CreateTime:	2017年10月1日 上午8:45:21
* @since 1.8
*/
public class Cart {
	//购物项
	private Map<String,CartItem> cart = 
			new HashMap<String,CartItem>();
	//总计
	private double total;
	public Map<String, CartItem> getCart() {
		return cart;
	}
	public void setCart(Map<String, CartItem> cart) {
		this.cart = cart;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}	
}
