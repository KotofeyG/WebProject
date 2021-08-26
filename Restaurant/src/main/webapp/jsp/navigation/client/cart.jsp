<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="cart.title" var="cart_title"/>
<fmt:message key="cart.empty" var="empty_cart"/>
<fmt:message key="cart.amount" var="amount"/>
<fmt:message key="cart.deleting_position" var="deleting_position"/>
<fmt:message key="cart.clearing_cart" var="clearing_cart"/>
<fmt:message key="cart.ordering" var="ordering"/>
<fmt:message key="cart.extra_data" var="extra_data"/>
<fmt:message key="cart.delivery_time" var="delivery_time"/>
<fmt:message key="cart.payment_type" var="payment_type"/>
<fmt:message key="cart.cash_payment" var="cash_payment"/>
<fmt:message key="cart.cashless_payments" var="cashless_payment"/>
<fmt:message key="cart.address" var="address"/>
<fmt:message key="cart.action_message" var="order_adding_message"/>
<fmt:message key="menu_update.meal_title" var="meal_title"/>
<fmt:message key="menu_update.meal_image" var="meal_image"/>
<fmt:message key="menu_update.meal_price" var="meal_price"/>
<fmt:message key="meal_management.empty_picture" var="empty_picture"/>
<fmt:message key="settings.city" var="city"/>
<fmt:message key="settings.street" var="street"/>
<fmt:message key="settings.building" var="building"/>
<fmt:message key="settings.block" var="block"/>
<fmt:message key="settings.flat" var="flat"/>
<fmt:message key="settings.entrance" var="entrance"/>
<fmt:message key="settings.floor" var="floor"/>
<fmt:message key="settings.intercom_code" var="intercom_code"/>
<fmt:message key="settings.invalid_city_message" var="invalid_city_message"/>
<fmt:message key="settings.invalid_street_message" var="invalid_street_message"/>
<fmt:message key="settings.invalid_building_message" var="invalid_building_message"/>
<fmt:message key="settings.invalid_block_message" var="invalid_block_message"/>
<fmt:message key="settings.invalid_flat_message" var="invalid_flat_message"/>
<fmt:message key="settings.invalid_entrance_message" var="invalid_entrance_message"/>
<fmt:message key="settings.invalid_floor_message" var="invalid_floor_message"/>
<fmt:message key="settings.invalid_intercom_code_message" var="invalid_intercom_code_message"/>
<fmt:message key="order_confirmation.action_positive_result" var="positive_result"/>
<fmt:message key="order_confirmation.action_negative_result" var="negative_result"/>

<jsp:useBean id="cart" scope="session" type="java.util.HashMap"/>
<jsp:useBean id="addresses" scope="session" type="java.util.List"/>

<%--@elvariable id="invalid_city" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_street" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_building" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_block" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_flat" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_entrance" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_floor" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_intercom_code" type="java.lang.Boolean"--%>
<%--@elvariable id="address_adding_result" type="java.lang.Boolean"--%>
<%--@elvariable id="action_result" type="java.lang.Boolean"--%>

<!DOCTYPE html>
<html>
<%@include file="../../header/header.jsp" %>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="${abs}/js/message.js"></script>
    <link rel="stylesheet" href="${abs}/css/meal_management.css">
    <link rel="stylesheet" href="${abs}/css/cart.css">
    <title>${title}</title>
</head>
<body>
<c:choose>
    <c:when test="${param.action_result eq 'true'}">
        <div class="alert alert-success confirmation-message" id="message"><b class="valid_message">${positive_result}</b></div>
    </c:when>
    <c:when test="${param.action_result eq 'false'}">
        <div class="alert alert-warning confirmation-message" id="message"><b class="invalid_message">${negative_result}</b></div>
    </c:when>
