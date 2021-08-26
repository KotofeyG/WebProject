<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../imports.jspf" %>

<fmt:message key="error.title_500" var="title"/>
<fmt:message key="error.internal_error" var="internal_error"/>
<fmt:message key="error.to_main" var="home"/>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${abs}/css/error500.css">
    <title>${title}</title>
</head>
<body>
<div>
    <h1>${title}</h1>
    <h2>${internal_error}</h2>
    <h4>${requestScope['jakarta.servlet.error.message']}</h4>
    <a href="${abs}/controller?command=go_to_main_command"><h4>${home}</h4></a>
</div>
</body>
</html>
