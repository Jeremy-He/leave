<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假一览</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        $(document).ready(function() {
            $("#btnExport").click(function(){
                top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                    if(v=="ok"){
                        $("#searchForm").attr("action","${ctx}/oa/leave/export");
                        $("#searchForm").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });
        });
		function page(n,s){
            if(n) $("#pageNo").val(n);
            if(s) $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/oa/leave/count");
            $("#searchForm").submit();
            return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/leave/count">请假统计</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="leave" action="${ctx}/oa/leave/count" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div style="margin-top:8px;">
			<label>归属部门：</label><sys:treeselect id="office" name="office.id" value="${leave.office.id}" labelName="office.name" labelValue="${leave.office.name}"
													title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true"/>
			<label>创建时间：</label>
			<input id="createDateStart"  name="createDateStart"  type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${leave.createDateStart}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
				　--　
			<input id="createDateEnd" name="createDateEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${leave.createDateEnd}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			&nbsp;<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
			<th>请假类型</th>
			<th>请假人</th>
			<th>申请时间</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>是否排除休假日</th>
			<th>申请天数</th>
			<th>批准天数</th>
			<th>实际开始时间</th>
			<th>实际结束时间</th>
			<th>实际请假天数</th>
			<th>请假原因</th>
			<th>状态</th>
			<th>操作</th>
		</tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="leave">
			<tr>
				<td>${fns:getDictLabel(leave.leaveType, 'oa_leave_type', '')}</td>
				<td>${leave.createBy.name}</td>
				<td><fmt:formatDate value="${leave.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${leave.startTime}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${leave.endTime}" pattern="yyyy-MM-dd"/></td>
				<td>${fns:getDictLabel(leave.countType, 'yes_no', '')}</td>
				<td>${leave.applyLeaveDays}</td>
				<td>${leave.giveLeaveDays}</td>
				<td><fmt:formatDate value="${leave.realityStartTime}" pattern="yyyy-MM-dd"/></td>
				<td><fmt:formatDate value="${leave.realityEndTime}" pattern="yyyy-MM-dd"/></td>
				<td>${leave.realityLeaveDays}</td>
				<td title="${leave.reason}">${fns:abbr(leave.reason,18)}</td>
				<td>${fns:getDictLabel(leave.status, 'oa_leave_status', '')}</td>
				<td><a href="${ctx}/oa/leave/form?id=${leave.id}">详情</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
