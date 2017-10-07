package com.briup.mapper;

import com.briup.domain.Manager;
import com.briup.domain.User;

/** 
* @author Wang Yanyang
* @date   CreateTime:	2017年9月25日 下午4:05:30
* @since 1.8
*/
public interface UserMapper {
	//注册用户
	public int regist(User user);
	//激活用户
	public int active(String activeCode);
	//检测用户名否存在
	public int checkUsername(String name);
	//登陆
	public User login(String name,String password);
	//管理员登陆
	public Manager managerLogin(String username, String password);
}