</c:choose>
<c:choose>
<c:when test="${cart.isEmpty() eq 'false'}">
<div class="absolute-cart">
    <div class="scroll-table">
        <table class="table-condensed table-bordered meal-table">
            <caption><h3 class="text-center">${cart_title}</h3></caption>
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
            <table class="table-condensed table-bordered meal-table">
                <tbody>
                <c:forEach items="${cart}" var="entry">
                    <input type="hidden" name="meal_id" value="${entry.key.id}" form="order">
                    <tr class="">
                        <td><img src="${entry.key.image}" class="img-thumbnail image-table" alt="${empty_picture}"/></td>
                        <td>${entry.key.title}</td>
                        <td><input type="hidden" name="meal_number" value="${entry.value}" form="order">${entry.value}</td>
                        <td>${entry.key.price}</td>
                        <td>
                            <form action="${abs}/controller" method="post">
                                <input type="hidden" name="command" value="delete_meals_from_cart_command">
                                <input type="hidden" name="meal_id" value="${entry.key.id}">
                                <button type="submit" class="del" value="×"><span>×</span></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="order_input">
    <h3>${extra_data}</h3>
    <button type="submit" class="btn btn-danger input-button" form="deletion"><span class="glyphicon glyphicon-remove-sign"></span> ${clearing_cart}</button>
    <button type="submit" class="btn btn-success input-button" form="order"><span class="glyphicon glyphicon-ok-sign"></span> ${ordering}</button>
    <br/><br/>
    <form action="${abs}/controller" method="post" id="deletion">
        <input type="hidden" name="command" value="delete_meals_from_cart_command">
        <c:forEach items="${cart.keySet()}" var="order">
            <input type="hidden" name="meal_id" value="${order.id}">
        </c:forEach>
    </form>
    <form action="${abs}/controller" method="post" id="order">
        <input type="hidden" name="command" value="order_meals_command">
        <label for="delivery_time">${delivery_time}</label>
        <input type="time" name="delivery_time" form="order" id="delivery_time">
        <label for="payment_type">${payment_type}</label>
        <select name="payment_type" id="payment_type" size="1" form="order">
            <option value="true">${cash_payment}</option>
            <option value="false">${cashless_payment}</option>
        </select>
        <br/><br/>
        <c:choose>
            <c:when test="${addresses.isEmpty() ne 'true'}">
                <label for="address">${address}</label>
                <select name="address" id="address" size="1" form="order">
                    <c:forEach items="${addresses}" var="address">
                        <option value="${address.id}">${address.city.value} ${address.street} ${address.building}</option>
                    </c:forEach>
                </select>
            </c:when>
            <c:otherwise>
                <div class="row">
                    <div class="form-group col-sm-4 indent">
                        <label for="city">${city}</label>
                        <input type="text" class="form-control" id="city" name="city" value="${valid_city}" list="cities">
                        <datalist id="cities">
                            <option value="Minsk">Минск</option>
                            <option value="Brest">Брест</option>
                            <option value="Vitebsk">Витебск</option>
                            <option value="Gomel">Гомель</option>
                            <option value="Grodno">Гродно</option>
                            <option value="Mogilev">Могилёв</option>
                        </datalist>
                    </div>
                    <br/>
                    <c:if test="${invalid_city eq 'true'}"><div id="message1"><b class="invalid_message">${invalid_city_message}</b></div></c:if>
                </div>
                <div class="row">
                    <div class="form-group col-sm-4 indent">
                        <label for="street">${street}</label>
                        <input type="text" class="form-control" id="street" name="street" value="${valid_street}">
                    </div><br/>
                    <c:if test="${invalid_street eq 'true'}"><div id="message2"><b class="invalid_message">${invalid_street_message}</b></div></c:if>
                </div>
                <div class="row">
                    <div class="form-group col-sm-4 indent">
                        <label for="building">${building}</label>
                        <input type="number" class="form-control" id="building" name="building" value="${valid_building}">
                    </div><br/>
                    <c:if test="${invalid_building eq 'true'}"><div id="message3"><b class="invalid_message">${invalid_building_message}</b></div></c:if>
                </div>
                <div class="row">
                    <div class="form-group col-sm-4 indent">
                        <label for="block">${block}</label>
                        <input type="text" class="form-control" id="block" name="block" value="${valid_block}">
                    </div><br/>
                    <c:if test="${invalid_block eq 'true'}"><div id="message4"><b class="invalid_message">${invalid_block_message}</b></div></c:if>
                </div>
                <div class="row">
                    <div class="form-group col-sm-4 indent">
                        <label for="flat">${flat}</label>
                        <input type="number" class="form-control" id="flat" name="flat" value="${valid_flat}">
                    </div>
                    <c:if test="${invalid_flat eq 'true'}"><div id="message5"><b class="invalid_message">${invalid_flat_message}</b></div></c:if>
                </div>
                <div class="row">
                    <div class="form-group col-sm-4 indent">
                        <label for="entrance">${entrance}</label>
                        <input type="number" class="form-control" id="entrance" name="entrance" value="${valid_entrance}">
                    </div>
                    <c:if test="${invalid_entrance eq 'true'}"><div id="message6"><b class="invalid_message">${invalid_entrance_message}</b></div></c:if>
                </div>
                <div class="row">
                    <div class="form-group col-sm-4 indent">
                        <label for="floor">${floor}</label>
                        <input type="number" class="form-control" id="floor" name="floor" value="${valid_floor}">
                    </div>
                    <c:if test="${invalid_floor eq 'true'}"><div id="message7"><b class="invalid_message">${invalid_floor_message}</b></div></c:if>
                </div>
                <div class="row">
                    <div class="form-group col-sm-4">
                        <label for="intercom_code">${intercom_code}</label>
                        <input type="number" class="form-control" id="intercom_code" name="intercom_code" value="${valid_intercom_code}">
                    </div>
                    <c:if test="${invalid_intercom_code eq 'true'}"><div id="message8"><b class="invalid_message">${invalid_intercom_code_message}</b></div></c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </form>
</div>
</c:when>
    <c:otherwise><h1>${empty_cart}</h1></c:otherwise>
</c:choose>
</body>
</html>