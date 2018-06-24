<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假审批</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li class="active"><a href="#"><shiro:hasPermission name="oa:leave:edit">${leave.act.taskName}</shiro:hasPermission><shiro:lacksPermission name="oa:leave:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="leave" action="${ctx}/oa/leave/saveAudit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
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
						<input id="giveLeaveDays" name="giveLeaveDays" type="number" class="required digits input-mini" value="${leave.giveLeaveDays}" style="margin: 0;" />
					</td>
					<td class="tit">是否上级审批</td>
					<td>
						<input id="isNeedParentAudit1" name="isNeedParentAudit" type="radio" value="1" class="required" checked style="margin: 0;" /><label for="isNeedParentAudit1">是</label>&nbsp;
						<input id="isNeedParentAudit0" name="isNeedParentAudit" type="radio" value="0" class="required" style="margin: 0;" /><label for="isNeedParentAudit0">否</label>
					</td>
				</tr>
				<tr>
					<td class="tit">请假原因</td>
					<td colspan="5">${leave.reason}</td>
				</tr>
				<tr>
					<td class="tit">您的意见</td>
					<td colspan="5">
						<form:textarea path="act.comment" class="required" rows="5" maxlength="20" cssStyle="width:500px"/>
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="form-actions">
			<shiro:hasPermission name="oa:leave:edit">
				<c:if test="${leave.act.taskDefKey eq 'reportBack'}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="销 假" onclick="$('#flag').val('yes')"/>&nbsp;
				</c:if>
				<c:if test="${leave.act.taskDefKey ne 'reportBack'}">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes')"/>&nbsp;
					<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no')"/>&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<act:histoicFlow procInsId="${leave.act.procInsId}"/>
	</form:form>
</body>
</html>
