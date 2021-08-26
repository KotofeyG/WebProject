<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../imports.jspf" %>

<fmt:message key="error.internal" var="title"/>
<fmt:message key="error.to_main" var="home"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
</head>
<body>
    <b>Внутренняя ошибка сервера</b>
    <div class="container-fluid">
        <a class="navbar-brand" href="${abs}/controller?command=go_to_main_command"><b>${home}</b></a>
    </div>
</body>
</html>