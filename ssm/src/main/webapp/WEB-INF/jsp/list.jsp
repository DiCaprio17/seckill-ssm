<%--
  Created by IntelliJ IDEA.
  User: hwangnuozhong
  Date: 2019/8/21
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>秒杀列表页</title>
    <%@include file="common/head.jsp" %>
</head>
<body>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>秒杀列表</h2>
        </div>

        <div class="panel-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <td>名称</td>
                    <td>库存</td>
                    <td>开始时间</td>
                    <td>结束时间</td>
                    <td>创建时间</td>
                    <td>详情页</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="sk">
                    <tr>
                        <td>${sk.name}</td>
                        <td>${sk.number}</td>
                        <td><fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><a class="btn btn-info" href="/seckill/${sk.seckillId}/detail" target="_blank">详情</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/plugins/jquery.js"></script>
<script src="${pageContext.request.contextPath}/resources/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
</html>
