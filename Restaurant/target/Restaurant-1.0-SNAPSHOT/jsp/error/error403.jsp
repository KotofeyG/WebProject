<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../imports.jspf" %>

<fmt:message key="error.title_403" var="title"/>
<fmt:message key="error.to_main" var="home"/>
<fmt:message key="error.forbidden_action" var="forbidden_action"/>
<fmt:message key="error.account_blockage_message" var="account_blockage_message"/>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${abs}/css/error403.css">
    <title>${title}</title>
</head>
<body>
<div>
    <h1>${title}</h1>
    <h2>${forbidden_action}</h2>
    <c:choose>
        <c:when test="${requestScope['jakarta.servlet.error.message'] eq 'account_blockage_message'}"><h4>${account_blockage_message}</h4></c:when>
    </c:choose>
    <a href="${abs}/controller?command=go_to_main_command"><h4>${home}</h4></a>
</div>
</body>
</html>