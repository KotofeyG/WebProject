<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="en-US" />
<fmt:setBundle basename="localization/locale" />

<fmt:message key="sign_up.invalid_login" var="inv_login"/>
<fmt:message key="sign_up.not_unique_login" var="not_un_login"/>
<fmt:message key="sign_up.invalid_passport" var="inv_pass"/>
<fmt:message key="sign_up.password_mismatch" var="pass_mismatch"/>
<fmt:message key="sign_up.invalid_email" var="inv_email"/>
<fmt:message key="sign_up.not_unique_email" var="not_un_email"/>
<fmt:message key="sign_up.invalid_mobile_number" var="inv_mob"/>
<fmt:message key="sign_up.not_unique_mobile_number" var="not_un_mob"/>

<!DOCTYPE html>
<html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <form action="controller" method="post">
            <input type="hidden" name="command" value="registration">
            Login:<input type="text" name="login" value="${registration_data.login}">
            <c:choose>
                <c:when test="${login_error eq 'invalid_login'}">${inv_login}</c:when>
                <c:when test="${login_error eq 'not_unique_login'}">${not_un_login}</c:when>
            </c:choose>
            <br/>
            Password:<input type="password" name="password"><br/>
            Confirm password:<input type="password" name="confirm_password">
            <c:choose>
                <c:when test="${passport_error eq 'invalid_passport'}">${inv_pass}</c:when>
                <c:when test="${passport_error eq 'password_mismatch'}">${pass_mismatch}</c:when>
            </c:choose>
            <br/>
            Email:<input type="email" name="email_address" value="${registration_data.email}">
            <c:choose>
                <c:when test="${email_error eq 'invalid_email'}">${inv_email}</c:when>
                <c:when test="${email_error eq 'not_unique_email'}">${not_un_email}</c:when>
            </c:choose>
            <br/>
            Mobile number:<input type="tel" name="mobile_number" value="${registration_data.mobileNumber}">
            <c:choose>
                <c:when test="${mobile_number_error eq 'invalid_mobile_number'}">${inv_mob}</c:when>
                <c:when test="${mobile_number_error eq 'not_unique_mobile_number'}">${not_un_mob}</c:when>
            </c:choose>
            <br/>
            <input type="submit" name="submit" value="submit"><br/>
        </form>
    </body>
</html>
