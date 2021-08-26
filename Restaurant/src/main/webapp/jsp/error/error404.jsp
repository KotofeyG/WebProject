<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../imports.jspf" %>

<fmt:message key="error.title_404" var="title"/>
<fmt:message key="error.to_main" var="home"/>
<fmt:message key="error.not_found_resource" var="not_found_resource"/>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${abs}/css/error404.css">
    <title>${title}</title>
</head>
<body>
<div>
    <h1>${title}</h1>
    <h2>${not_found_resource}</h2>
    <a href="${abs}/controller?command=go_to_main_command"><h4>${home}</h4></a>
</div>
</body>
</html>
