<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>
<%@taglib prefix="ctg" uri="custom_tags" %>

<fmt:message key="order_confirmation.title" var="title"/>
<fmt:message key="order_confirmation.approval" var="approve"/>
<fmt:message key="order_confirmation.rejection" var="reject"/>
<fmt:message key="order_confirmation.order_id" var="order_id"/>
<fmt:message key="order_confirmation.username" var="username"/>
<fmt:message key="order_confirmation.total_price" var="total_price_column"/>
<fmt:message key="order_confirmation.meals" var="meals"/>
<fmt:message key="order_confirmation.address" var="address"/>
<fmt:message key="order_confirmation.delivery_time" var="delivery_time"/>
<fmt:message key="order_confirmation.payment_type" var="payment_type"/>
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


<jsp:useBean id="orders" scope="request" type="java.util.Map"/>

<%--@elvariable id="action_result" type="java.lang.Boolean"--%>
<%--@elvariable id="zero_number_of_orders" type="java.lang.Boolean"--%>

<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>
<%@include file="../../header/header.jsp" %>
<div class="container-fluid">
    <div class="scroll-table">
        <div class="row">
            <div class="col-sm-1"></div>
            <form action="${abs}/controller" method="post" id="order_status_changing">
                <input type="hidden" name="command" value="change_order_status_command">
                <div class="col-sm-1">
                    <button type="submit" class="btn btn-success" name="action" value="approve">
                        <span class="glyphicon glyphicon-ok"></span> ${approve}
                    </button>
                </div>
                <div class="col-sm-1">
                    <button type="submit" class="btn btn-warning" name="action" value="reject">
                        <span class="glyphicon glyphicon-remove"></span> ${reject}
                    </button>
                </div>
            </form>
        </div>
        <table class="table-condensed table-bordered mealTable">
            <caption><h3 class="text-center">${title}</h3></caption>
            <thead>
            <tr>
                <th></th>
                <th>${order_id}</th>
                <th>${username}</th>
                <th>${total_price_column}</th>
                <th>${address}</th>
                <th>${delivery_time}</th>
                <th>${payment_type}</th>
                <th>${order_time}</th>
                <th>${order_status}</th>
                <th>${edit_order}</th>
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
                                <input type="radio" class="form-check-input" name="selected" value="${meal.key.id}" form="order_status_changing">
                            </label>
                        </td>
                        <td>${meal.key.id}</td>
                        <td>
                                ${login} ${meal.value.login}
                            <c:if test="${not empty meal.value.firstName}"><br/>${first_name} ${meal.value.firstName}</c:if>
                            <c:if test="${not empty meal.value.patronymic}"><br/>${patronymic} ${meal.value.patronymic}</c:if>
                            <c:if test="${not empty meal.value.lastName}"><br/>${last_name} ${meal.value.lastName}</c:if>
                        </td>
                        <td><ctg:totalCost mealList="${meal.key.meals}"/></td>
                        <td><ctg:AddressInfo address="${meal.key.address}"/></td>
                        <td>${meal.key.deliveryTime}</td>
                        <td>${meal.key.cash}</td>
                        <td>${meal.key.created}</td>
                        <td>${meal.key.status.value}</td>
                        <td><a href="${abs}/controller?command=edit_order_command&order_id=${meal.key.id}">${edit}</a></td>
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
            <c:choose>
                <c:when test="${zero_number_of_orders eq 'true'}"><br/>${zero_orders_message}</c:when>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>