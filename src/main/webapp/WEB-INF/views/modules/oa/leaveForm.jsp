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
            $("#startTime").change(countLeaveEndDate);
            $("#applyLeaveDays").change(countLeaveEndDate);
            $("input[name='countType']").click(countLeaveEndDate);
		});
		function countLeaveEndDate() {
            var startTime = $("#startTime").val();
            var applyLeaveDays = $("#applyLeaveDays").val();
            var countType = $("input[name='countType']:checked").val();
            if (startTime === '' || applyLeaveDays === '' || countType === '') {
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
                    $("#endTime").val(endTime);
                }
			});
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/leave/">待办任务</a></li>
		<li><a href="${ctx}/oa/leave/list">所有任务</a></li>
		<shiro:hasPermission name="oa:leave:edit"><li class="active"><a href="${ctx}/oa/leave/form">请假申请</a></li></shiro:hasPermission>
	</ul>
	<form:form id="inputForm" modelAttribute="leave" action="${ctx}/oa/leave/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">请假类型：</label>
			<div class="controls">
				<form:select path="leaveType" cssClass="input-medium">
					<form:options items="${fns:getDictList('oa_leave_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<input id="startTime" name="startTime" type="text" readonly="readonly" maxlength="20" class="Wdate required"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,onpicked:countLeaveEndDate});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请假天数：</label>
			<div class="controls">
				<input id="applyLeaveDays" name="applyLeaveDays" type="text" maxlength="20" class="required number"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否排除休假日：</label>
			<div class="controls">
				<input id="countType1" name="countType" type="radio" value="1" maxlength="20" class="required number"/><label for="countType1">是</label>
				<input id="countType0" name="countType" type="radio" value="0" maxlength="20" class="required number"/><label for="countType0">否</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<input id="endTime" name="endTime" type="text" readonly="readonly" maxlength="20" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请假原因：</label>
			<div class="controls">
				<form:textarea path="reason" class="required input-xxlarge" rows="5" maxlength="120"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="oa:leave:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
