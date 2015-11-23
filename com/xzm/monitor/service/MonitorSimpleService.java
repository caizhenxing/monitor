package com.xzm.monitor.service;

import org.aopalliance.intercept.MethodInvocation;

import com.xzm.monitor.entity.MonitorInfo;
import com.xzm.monitor.entity.Paging;


public interface MonitorSimpleService {
	
	
	boolean checkMonitorMethod(MonitorInfo monitorInfo,MethodInvocation invocation);
	
	Paging<MonitorInfo> getMonitorMethods(Paging<MonitorInfo> paging,MonitorInfo monitorInfo);
	
	boolean addMonitorMethod(MonitorInfo monitorInfo);
	
	boolean removeMonitorMethod(MonitorInfo monitorInfo);
	
	/**
	  * 添加监控方法执行信息
	  * @Title: addMethodProcessInfo
	  * @Description: TODO
	  * @param @param monitorInfo
	  * @param @return   
	  * @return boolean
	  * @throws
	 */
	boolean addMethodProcessInfo(MonitorInfo monitorInfo);
	/**
	  * 查询方法执行历史信息
	  * @Title: getMonitorMethodProcrssInfo
	  * @Description: TODO
	  * @param @param paging
	  * @param @param monitorInfo
	  * @param @return   
	  * @return Paging<MonitorInfo>
	  * @throws
	 */
	Paging<MonitorInfo> getMonitorMethodProcrssInfo(Paging<MonitorInfo> paging, MonitorInfo monitorInfo);
	
}
