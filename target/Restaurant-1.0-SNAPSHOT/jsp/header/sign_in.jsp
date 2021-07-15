<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="abs">${pageContext.request.contextPath}</c:set>

<fmt:setLocale value="ru-RU" />
<fmt:setBundle basename="localization/locale" />

<fmt:message key="sign_in.invalid_login_or_password" var="inv_log_pass"/>
<fmt:message key="sign_in.login" var="sign_in"/>

<html>
    <head><title>Registration</title></head>
    <body>
        <form name="LoginForm" method="post" action="${abs}/controller">
            <input type="hidden" name="command" value="sign_in" />
            <input type="text" name="login" value="" />
            <a href="${abs}/controller?command=go_to_registration_command">
                <button type="button">${registration}</button>
            </a>
            <br/>
            <input type="password" name="password" value="" />
            <c:if test="${not empty login}">${inv_log_pass}</c:if>
            <input type="submit" value="${sign_in}" />
        </form>
    </body>
</html>