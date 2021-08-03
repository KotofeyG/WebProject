<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../imports.jspf" %>

<fmt:message key="main.title" var="title"/>

<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${abs}/css/background.css">
    <title>${title}</title>
</head>
<body>
<header><%@include file="../header/header.jsp" %></header>
<div class="container-fluid">
    <div class="row">
    </div>
</div>
<footer><%@include file="../footer/footer.jsp" %></footer>
</body>
</html>