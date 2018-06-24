<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
                    countLeaveEndDate();
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
            $("#realityStartTime").change(countLeaveEndDate);
            $("#realityLeaveDays").change(countLeaveEndDate);
		});
		function countLeaveEndDate() {
            var startTime = $("#realityStartTime").val();
            var applyLeaveDays = $("#realityLeaveDays").val();
            var countType = '${leave.countType}';
            if (startTime === '' || applyLeaveDays === '' || countType === '' || countType === undefined) {
                return;
			}
            $.ajax({
				url: "${ctx}/oa/holidays/countLeaveEndDate",
				type: "post",
				async: false,
				data: {
                    startTime: startTime,
                    applyLeaveDays: applyLeaveDays,
                    countType: countType
                },
				success:function (endTime) {
                    $("#realityEndTime").val(endTime);
                }
			});
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/leave/">待办任务</a></li>
		<li><a href="${ctx}/oa/leave/list">所有任务</a></li>
		<shiro:hasPermission name="oa:leave:edit"><li class="active"><a>销假</a></li></shiro:hasPermission>
	</ul>
	<form:form id="inputForm" modelAttribute="leave" action="${ctx}/oa/leave/reportBack" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">请假类型：</label>
			<div class="controls">
				${fns:getDictLabel(leave.leaveType, 'oa_leave_type', '')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<fmt:formatDate value="${leave.startTime}" pattern="yyyy-MM-dd"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<fmt:formatDate value="${leave.endTime}" pattern="yyyy-MM-dd"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否排除休假日：</label>
			<div class="controls">
				${fns:getDictLabel(leave.countType, 'yes_no', '')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申请天数：</label>
			<div class="controls">
				${leave.applyLeaveDays}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">批准天数：</label>
			<div class="controls">
				${leave.giveLeaveDays}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请假原因：</label>
			<div class="controls">
				${leave.reason}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">实际开始时间：</label>
			<div class="controls">
				<input id="realityStartTime" name="realityStartTime" type="text" readonly="readonly" maxlength="20" class="Wdate required"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,onpicked:countLeaveEndDate});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">实际请假天数：</label>
			<div class="controls">
				<input id="realityLeaveDays" name="realityLeaveDays" type="number" maxlength="20" class="required digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">实际结束时间：</label>
			<div class="controls">
				<input id="realityEndTime" name="realityEndTime" type="text" readonly="readonly" maxlength="20" class="required"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="oa:leave:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
