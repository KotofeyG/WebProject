<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../imports.jspf" %>

<fmt:message key="account_creation.title" var="title"/>
<fmt:message key="account_creation.to_main" var="home"/>
<fmt:message key="account_creation.message" var="message"/>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${abs}/css/account_details.css">
    <title>${title}</title>
</head>
<body>
<div>
    <h3>${message}</h3>
    <a href="${abs}/controller?command=go_to_main_command"><h4>${home}</h4></a>
</div>
</body>
</html>