<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../imports.jspf" %>

<fmt:message key="main.title" var="title"/>
<fmt:message key="main.distribution" var="distribution"/>
<fmt:message key="main.greeting" var="greeting"/>
<fmt:message key="header.brand" var="brand"/>
<fmt:message key="header.invalid_login_message" var="invalid_message"/>
<fmt:message key="header.valid_login_message" var="valid_message"/>

<%--@elvariable id="authentication_result" type="java.lang.Boolean"--%>

<!DOCTYPE html>
<html>
<header>
    <%@include file="../header/header.jsp" %>
</header>
<head>
    <script src="${abs}/js/message.js"></script>
    <link rel="stylesheet" href="${abs}/css/main.css">
    <title>${title}</title>
</head>
<body>
<c:choose>
    <c:when test="${authentication_result eq 'false'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${invalid_message}</b></div></c:when>
    <c:when test="${authentication_result eq 'true'}"><div class="alert alert-success" id="message"><b class="valid_message">${valid_message} ${user.login}</b></div></c:when>
</c:choose>
<h1>${brand}</h1>
<p>
    <c:choose>
        <c:when test="${user.role eq 'GUEST'}">${greeting}</c:when>
        <c:otherwise>${distribution}</c:otherwise>
    </c:choose>
</p>
<footer>
    <%@include file="../footer/footer.jsp" %>
</footer>
</body>
</html>