package com.briup.domain;
/** 
* @author Wang Yanyang
* @date   CreateTime:	2017年10月1日 上午8:45:14
* @since 1.8
*/
public class CartItem {
	//商品
	private Product product;
	//购买数量
	private int buyNum;
	//小计
	private double subTotal;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	
}
