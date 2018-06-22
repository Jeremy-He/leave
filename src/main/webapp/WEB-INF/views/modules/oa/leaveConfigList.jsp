<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>年休假规则管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/leaveConfig/">年休假规则列表</a></li>
		<shiro:hasPermission name="oa:leaveConfig:edit"><li><a href="${ctx}/oa/leaveConfig/form">年休假规则添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="leaveConfig" action="${ctx}/oa/leaveConfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>工龄 &gt;=（单位：年）</th>
				<th>工龄 &lt;=（单位：年）</th>
				<th>可休年假天数</th>
				<th>修改时间</th>
				<th>备注</th>
				<shiro:hasPermission name="oa:leaveConfig:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="leaveConfig">
			<tr>
				<td><a href="${ctx}/oa/leaveConfig/form?id=${leaveConfig.id}">
					${leaveConfig.minSeniority}
				</a></td>
				<td>
					${leaveConfig.maxSeniority}
				</td>
				<td>
					${leaveConfig.leaveDays}
				</td>
				<td>
					<fmt:formatDate value="${leaveConfig.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${leaveConfig.remarks}
				</td>
				<shiro:hasPermission name="oa:leaveConfig:edit"><td>
    				<a href="${ctx}/oa/leaveConfig/form?id=${leaveConfig.id}">修改</a>
					<a href="${ctx}/oa/leaveConfig/delete?id=${leaveConfig.id}" onclick="return confirmx('确认要删除该年休假规则吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>