package com.briup.mapper;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/** 
* @author Wang Yanyang
* @date   CreateTime:	2017年9月27日 下午7:28:44
* @since 1.8
*/
public class SessionPool {
	//初始化个数
	private int init = 3;
	//当前个数
	private int curr = 0;
	//集合存放SqlSession
	private List<SqlSession> list = new ArrayList<SqlSession>();
	
	public SessionPool(){
		while(curr < init){
			SqlSession session = createSqlSession();
			list.add(session);
			curr++;
		}
	}
	
	//创建SqlSession
	private SqlSession createSqlSession(){
		SqlSession proxy = null;
		try {
			//new 工厂对象
			SqlSessionFactoryBuilder ssfb =
					new SqlSessionFactoryBuilder();
			//创建工厂
			SqlSessionFactory factory =
					ssfb.build(Resources.getResourceAsStream(
							"mybatis-config.xml"));
			//得到SqlSession对象
			SqlSession session = factory.openSession();
			proxy = (SqlSession)Proxy.newProxyInstance(
					session.getClass().getClassLoader(), 
					session.getClass().getInterfaces(), 
					new InvocationHandler() {
						
						@Override
						public Object invoke(Object proxy,
									Method method, 
									Object[] args) throws Throwable {
							
							if("close".equals(method.getName())){
								list.add(session);
								curr++;
								return null;
							}
							curr++;
							return method.invoke(session, args);
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return proxy;
	}
	
	//得到SqlSession
	public SqlSession getSqlSession(){
		if(list.size() > 0){
			curr--;
			return list.remove(0);
		}
		return createSqlSession();
	}
}
