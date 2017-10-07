package com.briup.domain;

import java.util.List;

/** 
* @author Wang Yanyang
* @date   CreateTime:	2017年9月28日 下午7:17:20
* @since 1.8
*/
public class PageBean<T> {
	//当前页数
	private int currentPage;
	//每页条数
	private int everyPageNumber;
	//总数
	private int totalNumber;
	//总页数
	private int totalPage;
	//每页存放条数
	private List<T> list;
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getEveryPageNumber() {
		return everyPageNumber;
	}
	public void setEveryPageNumber(int everyPageNumber) {
		this.everyPageNumber = everyPageNumber;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
}
