<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>客户设备管理</title>
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
		<li class="active"><a href="${ctx}/cusdev/cusdev/">客户设备列表</a></li>
		<shiro:hasPermission name="cusdev:cusdev:edit"><li><a href="${ctx}/cusdev/cusdev/form">客户设备添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cusdev" action="${ctx}/cusdev/cusdev/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>设备名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>设备编码：</label>
				<form:input path="code" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>所属客户：</label>
				<form:input path="customerid" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>申请单：</label>
				<form:input path="applyid" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>设备类型：</label>
				<form:input path="type" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>机架ID：</label>
				<form:input path="rackid" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>IP：</label>
				<form:input path="ip" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>设备名称</th>
				<shiro:hasPermission name="cusdev:cusdev:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cusdev">
			<tr>
				<td><a href="${ctx}/cusdev/cusdev/form?id=${cusdev.id}">
					${cusdev.name}
				</a></td>
				<shiro:hasPermission name="cusdev:cusdev:edit"><td>
    				<a href="${ctx}/cusdev/cusdev/form?id=${cusdev.id}">修改</a>
					<a href="${ctx}/cusdev/cusdev/delete?id=${cusdev.id}" onclick="return confirmx('确认要删除该客户设备吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>