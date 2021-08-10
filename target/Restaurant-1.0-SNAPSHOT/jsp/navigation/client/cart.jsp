<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="cart.title" var="title"/>
<fmt:message key="cart.amount" var="amount"/>
<fmt:message key="cart.deleting_position" var="deleting_position"/>
<fmt:message key="cart.clearing_cart" var="clearing_cart"/>
<fmt:message key="cart.ordering" var="ordering"/>
<fmt:message key="cart.extra_data" var="extra_data"/>
<fmt:message key="cart.delivery_time" var="delivery_time"/>
<fmt:message key="cart.address" var="address"/>
<fmt:message key="menu_update.meal_title" var="meal_title"/>
<fmt:message key="menu_update.meal_image" var="meal_image"/>
<fmt:message key="menu_update.meal_price" var="meal_price"/>
<fmt:message key="meal_management.empty_picture" var="empty_picture"/>

<%--<fmt:message key="settings.city" var="city"/>--%>
<%--<fmt:message key="settings.street" var="street"/>--%>
<%--<fmt:message key="settings.building" var="building"/>--%>
<%--<fmt:message key="settings.block" var="block"/>--%>
<%--<fmt:message key="settings.flat" var="flat"/>--%>
<%--<fmt:message key="settings.entrance" var="entrance"/>--%>
<%--<fmt:message key="settings.floor" var="floor"/>--%>
<%--<fmt:message key="settings.intercom_code" var="intercom_code"/>--%>

<%--<fmt:message key="settings.invalid_city_message" var="invalid_city_message"/>--%>
<%--<fmt:message key="settings.invalid_street_message" var="invalid_street_message"/>--%>
<%--<fmt:message key="settings.invalid_building_message" var="invalid_building_message"/>--%>
<%--<fmt:message key="settings.invalid_block_message" var="invalid_block_message"/>--%>
<%--<fmt:message key="settings.invalid_flat_message" var="invalid_flat_message"/>--%>
<%--<fmt:message key="settings.invalid_entrance_message" var="invalid_entrance_message"/>--%>
<%--<fmt:message key="settings.invalid_floor_message" var="invalid_floor_message"/>--%>
<%--<fmt:message key="settings.invalid_intercom_code_message" var="invalid_intercom_code_message"/>--%>

<%--<fmt:message key="settings.address_adding_message" var="address_adding_message"/>--%>

<jsp:useBean id="cart" scope="request" type="java.util.HashMap"/>
<jsp:useBean id="addresses" scope="request" type="java.util.List"/>

<%--@elvariable id="valid_city" type="java.lang.String"--%>
<%--@elvariable id="valid_street" type="java.lang.String"--%>
<%--@elvariable id="valid_building" type="java.lang.String"--%>
<%--@elvariable id="valid_block" type="java.lang.String"--%>
<%--@elvariable id="valid_flat" type="java.lang.String"--%>
<%--@elvariable id="valid_entrance" type="java.lang.String"--%>
<%--@elvariable id="valid_floor" type="java.lang.String"--%>
<%--@elvariable id="valid_intercom_code" type="java.lang.String"--%>

