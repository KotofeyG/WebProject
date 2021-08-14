<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="editing_order.title" var="title"/>

<fmt:message key="menu_update.meal_title" var="meal_title"/>
<fmt:message key="menu_update.meal_image" var="meal_image"/>
<fmt:message key="menu_update.meal_price" var="meal_price"/>
<fmt:message key="cart.amount" var="amount"/>
<fmt:message key="cart.deleting_position" var="deleting_position"/>
<fmt:message key="meal_management.empty_picture" var="empty_picture"/>

<jsp:useBean id="cart" scope="request" type="java.util.HashMap"/>

<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="${abs}/js/plus_minus_input_counter.js"></script>
    <link rel="stylesheet" href="${abs}/css/cart.css">
    <title>${title}</title>
</head>
<body>
<%@include file="../../header/header.jsp" %>
<div class="container-fluid">
    <div class="scroll-table">
        <table class="table-condensed table-bordered mealTable">
            <caption><h3 class="text-center">${title}</h3></caption>
            <thead>
            <tr>
                <th>${meal_image}</th>
                <th>${meal_title}</th>
                <th>${amount}</th>
                <th>${meal_price}</th>
                <th>${deleting_position}</th>
            </tr>
            </thead>
        </table>
        <div class="scroll-table-body">
            <table class="table-condensed table-bordered mealTable">
                <tbody>
                <c:forEach items="${cart}" var="entry">
                    <input type="hidden" name="meal_id" value="${entry.key.id}" >
                    <tr class="">
                        <td><img src="${entry.key.image}" class="img-thumbnail image-table" alt="${empty_picture}"/>
                        </td>
                        <td>${entry.key.title}</td>
                        <td class="plus-minus-btn">
                            <div class="counter">
                                <div class="counter__btn counter__btn_minus">-</div>
                                <input type="text" class="counter__number" name="meal_number" value="${entry.value}"
                                       >
                                <div class="counter__btn counter__btn_plus">+</div>
                            </div>
                        </td>
                        <td>${entry.key.price}</td>
                        <td>
                            <form action="${abs}/controller" method="post">
                                <input type="hidden" name="command" value="delete_from_cart_command">
                                <input type="hidden" name="meal_id" value="${entry.key.id}">
                                <input type="submit" value="✕">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>