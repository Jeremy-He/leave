<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>审批详情</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li class="active"><a href="${ctx}/oa/leave/form?id=${leave.id}">审批详情</a></li>
	</ul>
	<sys:message content="${message}"/>
	<form class="form-horizontal">
		<fieldset>
			<legend>${testAudit.act.taskName}</legend>
			<table class="table-form">
				<tr>
					<td class="tit">申请人</td>
					<td>${leave.createBy.name}</td>
					<td class="tit">部门</td>
					<td>${leave.createBy.office.name}</td>
					<td class="tit">请假类型</td>
					<td>${fns:getDictLabel(leave.leaveType, 'oa_leave_type', '')}</td>
				</tr>
				<tr>
					<td class="tit">申请时间</td>
					<td><fmt:formatDate value="${leave.createDate}" type="both"/></td>
					<td class="tit">开始日期</td>
					<td><fmt:formatDate value="${leave.startTime}" pattern="yyyy-MM-dd"/></td>
					<td class="tit">结束日期</td>
					<td><fmt:formatDate value="${leave.endTime}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<td class="tit">申请天数</td>
					<td>${leave.applyLeaveDays}</td>
					<td class="tit">批准天数</td>
					<td>
						${leave.giveLeaveDays}
					</td>
					<td class="tit">实际请假天数</td>
					<td>
						${leave.realityLeaveDays}
					</td>
				</tr>
				<tr>
					<td class="tit">是否排序休假日</td>
					<td>
						${fns:getDictLabel(leave.countType, 'yes_no', '')}
					</td>
					<td class="tit">实际开始日期</td>
					<td><fmt:formatDate value="${leave.realityStartTime}" pattern="yyyy-MM-dd"/></td>
					<td class="tit">实际日期</td>
					<td><fmt:formatDate value="${leave.realityEndTime}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<td class="tit">请假原因</td>
					<td colspan="5">${leave.reason}</td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<act:histoicFlow procInsId="${leave.act.procInsId}"/>
	</form>
</body>
</html>
