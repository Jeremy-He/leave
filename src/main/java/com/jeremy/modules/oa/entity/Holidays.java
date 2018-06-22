package com.jeremy.modules.oa.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeremy.common.utils.excel.fieldtype.DateType;
import com.jeremy.common.persistence.DataEntity;
import com.jeremy.common.utils.excel.annotation.ExcelField;

/**
 * 法定节假日配置Entity
 * @author Jeremy
 * @version 2018-06-21
 */
public class Holidays extends DataEntity<Holidays> {
	
	private static final long serialVersionUID = 1L;
	private Date startDate;		// 开始日期
	private Date endDate;		// 结束日期
	private Integer type;		// 类型：0-法定假日，1-法定工作日
	private String year;        // 年份查询条件
	
	public Holidays() {
		super();
	}

	public Holidays(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="开始日期", align=1, sort=3, fieldType = DateType.class)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	@ExcelField(title="结束日期", align=1, sort=4, fieldType = DateType.class)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ExcelField(title="类型", align=1, sort=2, dictType = "holiday_type")
	public Integer getType() {
		return type;
	}

	@Override
	@ExcelField(title="名称", align=1, sort=1)
	public String getRemarks() {
		return super.getRemarks();
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}