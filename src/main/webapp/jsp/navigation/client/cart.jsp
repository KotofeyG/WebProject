<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="cart.title" var="title"/>
<fmt:message key="menu_update.meal_title" var="meal_title"/>
<fmt:message key="menu_update.meal_image" var="meal_image"/>
<fmt:message key="menu_update.meal_price" var="meal_price"/>
<fmt:message key="meal_management.empty_picture" var="empty_picture"/>

<jsp:useBean id="cart" scope="request" type="com.kotov.restaurant.model.entity.Cart"/>

<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="${abs}/js/plus_minus_input_counter.js"></script>
    <link rel="stylesheet" href="${abs}/css/cart.css">
    <title>${title}</title>
</head>
<body class="cart">
<%@include file="../../header/header.jsp" %>
<div class="scroll-table" id="scroll-table">
    <table class="table-condensed table-bordered mealTable">
        <caption><h3 class="text-center">Корзина</h3></caption>
        <thead>
        <tr>
            <th>${meal_image}</th>
            <th>${meal_title}</th>
            <th>Кол-во</th>
            <th>${meal_price}</th>
        </tr>
        </thead>
    </table>
    <div class="scroll-table-body">
        <table class="table-condensed table-bordered mealTable">
            <tbody>

            <c:forEach items="${cart.meals}" var="entry">
                <tr>
                    <td> <img src="${entry.key.image}" class="img-thumbnail image-table" alt="${empty_picture}"/></td>
                    <td>${entry.key.title}</td>
                    <td>
                        <div class="counter">
                            <div class="counter__btn counter__btn_minus">-</div>
                            <input type="text" class="counter__number" name="meal_number" value="${entry.value}">
                            <div class="counter__btn counter__btn_plus">+</div>
                        </div>
                    </td>
                    <td>${entry.key.price}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>