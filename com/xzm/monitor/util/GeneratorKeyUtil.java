package com.xzm.monitor.util;

import org.springframework.util.StringUtils;

/**
 * 生成key 工具
 * @author helloxu
 * @version 2014-8-26  下午03:37:43
 *
 */
public class GeneratorKeyUtil {
	
	/**
	 * 
	 * @author helloxu
	 * @version 2014-8-26  下午03:41:29
	 * @param mainKey 关键参数
	 * @param keys 基础参数
	 * @return
	 */
	public static String getKey(String mainKey,String... keys){
		StringBuffer buffer = new StringBuffer();
		for (String key : keys) {
			if(!StringUtils.isEmpty(key) && !"null".equalsIgnoreCase(key)){
				buffer.append(key).append("_");
			}
		}
		if(!StringUtils.isEmpty(mainKey)){
			buffer.append(mainKey);
		}
		return buffer.toString();
		
	}

}
