package com.xzm.monitor.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.StringUtils;

import cn.osfeng.money.entity.solr.MetaCondition;

/**
 * 分页
 * 
 * @author xuzhenmin
 * @version 2014-7-3 上午10:35:43
 * 
 * @param <T>
 */
public class Paging<T> implements Serializable {

	/**
	 * @author xuzhenmin
	 * @version 2014-7-3 上午10:37:25
	 */
	private static final long serialVersionUID = 1L;

	private T t;

	/**
	 * solr查询分页
	 */
	private Integer searchPage = 0;

	/**
	 * 起始页
	 */
	private Integer startPage = 0;
	//
	private int currentPage = 1;
	// 页面大小
	private int pageSize = 10;
	// 总页数
	@SuppressWarnings("unused")
	private int pageCounts;
	// 总条数
	private Integer counts;

	private List<T> objects;

	/**
	 * solr facet
	 */
	private List<MetaCondition> conditions;

	public Paging() {

	}

	public Paging(Integer startPage, Integer pageSize) {
		this.pageSize = pageSize;
		if (StringUtils.isEmpty(startPage) || startPage.intValue() < 1) {
			this.startPage = 0;
			this.currentPage = 1;
		} else {
			this.currentPage = startPage;
			this.startPage = (startPage - 1) * pageSize;
		}

	}

	public Paging(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Paging(Integer counts, List<T> objects, T t) {

		this.counts = counts;
		this.objects = objects;
		this.t = t;

	}

	public Integer getStartPage() {

		return startPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageCounts(int pageCounts) {
		this.pageCounts = pageCounts;
	}

	// 处理起始数据
	public void setStartPage(Integer startPage) {

		if (StringUtils.isEmpty(startPage) || startPage.intValue() < 1) {
			this.startPage = 0;
			this.currentPage = 1;
		} else {
			this.currentPage = startPage;
			this.startPage = (startPage - 1) * pageSize;
		}
	}

	public Integer getCurrentPage() {
		if (currentPage < 1) {
			currentPage = 1;
		}
		return currentPage;
	}

	public Integer getPageCounts() {
		if (pageSize == 0 || counts == null || counts == 0)
			return 0;
		if (counts % pageSize != 0) {
			return counts / pageSize + 1;
		} else {
			return counts / pageSize;
		}
	}

	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	public List<T> getObjects() {
		return objects;
	}

	public void setObjects(List<T> objects) {
		this.objects = objects;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public Integer getSearchPage() {
		return searchPage;
	}

	public void setSearchPage(Integer searchPage) {
		if (searchPage < 1) {
			this.currentPage = 1;
		} else {
			this.currentPage = searchPage;
		}

		this.searchPage = searchPage;
	}

	public List<MetaCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<MetaCondition> conditions) {
		this.conditions = conditions;
	}

}
