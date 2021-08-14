<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>
<%@taglib prefix="ctg" uri="custom_tags" %>

<fmt:message key="order_check.title" var="title"/>
<fmt:message key="order_check.extra_info" var="extra_info"/>

<fmt:message key="order_check.action" var="action"/>
<fmt:message key="order_check.payment" var="payment"/>
<fmt:message key="order_check.paid" var="paid"/>
<fmt:message key="order_check.rejection" var="rejection"/>
<fmt:message key="order_check.rejected" var="rejected"/>

<fmt:message key="order_confirmation.order_id" var="order_id"/>
<fmt:message key="order_confirmation.username" var="username"/>
<fmt:message key="order_confirmation.total_price" var="total_price_column"/>
<fmt:message key="order_confirmation.address" var="address"/>
<fmt:message key="order_confirmation.delivery_time" var="delivery_time"/>
<fmt:message key="order_confirmation.payment_type" var="payment_type"/>
<fmt:message key="order_confirmation.order_time" var="order_time"/>
<fmt:message key="order_confirmation.order_editing" var="edit_order"/>
<fmt:message key="order_confirmation.order_status" var="order_status"/>

<jsp:useBean id="orders" scope="request" type="java.util.List"/>

<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<%@include file="../../header/header.jsp" %>
<body>
<div class="container-fluid">
    <div class="scroll-table">
        <table class="table-condensed table-bordered mealTable">
            <caption><h3 class="text-center">${title}</h3></caption>
            <thead>
            <tr>
                <th></th>
                <th>${order_id}</th>
                <th>${total_price_column}</th>
                <th>${address}</th>
                <th>${delivery_time}</th>
                <th>${payment_type}</th>
                <th>${order_time}</th>
                <th>${order_status}</th>
                <th>${extra_info}</th>
                <th>${action}</th>
            </tr>
            </thead>
        </table>
        <div class="scroll-table-body">
            <table class="table-condensed table-bordered mealTable">
                <tbody>
                <c:forEach items="${orders}" var="meal">
                    <tr>
                        <td>
                            <label class="form-check-label">
                                <input type="radio" class="form-check-input" name="selected" value="${meal.id}">
                            </label>
                        </td>
                        <td>${meal.id}</td>
                        <td><ctg:totalCost mealList="${meal.meals}"/></td>
                        <td><ctg:AddressInfo address="${meal.address}"/></td>
                        <td>${meal.deliveryTime}</td>
                        <td>${meal.cash}</td>
                        <td>${meal.created}</td>
                        <td>${meal.status.value}</td>
                        <td><a href="${abs}/controller?command=editing_order_command&order_id=${meal.id}">${edit}</a>
                        <td>
                            <form action="controller" method="post">
                                <input type="hidden" name="command" value="order_action_command">
                                <input type="hidden" name="selected" value="${meal.id}"/>
                                <c:choose>
                                    <c:when test="${meal.status eq 'IN_PROCESS'}"><button type="submit" class="btn-danger" name="action" value="reject">${rejection}</c:when>
                                    <c:when test="${meal.status eq 'APPROVED'}"><button type="submit" class="btn-success" name="action" value="pay">${payment}</c:when>
                                    <c:when test="${meal.status eq 'REJECTED'}"><button class="btn-default" disabled>${rejected}</c:when>
                                    <c:when test="${meal.status eq 'PAID'}"><button class="btn-default" disabled>${paid}</c:when>
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
                <c:when test="${action_result eq 'true'}">${positive_result}</c:when>
                <c:when test="${action_result eq 'false'}">${negative_result}</c:when>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>