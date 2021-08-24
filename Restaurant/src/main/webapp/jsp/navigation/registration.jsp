<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../imports.jspf" %>

<fmt:message key="registration.title" var="registration_title"/>
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
<%@include file="../header/header.jsp" %>
<head>
    <script src="${abs}/js/message.js"></script>
    <link rel="stylesheet" href="${abs}/css/registration.css">
    <title>${title}</title>
</head>
<body>
<h1>${registration_title}</h1>
<div class="container-fluid" id="container-fluid">
    <form action="${abs}/controller" method="post">
        <input type="hidden" name="command" value="registration_command">
        <label for="login">${login}</label><br/>
        <input type="text" id="login" class="form-control" name="login" value="${valid_login}" placeholder="${login}">
        <c:choose>
            <c:when test="${invalid_login eq 'invalid_message'}"><div id="message1"><b>${invalid_login_message}</b></div></c:when>
            <c:when test="${invalid_login eq 'not_unique_message'}"><div id="message1"><b>${not_unique_login_message}</b></div></c:when>
        </c:choose>
        <br/><br/>
        <label for="password">${psw}</label><br/>
        <input type="password" id="password" class="form-control" name="password" placeholder="${psw}">
        <c:if test="${invalid_passport eq 'invalid_message'}"><div id="message2"><b>${invalid_psw_message}</b></div></c:if>
        <br/><br/>
        <label for="confirm-password">${confirm_psw}</label><br/>
        <input type="password" id="confirm-password" class="form-control" name="confirm_password" placeholder="${confirm_psw}">
        <c:if test="${invalid_passport eq 'password_mismatch'}"><div id="message2"><b>${psw_mismatch_message}</b></div></c:if>
        <br/><br/>
        <label for="email-address">${email}</label><br/>
        <input type="email" id="email-address" class="form-control" name="email_address" value="${valid_email}" placeholder="${email}">
        <c:choose>
            <c:when test="${invalid_email eq 'invalid_message'}"><div id="message3"><b>${invalid_email_message}</b></div></c:when>
            <c:when test="${invalid_email eq 'not_unique_message'}"><div id="message3"><b>${not_unique_email_message}</b></div></c:when>
        </c:choose>
        <br/><br/>
        <label for="mobile_number">${mobile_number}</label><br/>
        <input type="tel" id="mobile_number" class="form-control" name="mobile_number" value="${valid_mobile_number}" placeholder="${mobile_number}">
        <c:choose>
            <c:when test="${invalid_mobile_number eq 'invalid_message'}"><div id="message4"><b>${invalid_mobile_number_message}</b></div></c:when>
            <c:when test="${invalid_mobile_number eq 'not_unique_message'}"><div id="message4"><b>${not_unique_mobile_number_message}</b></div></c:when>
        </c:choose>
        <br/><br/>
        <input type="submit" class="form-control" id="sign_up" name="submit" value="${sign_up}"><br/>
    </form>
</div>
<%@include file="../footer/footer.jsp" %>
</body>
</html>