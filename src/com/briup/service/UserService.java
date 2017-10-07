package com.briup.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.briup.domain.Manager;
import com.briup.domain.OrderItem;
import com.briup.domain.User;
import com.briup.mapper.ProductMapper;
import com.briup.mapper.SessionPool;
import com.briup.mapper.UserMapper;

/** 
* @author Wang Yanyang
* @date   CreateTime:	2017年9月25日 下午3:16:48
* @since 1.8
*/
public class UserService {
	private SessionPool sessionPool;
	private SqlSession ss;
	
	public UserService() {
		//SqlSession对象池
		sessionPool = new SessionPool();
	}

	public boolean regist(User user) {	
		try {
			//得到SqlSession对象
			ss = sessionPool.getSqlSession();
			//得到UserMapper对象
			UserMapper mapper = ss.getMapper(UserMapper.class);
			//调用regist方法
			mapper.regist(user);
		} catch (Exception e) {
			ss.rollback(true);
			e.printStackTrace();
			return false;
		} finally{
			ss.commit();
			ss.close();
		}
		return true;
	}

	//没写完？？？？
	public void active(String activeCode) {
		ss = sessionPool.getSqlSession();
		//得到UserMapper对象
		UserMapper mapper = ss.getMapper(UserMapper.class);
		mapper.active(activeCode);
		ss.commit();
		ss.close();
	}

	public boolean checkUsername(String name) {
		ss = sessionPool.getSqlSession();
		//得到UserMapper对象
		UserMapper mapper = ss.getMapper(UserMapper.class);
		int len = mapper.checkUsername(name);
		ss.commit();
		ss.close();
		return len > 0 ? true : false;
	}
	//判断用户能否登陆
	public User login(String username,String password){
		
		ss = sessionPool.getSqlSession();
		//得到UserMapper对象
		UserMapper mapper = ss.getMapper(UserMapper.class);
		User user = mapper.login(username, password);
		ss.commit();
		ss.close();
		return user;
	}
	//管理员登录
	public Manager managerLogin(String username, String password) {
		// TODO Auto-generated method stub
		ss = sessionPool.getSqlSession();
		//得到UserMapper对象
		UserMapper mapper = ss.getMapper(UserMapper.class);
		Manager manager = mapper.managerLogin(username, password);
		ss.commit();
		ss.close();
		return manager;
	}
}
