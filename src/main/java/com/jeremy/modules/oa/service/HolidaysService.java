package com.jeremy.modules.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremy.common.persistence.Page;
import com.jeremy.common.service.CrudService;
import com.jeremy.modules.oa.entity.Holidays;
import com.jeremy.modules.oa.dao.HolidaysDao;

/**
 * 法定节假日配置Service
 * @author Jeremy
 * @version 2018-06-21
 */
@Service
@Transactional(readOnly = true)
public class HolidaysService extends CrudService<HolidaysDao, Holidays> {

	public Holidays get(String id) {
		return super.get(id);
	}
	
	public List<Holidays> findList(Holidays holidays) {
		return super.findList(holidays);
	}
	
	public Page<Holidays> findPage(Page<Holidays> page, Holidays holidays) {
		return super.findPage(page, holidays);
	}
	
	@Transactional(readOnly = false)
	public void save(Holidays holidays) {
		super.save(holidays);
	}
	
	@Transactional(readOnly = false)
	public void delete(Holidays holidays) {
		super.delete(holidays);
	}
	
}