<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../imports.jspf" %>

<fmt:message key="registration.title" var="title"/>
<fmt:message key="registration.sign_up_1" var="sign_up"/>
<fmt:message key="registration.login" var="login"/>
<fmt:message key="registration.password" var="psw"/>
<fmt:message key="registration.confirm_password" var="confirm_psw"/>
<fmt:message key="registration.email" var="email"/>
<fmt:message key="registration.mobile_number" var="mobile_number"/>
<fmt:message key="registration.invalid_login" var="invalid_login_message"/>
<fmt:message key="registration.invalid_passport" var="invalid_psw_message"/>
<fmt:message key="registration.invalid_email" var="invalid_email_message"/>
<fmt:message key="registration.invalid_mobile_number" var="invalid_mobile_number_message"/>
<fmt:message key="registration.not_unique_login" var="not_unique_login_message"/>
<fmt:message key="registration.password_mismatch" var="psw_mismatch_message"/>
<fmt:message key="registration.not_unique_email" var="not_unique_email_message"/>
<fmt:message key="registration.not_unique_mobile_number" var="not_unique_mobile_number_message"/>

<%--@elvariable id="valid_login" type="java.lang.String"--%>
<%--@elvariable id="valid_email" type="java.lang.String"--%>
<%--@elvariable id="valid_mobile_number" type="java.lang.String"--%>
<%--@elvariable id="invalid_login" type="java.lang.String"--%>
<%--@elvariable id="invalid_passport" type="java.lang.String"--%>
<%--@elvariable id="invalid_email" type="java.lang.String"--%>
<%--@elvariable id="invalid_mobile_number" type="java.lang.String"--%>

<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>
<%@include file="../header/header.jsp" %>
<div class="container-fluid">
    <form action="${abs}/controller" method="post">
        <input type="hidden" name="command" value="registration_command">
        <label for="login">${login}</label><br/>
        <input type="text" id="login" name="login" value="${valid_login}" placeholder="${login}">
        <c:choose>
            <c:when test="${invalid_login eq 'invalid_message'}">${invalid_login_message}</c:when>
            <c:when test="${invalid_login eq 'not_unique_message'}">${not_unique_login_message}</c:when>
        </c:choose>
        <br/><br/>
        <label for="password">${psw}</label><br/>
        <input type="password" id="password" name="password" placeholder="${psw}">
        <c:if test="${invalid_passport eq 'invalid_message'}">${invalid_psw_message}</c:if>
        <br/><br/>
        <label for="confirm-password">${confirm_psw}</label><br/>
        <input type="password" id="confirm-password" name="confirm_password" placeholder="${confirm_psw}">
        <c:if test="${invalid_passport eq 'password_mismatch'}">${psw_mismatch_message}</c:if>
        <br/><br/>
        <label for="email-address">${email}</label><br/>
        <input type="email" id="email-address" name="email_address" value="${valid_email}" placeholder="${email}">
        <c:choose>
            <c:when test="${invalid_email eq 'invalid_message'}">${invalid_email_message}</c:when>
            <c:when test="${invalid_email eq 'not_unique_message'}">${not_unique_email_message}</c:when>
        </c:choose>
        <br/><br/>
        <label for="mobile_number">${mobile_number}</label><br/>
        <input type="tel" id="mobile_number" name="mobile_number" value="${valid_mobile_number}" placeholder="${mobile_number}">
        <c:choose>
            <c:when test="${invalid_mobile_number eq 'invalid_message'}">${invalid_mobile_number_message}</c:when>
            <c:when test="${invalid_mobile_number eq 'not_unique_message'}">${not_unique_mobile_number_message}</c:when>
        </c:choose>
        <br/><br/>
        <input type="submit" name="submit" value="${sign_up}"><br/>
    </form>
</div>
<%@include file="../footer/footer.jsp" %>
</body>
</html>