<%--@elvariable id="invalid_city" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_street" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_building" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_block" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_flat" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_entrance" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_floor" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_intercom_code" type="java.lang.Boolean"--%>
<%--@elvariable id="address_adding_result" type="java.lang.Boolean"--%>
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
                    <input type="hidden" name="meal_id" value="${entry.key.id}" form="order">
                    <tr class="">
                        <td><img src="${entry.key.image}" class="img-thumbnail image-table" alt="${empty_picture}"/>
                        </td>
                        <td>${entry.key.title}</td>
                        <td class="plus-minus-btn">
                            <div class="counter">
                                <div class="counter__btn counter__btn_minus">-</div>
                                <input type="text" class="counter__number" name="meal_number" value="${entry.value}"
                                       form="order">
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
    <div class="row">
        <form action="${abs}/controller" method="post">
            <input type="hidden" name="command" value="delete_from_cart_command">
            <c:forEach items="${cart.keySet()}" var="meal">
                <input type="hidden" name="meal_id" value="${meal.id}">
            </c:forEach>
            <div class="col-sm-1"></div>
            <div class="col-sm-1">
                <button type="submit" class="btn btn-danger">
                    <span class="glyphicon glyphicon-remove-sign"></span> ${clearing_cart}
                </button>
            </div>
        </form>
        <div class="col-sm-7"></div>
        <div class="col-sm-1">
            <form action="${abs}/controller" method="post" id="order">
                <input type="hidden" name="command" value="meal_order_command">
                <button type="submit" class="btn btn-success">
                    <span class="glyphicon glyphicon-ok-sign"></span> ${ordering}
                </button>
            </form>
        </div>
    </div>
    <%--    <br/>--%>
    <%--    <div class="row">--%>
    <%--        <div class="col-sm-1"></div>--%>
    <%--        <h3 class="col-sm-3">${extra_data}</h3>--%>
    <%--    </div>--%>
    <%--    <div class="row">--%>
    <%--        <div class="col-sm-1"></div>--%>

    <%--        <div class="col-sm-3">--%>
    <%--            <label for="address">${address}</label>--%>
    <%--            <select name="address" id="address" size="1" form="order">--%>
    <%--                <c:forEach items="${addresses}" var="address">--%>
    <%--                <option value="${address.id}">${address.city.getRussianName()} ${address.street} ${address.building}</option>--%>
    <%--                </c:forEach>--%>
    <%--            </select>--%>
    <%--        </div>--%>

    <%--        <div class="address">--%>
    <%--                <div class="row">--%>
    <%--                    <div class="col-sm-1"></div>--%>
    <%--                    <div class="form-group col-sm-4">--%>
    <%--                        <label for="city">${city}</label>--%>
    <%--                        <input type="text" class="form-control" id="city" name="city" value="${valid_city}" list="cities" form="order">--%>
    <%--                        <datalist id="cities">--%>
    <%--                            <option value="Минск"></option>--%>
    <%--                            <option value="Брест"></option>--%>
    <%--                            <option value="Витебск"></option>--%>
    <%--                            <option value="Гомель"></option>--%>
    <%--                            <option value="Гродно"></option>--%>
    <%--                            <option value="Могилёв"></option>--%>
    <%--                        </datalist>--%>
    <%--                        <c:if test="${invalid_city eq 'true'}">${invalid_city_message}</c:if>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--                <div class="row">--%>
    <%--                    <div class="col-sm-1"></div>--%>
    <%--                    <div class="form-group col-sm-4">--%>
    <%--                        <label for="street">${street}</label>--%>
    <%--                        <input type="text" class="form-control" id="street" name="street" value="${valid_street}" form="order">--%>
    <%--                        <c:if test="${invalid_street eq 'true'}">${invalid_street_message}</c:if>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--                <div class="row">--%>
    <%--                    <div class="col-sm-1"></div>--%>
    <%--                    <div class="form-group col-sm-2">--%>
    <%--                        <label for="building">${building}</label>--%>
    <%--                        <input type="number" class="form-control" id="building" name="building" value="${valid_building}" form="order">--%>
    <%--                        <c:if test="${invalid_building eq 'true'}">${invalid_building_message}</c:if>--%>
    <%--                    </div>--%>
    <%--                    <div class="form-group col-sm-2">--%>
    <%--                        <label for="block">${block}</label>--%>
    <%--                        <input type="text" class="form-control" id="block" name="block" value="${valid_block}">--%>
    <%--                        <c:if test="${invalid_block eq 'true'}">${invalid_block_message}</c:if>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--                <div class="row">--%>
    <%--                    <div class="col-sm-1"></div>--%>
    <%--                    <div class="form-group col-sm-2">--%>
    <%--                        <label for="flat">${flat}</label>--%>
    <%--                        <input type="number" class="form-control" id="flat" name="flat" value="${valid_flat}">--%>
    <%--                        <c:if test="${invalid_flat eq 'true'}">${invalid_flat_message}</c:if>--%>
    <%--                    </div>--%>
    <%--                    <div class="form-group col-sm-2">--%>
    <%--                        <label for="entrance">${entrance}</label>--%>
    <%--                        <input type="number" class="form-control" id="entrance" name="entrance" value="${valid_entrance}">--%>
    <%--                        <c:if test="${invalid_entrance eq 'true'}">${invalid_entrance_message}</c:if>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--                <div class="row">--%>
    <%--                    <div class="col-sm-1"></div>--%>
    <%--                    <div class="form-group col-sm-2">--%>
    <%--                        <label for="floor">${floor}</label>--%>
    <%--                        <input type="number" class="form-control" id="floor" name="floor" value="${valid_floor}">--%>
    <%--                        <c:if test="${invalid_floor eq 'true'}">${invalid_floor_message}</c:if>--%>
    <%--                    </div>--%>
    <%--                    <div class="form-group col-sm-2">--%>
    <%--                        <label for="intercom_code">${intercom_code}</label>--%>
    <%--                        <input type="number" class="form-control" id="intercom_code" name="intercom_code"--%>
    <%--                               value="${valid_intercom_code}">--%>
    <%--                        <c:if test="${invalid_intercom_code eq 'true'}">${invalid_intercom_code_message}</c:if>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--                <div class="row">--%>
    <%--                    <div class="col-sm-1"></div>--%>
    <%--                    <div class="col-sm-1">--%>
    <%--                        <button type="submit" class="btn btn-default">${add}</button>--%>
    <%--                    </div>--%>
    <%--                    <div class="col-sm-2">--%>
    <%--                        <c:if test="${address_adding_result eq 'true'}">${address_adding_message}</c:if>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--        </div>--%>


    <%--        <div class="col-sm-2">--%>
    <%--            <label for="delivery_time">${delivery_time}</label>--%>
    <%--            <input type="time" name="delivery_time" form="order" id="delivery_time">--%>
    <%--        </div>--%>
    <%--    </div>--%>
</div>
</body>
</html>