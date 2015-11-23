package com.xzm.monitor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.xzm.monitor.annotation.MethodMonitor;
import com.xzm.monitor.entity.ConstantInfo;
import com.xzm.monitor.entity.MonitorInfo;
import com.xzm.monitor.entity.Paging;
import com.xzm.monitor.service.MonitorSimpleService;
import com.xzm.monitor.util.JsonMapper;

import cn.osfeng.money.cache.redis.RedisPool;

@Service
public class MonitorSimpleServiceImpl implements MonitorSimpleService {

	private Logger logger = LoggerFactory.getLogger(MonitorSimpleServiceImpl.class);
	
	private static final Integer ANNOTATION_TAG = 1;
	
	@Autowired
	private RedisPool redisPool;
	

	@Override
	public boolean checkMonitorMethod(MonitorInfo monitorInfo,MethodInvocation invocation) {
		Set<String> sets = null;
		boolean isMonitor = false;
		
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		MonitorInfo info = null;
		try {
			sets = redisPool.getSmembersMe(ConstantInfo.OSFENG_CN_BLOG_MONITOR_METHOD_LIST_TAGE+"test_");
			if(!StringUtils.isEmpty(sets)){
				for (String str : sets) {
					info = jsonMapper.fromJson(str, MonitorInfo.class);
					if(!StringUtils.isEmpty(info.getMethodName())){
						if(info.getMethodName().equals(monitorInfo.getMethodName())){
							isMonitor = true;
							break;
						}
					}
					
				}
			}
			if(!isMonitor){
				isMonitor = invocation.getMethod().isAnnotationPresent(MethodMonitor.class);
	        	logger.debug("methodName:{}  ,是否注解:{}",invocation.getMethod().getName(),isMonitor);
	        	if(isMonitor){
	        		addMonitorMethod(new MonitorInfo(invocation.getMethod().getDeclaringClass().getSimpleName(), invocation.getMethod().getName(),ANNOTATION_TAG));
	        	}
			}
		} catch (Exception e) {
			logger.error("checkMonitorMethod 异常：{}",e);
		}
		logger.debug("================  methodName:{} contains:{}",monitorInfo.getMethodName(),sets.contains(monitorInfo.getMethodName()));
		return isMonitor;
	}

	@Override
	public Paging<MonitorInfo> getMonitorMethods(Paging<MonitorInfo> paging,MonitorInfo monitorInfo) {
		if(StringUtils.isEmpty(paging)){
			paging = new Paging<MonitorInfo>();
		}
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		List<String> methods = new ArrayList<String>();
		List<MonitorInfo> methodsBeans = new ArrayList<MonitorInfo>();
		Integer counts = 0;
		try {
			counts = Integer.valueOf(redisPool.getScardMe(ConstantInfo.OSFENG_CN_BLOG_MONITOR_METHOD_LIST_TAGE+"test_") + "");
			methods = redisPool.getListByNum(ConstantInfo.OSFENG_CN_BLOG_MONITOR_METHOD_LIST_TAGE+"test_", paging.getStartPage(), paging.getStartPage()+paging.getPageSize(),null,null);
			if(!StringUtils.isEmpty(methods)){
				for (String method : methods) {
					methodsBeans.add(jsonMapper.fromJson(method, MonitorInfo.class));
				}
			}
		} catch (Exception e) {
			logger.error("getMonitorMethods 异常：{}",e);
		}
		paging.setObjects(methodsBeans);
		paging.setCounts(counts);
		return paging;
	}

	@Override
	public boolean addMonitorMethod(MonitorInfo monitorInfo) {
		try {
			JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
			monitorInfo.setProcessTime(new Date());
			redisPool.setSaddMe(ConstantInfo.OSFENG_CN_BLOG_MONITOR_METHOD_LIST_TAGE+"test_", jsonMapper.toJson(monitorInfo));
		} catch (Exception e) {
			logger.error("addMonitorMethod 异常：{}",e);
			return false;
		}
		return true;
	}

	@Override
	public boolean removeMonitorMethod(MonitorInfo monitorInfo) {
		try {
			redisPool.delSremMe(ConstantInfo.OSFENG_CN_BLOG_MONITOR_METHOD_LIST_TAGE+"test_", monitorInfo.getMethodName());
			
		} catch (Exception e) {
			logger.error("removeMonitorMethod 异常：{}",e);
			return false;
		}
		return true;
	}

	@Override
	public boolean addMethodProcessInfo(MonitorInfo monitorInfo) {
		try {
			JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
//			redisPool.setLPushByPipelining(key, value);
			redisPool.setZaddByPipelining(ConstantInfo.OSFENG_CN_BLOG_MONITOR_METHOD_TAGE + monitorInfo.getMethodName(), monitorInfo.getProcessTime().getTime(), jsonMapper.toJson(monitorInfo));
		} catch (Exception e) {
			logger.error("addMethodProcessInfo 异常：{}",e);
			return false;
		}
		return true;
	}

	@Override
	public Paging<MonitorInfo> getMonitorMethodProcrssInfo(
			Paging<MonitorInfo> paging, MonitorInfo monitorInfo) {
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		Set<String> methods = new HashSet<String>();
		List<MonitorInfo> monitorInfos = new ArrayList<MonitorInfo>();
		Integer counts = 0;
		if (StringUtils.isEmpty(paging)) {
			paging = new Paging<MonitorInfo>();
		}else{
			try {
				counts = Integer.valueOf(redisPool.getZcardMe(ConstantInfo.OSFENG_CN_BLOG_MONITOR_METHOD_TAGE + monitorInfo.getMethodName()) + "");
				methods = redisPool.getZrangeMe(ConstantInfo.OSFENG_CN_BLOG_MONITOR_METHOD_TAGE + monitorInfo.getMethodName(), counts-35,counts);
				if(!StringUtils.isEmpty(methods)){
					for (String method : methods) {
						monitorInfos.add(jsonMapper.fromJson(method, MonitorInfo.class));
					}
				}
				
			} catch (Exception e) {
				logger.error("getMonitorMethodProcrssInfo 异常：{}",e);
			}
			paging = new Paging<MonitorInfo>(counts, monitorInfos, monitorInfo);
		}
		
		return paging;
	}


}
