/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.jeremy.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.google.common.collect.Lists;
import com.jeremy.common.beanvalidator.BeanValidators;
import com.jeremy.common.utils.DateUtils;
import com.jeremy.common.utils.excel.ExportExcel;
import com.jeremy.common.utils.excel.ImportExcel;
import com.jeremy.modules.oa.entity.Leave;
import com.jeremy.modules.sys.entity.User;
import com.jeremy.modules.sys.service.SystemService;
import com.jeremy.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeremy.common.config.Global;
import com.jeremy.common.persistence.Page;
import com.jeremy.common.web.BaseController;
import com.jeremy.common.utils.StringUtils;
import com.jeremy.modules.oa.entity.Holidays;
import com.jeremy.modules.oa.service.HolidaysService;

import java.sql.ClientInfoStatus;
import java.util.*;

/**
 * 法定节假日配置Controller
 * @author Jeremy
 * @version 2018-06-21
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/holidays")
public class HolidaysController extends BaseController {

	@Autowired
	private HolidaysService holidaysService;

	@ModelAttribute
	public Holidays get(@RequestParam(required=false) String id) {
		Holidays entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = holidaysService.get(id);
		}
		if (entity == null){
			entity = new Holidays();
		}
		return entity;
	}

	@RequiresPermissions("oa:holidays:view")
	@RequestMapping(value = {"list", ""})
	public String list(Holidays holidays, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Holidays> page = holidaysService.findPage(new Page<Holidays>(request, response), holidays);
		model.addAttribute("page", page);
		return "modules/oa/holidaysList";
	}

	@RequiresPermissions("oa:holidays:view")
	@RequestMapping(value = "form")
	public String form(Holidays holidays, Model model) {
		model.addAttribute("holidays", holidays);
		return "modules/oa/holidaysForm";
	}

	@RequiresPermissions("oa:holidays:edit")
	@RequestMapping(value = "save")
	public String save(Holidays holidays, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, holidays)){
			return form(holidays, model);
		}
		holidaysService.save(holidays);
		addMessage(redirectAttributes, "保存法定节假日配置成功");
		return "redirect:"+Global.getAdminPath()+"/oa/holidays/?repage";
	}

	@RequiresPermissions("oa:holidays:edit")
	@RequestMapping(value = "delete")
	public String delete(Holidays holidays, RedirectAttributes redirectAttributes) {
		holidaysService.delete(holidays);
		addMessage(redirectAttributes, "删除法定节假日配置成功");
		return "redirect:"+Global.getAdminPath()+"/oa/holidays/?repage";
	}

	/**
	 * 导入数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("oa:holidays:edit")
	@RequestMapping(value = "import", method=RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Holidays> list = ei.getDataList(Holidays.class);
			for (Holidays holidays : list) {
				try {
					holidaysService.save(holidays);
					successNum++;
				} catch (Exception e) {
					logger.error(e.getMessage());
					failureMsg.append("<br/> ").append(holidays.getRemarks()).append(" 已存在; ");
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条数据，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条数据"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入数据失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/oa/holidays/list?repage";
	}

	/**
	 * 下载导入数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("oa:holidays:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "节假日配置导入模板.xlsx";
			Holidays example = new Holidays();
			example.setRemarks("例子(请删除)");
			example.setStartDate(Calendar.getInstance().getTime());
			example.setEndDate(Calendar.getInstance().getTime());
			example.setType(0);
			new ExportExcel("节假日配置", Holidays.class, 2).setDataList(Collections.singletonList(example)).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/oa/holidays/list?repage";
	}

	@RequiresPermissions("user")
	@RequestMapping(value = "countLeaveEndDate", method=RequestMethod.POST)
	@ResponseBody
	public String countLeaveEndDate(Leave leave) {
		Date endDate = holidaysService.countLeaveEndDate(leave);
		return DateUtils.formatDate(endDate, "yyyy-MM-dd");
	}

}