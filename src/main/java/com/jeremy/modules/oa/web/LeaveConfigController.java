/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.jeremy.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeremy.common.config.Global;
import com.jeremy.common.persistence.Page;
import com.jeremy.common.web.BaseController;
import com.jeremy.common.utils.StringUtils;
import com.jeremy.modules.oa.entity.LeaveConfig;
import com.jeremy.modules.oa.service.LeaveConfigService;

/**
 * 年休假规则Controller
 * @author jeremy
 * @version 2018-06-17
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/leaveConfig")
public class LeaveConfigController extends BaseController {

	@Autowired
	private LeaveConfigService leaveConfigService;

	@ModelAttribute
	public LeaveConfig get(@RequestParam(required=false) String id) {
		LeaveConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = leaveConfigService.get(id);
		}
		if (entity == null){
			entity = new LeaveConfig();
		}
		return entity;
	}

	@RequiresPermissions("oa:leaveConfig:view")
	@RequestMapping(value = {"list", ""})
	public String list(LeaveConfig leaveConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LeaveConfig> page = leaveConfigService.findPage(new Page<LeaveConfig>(request, response), leaveConfig);
		model.addAttribute("page", page);
		return "modules/oa/leaveConfigList";
	}

	@RequiresPermissions("oa:leaveConfig:view")
	@RequestMapping(value = "form")
	public String form(LeaveConfig leaveConfig, Model model) {
		model.addAttribute("leaveConfig", leaveConfig);
		return "modules/oa/leaveConfigForm";
	}

	@RequiresPermissions("oa:leaveConfig:edit")
	@RequestMapping(value = "save")
	public String save(LeaveConfig leaveConfig, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, leaveConfig)){
			return form(leaveConfig, model);
		}
		leaveConfigService.save(leaveConfig);
		addMessage(redirectAttributes, "保存年休假规则成功");
		return "redirect:"+Global.getAdminPath()+"/oa/leaveConfig/?repage";
	}

	@RequiresPermissions("oa:leaveConfig:edit")
	@RequestMapping(value = "delete")
	public String delete(LeaveConfig leaveConfig, RedirectAttributes redirectAttributes) {
		leaveConfigService.delete(leaveConfig);
		addMessage(redirectAttributes, "删除年休假规则成功");
		return "redirect:"+Global.getAdminPath()+"/oa/leaveConfig/?repage";
	}

}