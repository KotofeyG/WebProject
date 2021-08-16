<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@include file="../imports.jspf" %>

<fmt:message key="error.title_400" var="title"/>
<fmt:message key="error.to_main" var="home"/>
<fmt:message key="error.wrong_command_message" var="wrong_command_message"/>

<%--@elvariable id="wrong_command" type="java.lang.Boolean"--%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${abs}/css/error400.css">
    <title>${title}</title>
</head>
<body>
<div>
    <h1>${title}</h1>
    <c:choose>
        <c:when test="${wrong_command eq 'true'}"><h2>${wrong_command_message}</h2></c:when>
    </c:choose>
    <a href="${abs}/controller?command=go_to_main_command"><h4>${home}</h4></a>
</div>
</body>
</html>
