<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../../imports.jspf" %>
<%@taglib prefix="ctg" uri="custom_tags" %>

<fmt:message key="main.rub" var="rub"/>
<fmt:message key="order_check.paid" var="paid"/>
<fmt:message key="order_check.approved" var="approved"/>
<fmt:message key="order_check.rejected" var="rejected"/>
<fmt:message key="order_check.in_process" var="in_process"/>
<fmt:message key="order_confirmation.title" var="order_confirmation_title"/>
<fmt:message key="order_confirmation.approval" var="approve"/>
<fmt:message key="order_confirmation.rejection" var="reject"/>
<fmt:message key="order_confirmation.payment" var="pay"/>
<fmt:message key="order_confirmation.choice" var="choice"/>
<fmt:message key="order_confirmation.order_id" var="order_id"/>
<fmt:message key="order_confirmation.username" var="username"/>
<fmt:message key="order_confirmation.product" var="product"/>
<fmt:message key="order_confirmation.ps" var="ps"/>
<fmt:message key="order_confirmation.total_price" var="total_price_column"/>
<fmt:message key="order_confirmation.meals" var="meals"/>
<fmt:message key="order_confirmation.address" var="address"/>
<fmt:message key="order_confirmation.mobile_number" var="mobile_number"/>
<fmt:message key="order_confirmation.delivery_time" var="delivery_time"/>
<fmt:message key="order_confirmation.payment_type" var="payment_type"/>
<fmt:message key="order_confirmation.cash_payment" var="cash_payment"/>
<fmt:message key="order_confirmation.cashless_payment" var="cashless_payment"/>
<fmt:message key="order_confirmation.order_time" var="order_time"/>
<fmt:message key="order_confirmation.order_editing" var="edit_order"/>
<fmt:message key="order_confirmation.order_status" var="order_status"/>
<fmt:message key="order_confirmation.login" var="login"/>
<fmt:message key="order_confirmation.first_name" var="first_name"/>
<fmt:message key="order_confirmation.patronymic" var="patronymic"/>
<fmt:message key="order_confirmation.last_name" var="last_name"/>
<fmt:message key="order_confirmation.edit" var="edit"/>
<fmt:message key="order_confirmation.action_positive_result" var="positive_result"/>
<fmt:message key="order_confirmation.action_negative_result" var="negative_result"/>
<fmt:message key="order_confirmation.zero_number_of_orders" var="zero_orders_message"/>

<jsp:useBean id="orders" scope="session" type="java.util.Map"/>

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
<c:choose>
    <c:when test="${orders.isEmpty() ne 'true'}">
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-1"></div>
                <form action="${abs}/controller" method="post" id="order_status_changing">
                    <input type="hidden" name="command" value="change_order_status_command">
                    <div class="col-sm-1">
                        <button type="submit" class="btn btn-check" name="action" value="approve">
                            <span class="glyphicon glyphicon-ok"></span> ${approve}
                        </button>
                    </div>
                    <div class="col-sm-1">
                        <button type="submit" class="btn btn-warning" name="action" value="reject">
                            <span class="glyphicon glyphicon-remove"></span> ${reject}
                        </button>
                    </div>
                    <div class="col-sm-1">
                        <button type="submit" class="btn btn-success" name="action" value="pay">
                            <span class="glyphicon glyphicon-arrow-down"></span> ${pay}
                        </button>
                    </div>
                </form>
            </div>
            <div class="scroll-table">
                <c:choose>
                    <c:when test="${param.action_result eq 'true'}">
                        <div class="alert alert-success confirmation-message" id="message"><b class="valid_message">${positive_result}</b></div>
                    </c:when>
                    <c:when test="${param.action_result eq 'false'}">
                        <div class="alert alert-warning confirmation-message" id="message"><b class="invalid_message">${negative_result}</b></div>
                    </c:when>
                </c:choose>
                <table class="table-condensed table-bordered meal-table">
                    <caption><h3 class="text-center">${order_confirmation_title}</h3></caption>
                    <thead>
                    <tr>
                        <th>${choice}</th>
                        <th id="small-font-size">${order_id}</th>
                        <th>${product}</th>
                        <th>${username}</th>
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
                                        <input type="radio" class="form-check-input" name="selected" value="${order.key.id}" form="order_status_changing">
                                    </label>
                                </td>
                                <td>${order.key.id}</td>
                                <td id="meal-info">
                                    <c:forEach items="${order.key.meals}" var="meal">
                                        ${meal.key.title} - ${meal.value} ${ps}<br/>
                                    </c:forEach>
                                </td>
                                <td id="user-info">
                                    ${login} ${order.value.login}
                                    <c:if test="${not empty order.value.firstName}"><br/>${first_name} ${order.value.firstName}</c:if>
                                    <c:if test="${not empty order.value.patronymic}"><br/>${patronymic} ${order.value.patronymic}</c:if>
                                    <c:if test="${not empty order.value.lastName}"><br/>${last_name} ${order.value.lastName}</c:if>
                                </td>
                                <td><ctg:totalCost mealList="${order.key.meals}"/> ${rub}</td>
                                <td id="address-info"><ctg:AddressInfo address="${order.key.address}"/><br/>${mobile_number}${order.value.mobileNumber}</td>
                                <td>${order.key.deliveryTime}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.key.cash eq 'true'}">${cash_payment}</c:when>
                                        <c:otherwise>${cashless_payment}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td id="order-time">${order.key.created}</td>
                                <td>
                                    <c:choose>
                                       <c:when test="${order.key.status eq 'IN_PROCESS'}"><b class="in_process">${in_process}</b></c:when>
                                       <c:when test="${order.key.status eq 'APPROVED'}"><b class="approved">${approved}</b></c:when>
                                       <c:when test="${order.key.status eq 'REJECTED'}"><b class="rejected">${rejected}</b></c:when>
                                       <c:when test="${order.key.status eq 'PAID'}"><b class="paid">${paid}</b></c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise><h1>${zero_orders_message}</h1></c:otherwise>
</c:choose>
</body>
</html>