package com.briup.util;

import java.util.UUID;

/** 
* @author Wang Yanyang
* @date   CreateTime:	2017年9月25日 下午3:12:44
* @since 1.8
*/
public class CommonsUtil {
	//生成uuid方法
	public static String getUUid(){
		return UUID.randomUUID().toString();
	}
}
