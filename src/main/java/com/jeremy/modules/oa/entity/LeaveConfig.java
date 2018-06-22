package com.jeremy.modules.oa.entity;


import com.jeremy.common.persistence.DataEntity;

/**
 * 年休假规则Entity
 * @author jeremy
 * @version 2018-06-17
 */
public class LeaveConfig extends DataEntity<LeaveConfig> {
	
	private static final long serialVersionUID = 1L;
	private Double minSeniority;		// 工龄大于或等于（单位：年）
	private Double maxSeniority;		// 工龄小于或等于（单位：年）
	private Integer leaveDays;		// 可休年假天数
	
	public LeaveConfig() {
		super();
	}

	public LeaveConfig(String id){
		super(id);
	}

	public Double getMinSeniority() {
		return minSeniority;
	}

	public void setMinSeniority(Double minSeniority) {
		this.minSeniority = minSeniority;
	}
	
	public Double getMaxSeniority() {
		return maxSeniority;
	}

	public void setMaxSeniority(Double maxSeniority) {
		this.maxSeniority = maxSeniority;
	}
	
	public Integer getLeaveDays() {
		return leaveDays;
	}

	public void setLeaveDays(Integer leaveDays) {
		this.leaveDays = leaveDays;
	}
	
}