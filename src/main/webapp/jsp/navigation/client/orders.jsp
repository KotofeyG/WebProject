<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>
<%@taglib prefix="ctg" uri="custom_tags" %>

<fmt:message key="order_check.title" var="order_title"/>
<fmt:message key="order_check.extra_info" var="extra_info"/>

<fmt:message key="order_check.action" var="action"/>
<fmt:message key="order_check.payment" var="payment"/>
<fmt:message key="order_check.paid" var="paid"/>
<fmt:message key="order_check.rejection" var="rejection"/>
<fmt:message key="order_check.rejected" var="rejected"/>
<fmt:message key="order_confirmation.order_id" var="order_id"/>
<fmt:message key="order_confirmation.username" var="username"/>
<fmt:message key="order_confirmation.choice" var="choice"/>
<fmt:message key="order_confirmation.total_price" var="total_price_column"/>
<fmt:message key="main.rub" var="rub"/>
<fmt:message key="order_confirmation.address" var="address"/>
<fmt:message key="order_confirmation.delivery_time" var="delivery_time"/>
<fmt:message key="order_confirmation.payment_type" var="payment_type"/>
<fmt:message key="order_confirmation.cash_payment" var="cash_payment"/>
<fmt:message key="order_confirmation.cashless_payments" var="cashless_payments"/>
<fmt:message key="order_confirmation.order_time" var="order_time"/>
<fmt:message key="order_confirmation.order_editing" var="edit_order"/>
<fmt:message key="order_confirmation.order_status" var="order_status"/>

<jsp:useBean id="orders" scope="request" type="java.util.List"/>

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
    <div class="scroll-table">
        <table class="table-condensed table-bordered meal-table">
            <caption><h3 class="text-center">${order_title}</h3></caption>
            <thead>
            <tr>
<%--                <th>${choice}</th>--%>
                <th>${order_id}</th>
                <th>${total_price_column}</th>
                <th>${address}</th>
                <th>${delivery_time}</th>
                <th>${payment_type}</th>
                <th>${order_time}</th>
                <th>${order_status}</th>
                <th>${action}</th>
            </tr>
            </thead>
        </table>
        <div class="scroll-table-body">
            <table class="table-condensed table-bordered meal-table">
                <tbody>
                <c:forEach items="${orders}" var="order">
                    <tr>
<%--                        <td>--%>
<%--                            <label class="form-check-label">--%>
<%--                                <input type="radio" class="form-check-input" name="selected" value="${order.id}">--%>
<%--                            </label>--%>
<%--                        </td>--%>
                        <td>${order.id}</td>
                        <td><ctg:totalCost mealList="${order.meals}"/> ${rub}</td>
                        <td id="user-info"><ctg:AddressInfo address="${order.address}"/></td>
                        <td>${order.deliveryTime}</td>
                        <td>

                            <c:choose>
                                <c:when test="${order.cash eq 'true'}">${cash_payment}</c:when>
                                <c:otherwise>${cashless_payments}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${order.created}</td>
                        <td>${order.status.value}</td>
                        <td>
                            <form action="controller" method="post">
                                <input type="hidden" name="command" value="order_action_command">
                                <input type="hidden" name="selected" value="${order.id}"/>
                                <c:choose>
                                    <c:when test="${order.status eq 'IN_PROCESS'}"><button type="submit" class="btn-danger" name="action" value="reject">${rejection}</c:when>
                                    <c:when test="${order.status eq 'APPROVED'}"><button type="submit" class="btn-success" name="action" value="pay">${payment}</c:when>
                                    <c:when test="${order.status eq 'REJECTED'}"><button class="btn-default" disabled>${rejected}</c:when>
                                    <c:when test="${order.status eq 'PAID'}"><button class="btn-default" disabled>${paid}</c:when>
                                </c:choose>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-5"></div>
        <div class="col-sm-3">
            <c:choose>
                <%--@elvariable id="action_result" type="java.lang.Boolean"--%>
                <c:when test="${action_result eq 'true'}">${positive_result}</c:when>
                <c:when test="${action_result eq 'false'}">${negative_result}</c:when>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>