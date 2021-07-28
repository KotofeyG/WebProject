<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>


<html>
<head>
    <title>Users management</title>
</head>
<body>
<%@include file="../../header/header.jsp" %>
<h1>Users management</h1>
<a href="${abs}/controller?command=find_all_users_command">GET OVER HERE!!!</a>
<table class="table table-dark table-striped table-hover">
    <tr>
        <th>id</th>
        <th>login</th>
        <%--    <th>role</th>--%>
        <%--    <th>state</th>--%>
        <%--    <th>e-mail</th>--%>
        <%--    <th>phone</th>--%>
        <%--    <th>registration date</th>--%>
    </tr>
    <c:forEach items="${all_users}" var="user">
        <tr>
            <td>${user.getUserId()}</td>
            <td>${user.getLogin()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
