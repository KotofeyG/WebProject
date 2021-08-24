<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="main.rub" var="rub"/>
<fmt:message key="menu_creation.title" var="title"/>
<fmt:message key="menu_creation.cancel" var="cancel"/>
<fmt:message key="menu_creation.menu_title" var="menu_title"/>
<fmt:message key="menu_creation.menu_type" var="menu_type"/>
<fmt:message key="menu_creation.menu_creation" var="menu_creation"/>
<fmt:message key="menu_creation.valid_data_check" var="valid_data_check"/>
<fmt:message key="menu_creation.invalid_data_check" var="invalid_data_check"/>
<fmt:message key="meal_management.negative_meal_search_result_message" var="negative_meal_search_message"/>
<fmt:message key="meal_management.dishes" var="dishes_list"/>
<fmt:message key="meal_management.meal_id" var="meal_id"/>
<fmt:message key="meal_management.choice" var="choice"/>
<fmt:message key="meal_management.meal_title" var="meal_title"/>
<fmt:message key="meal_management.meal_image" var="meal_image"/>
<fmt:message key="meal_management.meal_type" var="meal_type"/>
<fmt:message key="meal_management.meal_price" var="meal_price"/>
<fmt:message key="meal_management.meal_ingredients" var="meal_ingredients"/>
<fmt:message key="meal_management.meal_status" var="meal_status"/>
<fmt:message key="meal_management.active" var="active"/>
<fmt:message key="meal_management.archive" var="archive"/>
<fmt:message key="meal_management.empty_picture" var="empty_picture"/>

<jsp:useBean id="meals" scope="session" type="java.util.List"/>

<%--@elvariable id="menu_creation_result" type="java.lang.Boolean"--%>
<%--@elvariable id="current_product_type" type="java.lang.String"--%>

<!DOCTYPE html>
<html>
<%@include file="../../header/header.jsp" %>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="${abs}/js/message.js"></script>
    <link rel="stylesheet" href="${abs}/css/meal_management.css">
    <title>${title}</title>
</head>
<body>
<div class="container-fluid">
    <c:choose>
        <c:when test="${menu_creation_result eq 'true'}">
            <div class="alert alert-success" id="message"><b class="valid_message">${valid_data_check}</b></div>
        </c:when>
        <c:when test="${menu_creation_result eq 'false'}">
            <div class="alert alert-warning long-message" id="message"><b class="invalid_message">${invalid_data_check}</b></div>
        </c:when>
    </c:choose>
    <div class="scroll-table">
        <form id="menu-form" method="post" action="${abs}/controller">
            <input type="hidden" name="command" value="menu_creation_command">
            <input type="hidden" name="type" value="${current_product_type}">
            <table class="table-bordered meal-table">
                <caption><h3 class="text-center">${dishes_list}</h3></caption>
                <thead>
                <tr>
                    <td colspan="2">
                        <a href="${abs}/controller?command=menu_management_command">
                            <button type="button" class="btn btn-warning form-group" data-bs-dismiss="modal">${cancel}</button>
                        </a>
                    </td>
                    <td colspan="2"><input type="text" id="menu-title" class="form-control" name="title" placeholder="${menu_title}"></td>
                    <td colspan="2">
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle" id="products" type="button" data-toggle="dropdown">
                                <c:choose>
                                    <c:when test="${current_product_type eq 'roll'}">${rolls_option}</c:when>
                                    <c:when test="${current_product_type eq 'nigiri'}">${nigiri_option}</c:when>
                                    <c:when test="${current_product_type eq 'sashimi'}">${sashimi_option}</c:when>
                                    <c:when test="${current_product_type eq 'soup'}">${soups_option}</c:when>
                                    <c:when test="${current_product_type eq 'main_dish'}">${main_dishes_option}</c:when>
                                    <c:when test="${current_product_type eq 'salad'}">${salads_option}</c:when>
                                    <c:when test="${current_product_type eq 'appetizer'}">${appetizers_option}</c:when>
                                    <c:otherwise>${rolls_option}</c:otherwise>
                                </c:choose>
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu">
                                <li><a href="${abs}/controller?command=show_menu_creation_info_command&product=roll">${rolls_option}</a></li>
                                <li><a href="${abs}/controller?command=show_menu_creation_info_command&product=nigiri">${nigiri_option}</a></li>
                                <li><a href="${abs}/controller?command=show_menu_creation_info_command&product=sashimi">${sashimi_option}</a></li>
                                <li><a href="${abs}/controller?command=show_menu_creation_info_command&product=soup">${soups_option}</a></li>
                                <li><a href="${abs}/controller?command=show_menu_creation_info_command&product=main_dish">${main_dishes_option}</a></li>
                                <li><a href="${abs}/controller?command=show_menu_creation_info_command&product=salad">${salads_option}</a></li>
                                <li><a href="${abs}/controller?command=show_menu_creation_info_command&product=appetizer">${appetizers_option}</a></li>
                            </ul>
                        </div>
                    </td>
                    <td colspan="2"><button type="submit" form="menu-form" class="btn btn-success form-group">${menu_creation}</button></td>
                </tr>
                <tr>
                    <th>${choice}</th>
                    <th>${meal_id}</th>
                    <th>${meal_title}</th>
                    <th>${meal_image}</th>
                    <th>${meal_type}</th>
                    <th>${meal_price}</th>
                    <th>${meal_ingredients}</th>
                    <th>${meal_status}</th>
                </tr>
                </thead>
            </table>
        </form>
        <c:choose>
        <c:when test="${meals.isEmpty() ne 'true'}">
        <div class="scroll-table-body">
            <table class="table-bordered meal-table">
                <tbody>
                <c:forEach items="${meals}" var="order">
                    <tr>
                        <td class="meal-checkbox">
                            <label class="form-check-label">
                                <input type="checkbox" class="form-check-input" name="selected" value="${order.id}" form="menu-form">
                            </label>
                        </td>
                        <td>${order.id}</td>
                        <td>${order.title}</td>
                        <td><img src="${order.image}" class="img-thumbnail" alt="${empty_picture}" width="153px"/></td>
                        <td>${order.type.value}</td>
                        <td>${order.price} ${rub}</td>
                        <td>${order.recipe}</td>
                        <td>
                            <c:choose>
                                <c:when test="${order.active eq 'true'}">${active}</c:when>
                                <c:otherwise>${archive}</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    </c:when>
    <c:otherwise><h1>${negative_meal_search_message}</h1></c:otherwise>
    </c:choose>
</div>
</body>
</html>