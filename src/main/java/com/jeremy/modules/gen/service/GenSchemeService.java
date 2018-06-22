
package com.jeremy.modules.gen.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jeremy.modules.gen.dao.GenSchemeDao;
import com.jeremy.modules.gen.dao.GenTableColumnDao;
import com.jeremy.modules.gen.dao.GenTableDao;
import com.jeremy.modules.gen.entity.GenConfig;
import com.jeremy.modules.gen.entity.GenTable;
import com.jeremy.modules.gen.entity.GenTableColumn;
import com.jeremy.modules.gen.entity.GenTemplate;
import com.jeremy.modules.gen.util.GenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremy.common.persistence.Page;
import com.jeremy.common.service.BaseService;
import com.jeremy.common.utils.StringUtils;
import com.jeremy.modules.gen.dao.GenSchemeDao;
import com.jeremy.modules.gen.dao.GenTableColumnDao;
import com.jeremy.modules.gen.dao.GenTableDao;
import com.jeremy.modules.gen.entity.GenConfig;
import com.jeremy.modules.gen.entity.GenScheme;
import com.jeremy.modules.gen.entity.GenTable;
import com.jeremy.modules.gen.entity.GenTableColumn;
import com.jeremy.modules.gen.entity.GenTemplate;
import com.jeremy.modules.gen.util.GenUtils;

/**
 * 生成方案Service
 * @author ThinkGem
 * @version 2013-10-15
 */
@Service
@Transactional(readOnly = true)
public class GenSchemeService extends BaseService {

	@Autowired
	private GenSchemeDao genSchemeDao;
//	@Autowired
//	private GenTemplateDao genTemplateDao;
	@Autowired
	private GenTableDao genTableDao;
	@Autowired
	private GenTableColumnDao genTableColumnDao;
	
	public GenScheme get(String id) {
		return genSchemeDao.get(id);
	}
	
	public Page<GenScheme> find(Page<GenScheme> page, GenScheme genScheme) {
		GenUtils.getTemplatePath();
		genScheme.setPage(page);
		page.setList(genSchemeDao.findList(genScheme));
		return page;
	}
	
	@Transactional(readOnly = false)
	public String save(GenScheme genScheme) {
		if (StringUtils.isBlank(genScheme.getId())){
			genScheme.preInsert();
			genSchemeDao.insert(genScheme);
		}else{
			genScheme.preUpdate();
			genSchemeDao.update(genScheme);
		}
		// 生成代码
		if ("1".equals(genScheme.getFlag())){
			return generateCode(genScheme);
		}
		return "";
	}
	
	@Transactional(readOnly = false)
	public void delete(GenScheme genScheme) {
		genSchemeDao.delete(genScheme);
	}
	
	private String generateCode(GenScheme genScheme){

		StringBuilder result = new StringBuilder();
		
		// 查询主表及字段列
		GenTable genTable = genTableDao.get(genScheme.getGenTable().getId());
		genTable.setColumnList(genMjdbcType(genTableColumnDao.findList(new GenTableColumn(new GenTable(genTable.getId())))));
		
		// 获取所有代码模板
		GenConfig config = GenUtils.getConfig();
		
		// 获取模板列表
		List<GenTemplate> templateList = GenUtils.getTemplateList(config, genScheme.getCategory(), false);
		List<GenTemplate> childTableTemplateList = GenUtils.getTemplateList(config, genScheme.getCategory(), true);
		
		// 如果有子表模板，则需要获取子表列表
		if (childTableTemplateList.size() > 0){
			GenTable parentTable = new GenTable();
			parentTable.setParentTable(genTable.getName());
			genTable.setChildList(genTableDao.findList(parentTable));
		}
		
		// 生成子表模板代码
		for (GenTable childTable : genTable.getChildList()){
			childTable.setParent(genTable);
			childTable.setColumnList(genMjdbcType(genTableColumnDao.findList(new GenTableColumn(new GenTable(childTable.getId())))));
			genScheme.setGenTable(childTable);
			Map<String, Object> childTableModel = GenUtils.getDataModel(genScheme);
			for (GenTemplate tpl : childTableTemplateList){
				result.append(GenUtils.generateToFile(tpl, childTableModel, genScheme.getReplaceFile()));
			}
		}
		
		// 生成主表模板代码
		genScheme.setGenTable(genTable);
		Map<String, Object> model = GenUtils.getDataModel(genScheme);
		for (GenTemplate tpl : templateList){
			System.out.println(tpl.getFilePath()+"=="+tpl.getFileName());
			result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
		}
		return result.toString();
	}
	
	public List<GenTableColumn> genMjdbcType(List<GenTableColumn> findList){
		List<GenTableColumn> columns = new LinkedList<GenTableColumn>();
		for (GenTableColumn column : findList) {
			System.out.println(column.getName());
			// 设置java类型
			if (column.getJavaType().equalsIgnoreCase("String")){
				column.setJdbcType("VARCHAR");
			}
			else if(column.getJavaType().equalsIgnoreCase("java.util.Date")){
				column.setJdbcType("TIMESTAMP");
			}
			else{
				column.setJdbcType("VARCHAR");
			}
			column.setJdbcType(column.getJdbcType().toUpperCase());
			System.out.println(column.getJdbcType());
			columns.add(column);
		}
		return columns;
	}
}
