<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="abs">${pageContext.request.contextPath}</c:set>

<fmt:setLocale value="ru-RU" />
<fmt:setBundle basename="localization/locale" />

<fmt:message key="header.registration" var="registration"/>
<fmt:message key="header.sign_in" var="sign_in"/>
<fmt:message key="header.logout" var="logout"/>

<fmt:message key="main.menu" var="menu"/>
<fmt:message key="main.order" var="orders"/>
<fmt:message key="main.settings" var="settings"/>
<fmt:message key="main.reviews" var="reviews"/>
<fmt:message key="main.cart" var="cart"/>
<fmt:message key="main.booking" var="booking"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Header</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${abs}/css/bootstrap.css" >
    <script defer src="${abs}/js/bootstrap.bundle.js"></script>

</head>
<body>
    <%@include file="sign_in.jsp" %><br/>
    <div class="container-fluid">
        <h1></h1>
        <div class="row">
            <div class="col-md-2" style="background-color: #5c636a">
                <a href="${abs}/controller?command=go_to_menu_command">
                    <button type="button">${menu}</button>
                </a>
            </div>
            <div class="col-md-2" style="background-color: #5c636a">
                <a href="${abs}/controller?command=go_to_orders_command">
                    <button type="button">${orders}</button>
                </a>
            </div>
            <div class="col-md-2" style="background-color: #5c636a">
                <a href="${abs}/controller?command=go_to_booking_command">
                    <button type="button">${booking}</button>
                </a>
            </div>
            <div class="col-md-2" style="background-color: #5c636a">
                <a href="${abs}/controller?command=go_to_settings_command">
                    <button type="button">${settings}</button>
                </a>
            </div>
            <div class="col-md-2" style="background-color: #5c636a">
                <a href="${abs}/controller?command=go_to_reviews_command">
                    <button type="button">${reviews}</button>
                </a>
            </div>
            <div class="col-md-2" style="background-color: #5c636a">
                <a href="${abs}/controller?command=go_to_cart_command">
                    <button type="button">${cart}</button>
                </a>
            </div>
        </div>
    </div>
</body>
</html>