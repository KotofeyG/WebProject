<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../../imports.jspf" %>
<%@taglib prefix="ctg" uri="custom_tags" %>

<fmt:message key="main.rub" var="rub"/>
<fmt:message key="order_check.title" var="order_title"/>
<fmt:message key="order_check.extra_info" var="extra_info"/>
<fmt:message key="order_check.action" var="action"/>
<fmt:message key="order_check.payment" var="pay"/>
<fmt:message key="order_check.paid" var="paid"/>
<fmt:message key="order_check.approved" var="approved"/>
<fmt:message key="order_check.rejected" var="rejected"/>
<fmt:message key="order_check.in_process" var="in_process"/>
<fmt:message key="order_confirmation.order_id" var="order_id"/>
<fmt:message key="order_confirmation.product" var="product"/>
<fmt:message key="order_confirmation.username" var="username"/>
<fmt:message key="order_confirmation.choice" var="choice"/>
<fmt:message key="order_confirmation.total_price" var="total_price_column"/>
<fmt:message key="order_confirmation.address" var="address"/>
<fmt:message key="order_confirmation.delivery_time" var="delivery_time"/>
<fmt:message key="order_confirmation.payment_type" var="payment_type"/>
<fmt:message key="order_confirmation.cash_payment" var="cash_payment"/>
<fmt:message key="order_confirmation.cashless_payment" var="cashless_payment"/>
<fmt:message key="order_confirmation.order_time" var="order_time"/>
<fmt:message key="order_confirmation.order_editing" var="edit_order"/>
<fmt:message key="order_confirmation.order_status" var="order_status"/>
<fmt:message key="order_confirmation.action_positive_result" var="positive_result"/>
<fmt:message key="order_confirmation.action_negative_result" var="negative_result"/>
<fmt:message key="order_confirmation.rejection" var="reject"/>
<fmt:message key="order_confirmation.ps" var="ps"/>

<jsp:useBean id="orders" scope="session" type="java.util.List"/>

<%--@elvariable id="action_result" type="java.lang.Boolean"--%>

<!DOCTYPE html>
<html>
<%@include file="../../header/header.jsp" %>
<head>
    <script src="${abs}/js/message.js"></script>
    <link rel="stylesheet" href="${abs}/css/meal_management.css">
    <title>${title}</title>
</head>
<body>
<div class="container-fluid">
<div class="row">
    <div class="col-sm-1"></div>
    <form action="${abs}/controller" method="post" id="order_status_changing">
        <input type="hidden" name="command" value="order_action_command">
        <div class="col-sm-1">
            <button type="submit" class="btn btn-warning" name="action" value="reject"><span class="glyphicon glyphicon-remove"></span> ${reject}</button>
        </div>
        <div class="col-sm-1">
            <button type="submit" class="btn btn-success" name="action" value="pay"><span class="glyphicon glyphicon-arrow-down"></span> ${pay}</button>
        </div>
    </form>
</div>
<c:choose>
    <c:when test="${param.action_result eq 'true'}">
        <div class="alert alert-success confirmation-message" id="message"><b class="valid_message">${positive_result}</b></div>
    </c:when>
    <c:when test="${param.action_result eq 'false'}">
        <div class="alert alert-warning confirmation-message" id="message"><b class="invalid_message">${negative_result}</b></div>
    </c:when>
</c:choose>
    <div class="scroll-table">
        <table class="table-condensed table-bordered meal-table">
            <caption><h3 class="text-center">${order_title}</h3></caption>
            <thead>
            <tr>
                <th>${choice}</th>
                <th id="small-font-size">${order_id}</th>
                <th>${product}</th>
                <th>${total_price_column}</th>
                <th>${address}</th>
                <th>${delivery_time}</th>
                <th>${payment_type}</th>
                <th>${order_time}</th>
                <th>${order_status}</th>
            </tr>
            </thead>
        </table>
        <div class="scroll-table-body">
            <table class="table-condensed table-bordered meal-table">
                <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
                        <td>
                            <label class="form-check-label">
                                <input type="radio" class="form-check-input" name="selected" value="${order.id}" form="order_status_changing">
                            </label>
                        </td>
                        <td>${order.id}</td>
                        <td id="meal-info">
                            <c:forEach items="${order.meals}" var="meal">
                                ${meal.key.title} - ${meal.value} ${ps}<br/>
                            </c:forEach>
                        </td>
                        <td><ctg:totalCost mealList="${order.meals}"/> ${rub}</td>
                        <td id="user-info"><ctg:AddressInfo address="${order.address}"/></td>
                        <td>${order.deliveryTime}</td>
                        <td>
                            <c:choose>
                                <c:when test="${order.cash eq 'true'}">${cash_payment}</c:when>
                                <c:otherwise>${cashless_payment}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${order.created}</td>
                        <td>
                            <c:choose>
                                <c:when test="${order.status eq 'IN_PROCESS'}"><b class="in_process">${in_process}</b></c:when>
                                <c:when test="${order.status eq 'APPROVED'}"><b class="approved">${approved}</b></c:when>
                                <c:when test="${order.status eq 'REJECTED'}"><b class="rejected">${rejected}</b></c:when>
                                <c:when test="${order.status eq 'PAID'}"><b class="paid">${paid}</b></c:when>
                            </c:choose>
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