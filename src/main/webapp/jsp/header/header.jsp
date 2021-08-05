<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../imports.jspf" %>

<fmt:message key="header.title" var="title"/>
<fmt:message key="header.brand" var="brand"/>
<fmt:message key="header.language_code" var="language"/>
<fmt:message key="header.dropdown_menu_title" var="menu"/>
<fmt:message key="header.rolls" var="rolls_menu"/>
<fmt:message key="header.nigiri" var="nigiri_menu"/>
<fmt:message key="header.sashimi" var="sashimi_menu"/>
<fmt:message key="header.soups" var="soups_menu"/>
<fmt:message key="header.main_dishes" var="main_dishes_menu"/>
<fmt:message key="header.salads" var="salads_menu"/>
<fmt:message key="header.appetizers" var="appetizers_menu"/>
<fmt:message key="header.registration" var="registration"/>
<fmt:message key="header.sign_in" var="sign_in"/>
<fmt:message key="header.logout" var="logout"/>
<fmt:message key="header.authorization" var="authorization"/>
<fmt:message key="header.name" var="name"/>
<fmt:message key="header.password" var="password"/>
<fmt:message key="header.invalid_login_psw" var="invalid_message"/>


<%--Temporary messages--%>
<fmt:message key="sign_up.registration" var="registration"/>
<fmt:message key="main.order" var="orders"/>
<fmt:message key="main.settings" var="settings"/>
<fmt:message key="main.reviews" var="reviews"/>
<fmt:message key="main.cart" var="cart_icon"/>
<fmt:message key="main.booking" var="booking"/>
<%--Temporary messages--%>

<jsp:useBean id="user" scope="session" type="com.kotov.restaurant.model.entity.User"/>

<%--@elvariable id="authorization_data" type="java.lang.Boolean"--%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${abs}/css/main.css">
    <title>${title}</title>
</head>
<body>
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${abs}/controller?command=go_to_main_command">${brand}</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span> ${menu}</a>
                <ul class="dropdown-menu">
                    <li><a href="${abs}/controller?command=product_command&product=roll">${rolls_menu}</a></li>
                    <li><a href="${abs}/controller?command=product_command&product=nigiri">${nigiri_menu}</a></li>
                    <li><a href="${abs}/controller?command=product_command&product=sashimi">${sashimi_menu}</a></li>
                    <li><a href="${abs}/controller?command=product_command&product=soup">${soups_menu}</a></li>
                    <li><a href="${abs}/controller?command=product_command&product=main_dish">${main_dishes_menu}</a></li>
                    <li><a href="${abs}/controller?command=product_command&product=salad">${salads_menu}</a></li>
                    <li><a href="${abs}/controller?command=product_command&product=appetizer">${appetizers_menu}</a></li>
                </ul>
            </li>
            <c:choose>
                <c:when test="${user.role eq 'ADMIN'}"><%@include file="fragment/admin_header.jspf" %></c:when>
                <c:when test="${user.role eq 'MANAGER'}"><%@include file="fragment/manager_header.jspf" %></c:when>
                <c:when test="${user.role eq 'CLIENT'}"><%@include file="fragment/client_header.jspf" %></c:when>
                <c:otherwise><%@include file="fragment/guest_header.jspf" %></c:otherwise>
            </c:choose>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span> ${language}</a>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="${abs}/controller?command=change_locale_command&locale=en_US">English</a></li>
                    <li><a class="dropdown-item" href="${abs}/controller?command=change_locale_command&locale=ru_RU">Русский</a></li>
                </ul>
            </li>
            <c:choose>
                <c:when test="${user.role eq 'ADMIN' or user.role eq 'MANAGER' or user.role eq 'CLIENT'}">
                    <li><a href="${abs}/controller?command=logout_command"><span class="glyphicon glyphicon-log-out"></span> ${logout}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${abs}/controller?command=go_to_registration_command"><span class="glyphicon glyphicon-user"></span> ${registration}</a></li>
                    <li><a href="#" data-toggle="modal" data-target="#login-modal"><span class="glyphicon glyphicon-log-in"></span> ${sign_in}</a></li>
                    <div id="login-modal" class="modal fade">
                        <div class="modal-dialog modal-sm">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">${authorization}</h4>
                                </div>
                                <div class="modal-body">
                                    <form action="${abs}/controller" method="post" id="log-form" class="form-group">
                                        <input type="hidden" name="command" value="authentication_command"/>
                                        <label for="user_name"><span class="glyphicon glyphicon-user"></span> ${name}</label>
                                        <input type="text" id="user_name" class="form-control" name="login" placeholder=${name}><br/>
                                        <label for="user-psw"><span class="glyphicon glyphicon-eye-open"></span> ${password}</label>
                                        <input type="password" id="user-psw" class="form-control" name="password" placeholder="${password}">
                                    </form>
                                    <c:if test="${authorization_data eq 'false'}"><b>${invalid_message}</b></c:if>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" form="log-form" class="btn btn-secondary" data-bs-dismiss="modal">${sign_in}</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>
</body>
</html>