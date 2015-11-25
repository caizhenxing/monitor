package com.xzm.monitor.entity;

import java.io.Serializable;
import java.util.Date;

public class MonitorInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8087107290740900732L;

	public MonitorInfo() {
	}

	public MonitorInfo(String beanName, String methodName) {
		this(beanName, methodName, null);
	}

	public MonitorInfo(String beanName, String methodName, Integer monitorType) {
		this.monitorType = monitorType;
		this.beanName = beanName;
		this.methodName = methodName;
	}

	// 执行用时
	private Date executeTime;
	// 方法触发时间
	private Date processTime;
	// 服务器标识
	private String serverAddress;
	// bean+method保证方法唯一
	private String methodName;

	private String beanName;
	// 监控类型(注解、配置等)
	private Integer monitorType;
	// 是否执行成功
	private boolean execSuccess;
	// 执行备注(异常)
	private String remark;

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Integer getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(Integer monitorType) {
		this.monitorType = monitorType;
	}

	public boolean isExecSuccess() {
		return execSuccess;
	}

	public void setExecSuccess(boolean execSuccess) {
		this.execSuccess = execSuccess;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
