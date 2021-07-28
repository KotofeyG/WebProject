<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../imports.jspf" %>

<fmt:message key="error.to_main" var="home"/>
<fmt:message key="error.non_existent_command" var="title"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title}</title>
</head>
<body>
    <b>Несуществующая команда</b>
    <div class="container-fluid">
        <a class="navbar-brand" href="${abs}/controller?command=go_to_main_command"><b>${home}</b></a>
    </div>
</body>
</html>