<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>
<%@taglib prefix="ctg" uri="custom_tags" %>

<fmt:message key="settings.title" var="title"/>
<fmt:message key="settings.addresses" var="user_addresses"/>
<fmt:message key="settings.adding_address" var="add_address"/>
<fmt:message key="settings.changing_personal_data" var="change_personal_data"/>
<fmt:message key="settings.changing_password" var="change_psw"/>
<fmt:message key="settings.adding" var="add"/>
<fmt:message key="settings.changing" var="change"/>
<fmt:message key="settings.city" var="city"/>
<fmt:message key="settings.street" var="street"/>
<fmt:message key="settings.building" var="building"/>
<fmt:message key="settings.block" var="block"/>
<fmt:message key="settings.flat" var="flat"/>
<fmt:message key="settings.entrance" var="entrance"/>
<fmt:message key="settings.floor" var="floor"/>
<fmt:message key="settings.intercom_code" var="intercom_code"/>
<fmt:message key="settings.discounts" var="discounts"/>
<fmt:message key="settings.adding_discount_card" var="add_discount_card"/>
<fmt:message key="settings.discount_card_number" var="card_number"/>
<fmt:message key="settings.personal_data" var="personal_data"/>
<fmt:message key="settings.first_name" var="first_name"/>
<fmt:message key="settings.patronymic" var="patronymic"/>
<fmt:message key="settings.last_name" var="last_name"/>
<fmt:message key="settings.mobile_number" var="mobile_number"/>
<fmt:message key="settings.email" var="email"/>
<fmt:message key="settings.old_password" var="old_psw"/>
<fmt:message key="settings.new_password" var="new_psw"/>
<fmt:message key="settings.password_confirmation" var="psw_confirmation"/>
<fmt:message key="settings.invalid_city_message" var="invalid_city_message"/>
<fmt:message key="settings.invalid_street_message" var="invalid_street_message"/>
<fmt:message key="settings.invalid_building_message" var="invalid_building_message"/>
<fmt:message key="settings.invalid_block_message" var="invalid_block_message"/>
<fmt:message key="settings.invalid_flat_message" var="invalid_flat_message"/>
<fmt:message key="settings.invalid_entrance_message" var="invalid_entrance_message"/>
<fmt:message key="settings.invalid_floor_message" var="invalid_floor_message"/>
<fmt:message key="settings.invalid_intercom_code_message" var="invalid_intercom_code_message"/>
<fmt:message key="settings.invalid_first_name_message" var="invalid_first_name_message"/>
<fmt:message key="settings.invalid_patronymic_message" var="invalid_patronymic_message"/>
<fmt:message key="settings.invalid_last_name_message" var="invalid_last_name_message"/>
<fmt:message key="settings.invalid_mobile_number_message" var="invalid_mobile_number_message"/>
<fmt:message key="settings.invalid_email_message" var="invalid_email_message"/>
<fmt:message key="settings.incorrect_password_message" var="incorrect_password_message"/>
<fmt:message key="settings.invalid_passport_message" var="invalid_passport_message"/>
<fmt:message key="settings.password_mismatch_message" var="password_mismatch_message"/>
<fmt:message key="settings.not_unique_mobile_number_message" var="not_unique_mobile_number_message"/>
<fmt:message key="settings.not_unique_email_message" var="not_unique_email_message"/>
<fmt:message key="settings.address_adding_message" var="address_adding_message"/>
<fmt:message key="settings.personal_data_changing_message" var="personal_data_changing_message"/>
<fmt:message key="settings.password_changing_message" var="password_change_message"/>
<fmt:message key="settings.discount_card_adding_positive_message" var="discount_card_adding_positive_message"/>
<fmt:message key="settings.discount_card_adding_negative_message" var="discount_card_adding_negative_message"/>

<jsp:useBean id="addresses" scope="request" type="java.util.List"/>

<%--@elvariable id="valid_city" type="java.lang.String"--%>
<%--@elvariable id="valid_street" type="java.lang.String"--%>
<%--@elvariable id="valid_building" type="java.lang.String"--%>
<%--@elvariable id="valid_block" type="java.lang.String"--%>
<%--@elvariable id="valid_flat" type="java.lang.String"--%>
<%--@elvariable id="valid_entrance" type="java.lang.String"--%>
<%--@elvariable id="valid_floor" type="java.lang.String"--%>
<%--@elvariable id="valid_intercom_code" type="java.lang.String"--%>
<%--@elvariable id="valid_first_name" type="java.lang.String"--%>
<%--@elvariable id="valid_patronymic" type="java.lang.String"--%>
<%--@elvariable id="valid_last_name" type="java.lang.String"--%>
<%--@elvariable id="valid_mobile_number" type="java.lang.String"--%>
<%--@elvariable id="valid_email" type="java.lang.String"--%>
<%--@elvariable id="invalid_city" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_street" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_building" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_block" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_flat" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_entrance" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_floor" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_intercom_code" type="java.lang.Boolean"--%>
<%--@elvariable id="address_adding_result" type="java.lang.Boolean"--%>
<%--@elvariable id="discount_card_adding_result" type="java.lang.Boolean"--%>
<%--@elvariable id="personal_data_change_result" type="java.lang.Boolean"--%>
<%--@elvariable id="password_change_result" type="java.lang.Boolean"--%>
<%--@elvariable id="first_name_change_result" type="java.lang.Boolean"--%>
<%--@elvariable id="patronymic_change_result" type="java.lang.Boolean"--%>
<%--@elvariable id="last_name_change_result" type="java.lang.Boolean"--%>
<%--@elvariable id="mobile_number_change_result" type="java.lang.String"--%>
<%--@elvariable id="email_change_result" type="java.lang.String"--%>
<%--@elvariable id="password_changing_result" type="java.lang.String"--%>

<!DOCTYPE html>
<html>
<%@include file="../../header/header.jsp" %>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="${abs}/js/message.js"></script>
    <link rel="stylesheet" href="${abs}/css/settings.css">
    <title>${title}</title>
</head>
<body>

<c:choose>
    <c:when test="${first_name_change_result eq 'true'}"><div class="alert alert-success" id="message"><b class="valid_message">${personal_data_changing_message}</b></div></c:when>
    <c:when test="${first_name_change_result eq 'false'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${invalid_first_name_message}</b></div></c:when>
    <c:when test="${patronymic_change_result eq 'true'}"><div class="alert alert-success" id="message"><b class="valid_message">${personal_data_changing_message}</b></div></c:when>
    <c:when test="${patronymic_change_result eq 'false'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${invalid_patronymic_message}</b></div></c:when>
    <c:when test="${last_name_change_result eq 'true'}"><div class="alert alert-success" id="message"><b class="valid_message">${personal_data_changing_message}</b></div></c:when>
    <c:when test="${last_name_change_result eq 'false'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${invalid_last_name_message}</b></div></c:when>
    <c:when test="${mobile_number_change_result eq 'true'}"><div class="alert alert-success" id="message"><b class="valid_message">${personal_data_changing_message}</b></div></c:when>
    <c:when test="${mobile_number_change_result eq 'invalid_message'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${invalid_mobile_number_message}</b></div></c:when>
    <c:when test="${mobile_number_change_result eq 'not_unique_message'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${not_unique_mobile_number_message}</b></div></c:when>
    <c:when test="${email_change_result eq 'true'}"><div class="alert alert-success" id="message"><b class="valid_message">${personal_data_changing_message}</b></div></c:when>
    <c:when test="${email_change_result eq 'invalid_message'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${invalid_email_message}</b></div></c:when>
    <c:when test="${email_change_result eq 'not_unique_message'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${not_unique_email_message}</b></div></c:when>
    <c:when test="${password_change_result eq 'true'}"><div class="alert alert-success" id="message"><b class="valid_message">${password_change_message}</b></div></c:when>
    <c:when test="${password_change_result eq 'incorrect_message'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${incorrect_password_message}</b></div></c:when>
    <c:when test="${password_change_result eq 'invalid_message'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${invalid_passport_message}</b></div></c:when>
    <c:when test="${password_change_result eq 'password_mismatch'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${password_mismatch_message}</b></div></c:when>
</c:choose>



<%--<c:choose>--%>
<%--    <c:when test="${password_change_result eq 'incorrect_message'}">${incorrect_password_message}</c:when>--%>
<%--    <c:when test="${password_change_result eq 'invalid_message'}">${invalid_passport_message}</c:when>--%>
<%--    <c:when test="${password_change_result eq 'password_mismatch'}">${password_mismatch_message}</c:when>--%>
<%--    <c:when test="${password_change_result eq 'true'}">${password_change_message}</c:when>--%>
<%--</c:choose>--%>




<%--<c:choose>--%>
<%--    <c:when test="${mobile_number_change_result eq 'true'}">${invalid_mobile_number_message}</c:when>--%>
<%--    <c:when test="${mobile_number_change_result eq 'invalid_message'}">${invalid_mobile_number_message}</c:when>--%>
<%--    <c:when test="${mobile_number_change_result eq 'not_unique_message'}">${not_unique_mobile_number_message}</c:when>--%>
<%--</c:choose>--%>

<div class="row">
    <%--    <div class="col-sm-1"></div>--%>
    <h3 class="col-sm-3 indent">${user_addresses}</h3>
</div>

<table class="checkout_table" id="checkout_table">
    <tbody>
    <c:forEach items="${addresses}" var="address">
        <tr>
            <td><ctg:AddressInfo address="${address}"/></td>
            <td class="controls"><a href="#" class="del">×</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<a href="#" class="edit" data-id="2848">Удалить</a> |--%>
<%--×--%>
<div class="row">
    <div class="col-sm-2"></div>
    <button class="col-sm-2 btn btn-success" data-toggle="collapse" data-target="#address">${add_address}</button>
</div>
<div class="address collapse ${address_adding_result eq 'true' or address_adding_result eq 'false' ? 'in' : ''}"
     id="address">
    <form action="${abs}/controller" method="post">
        <input type="hidden" name="command" value="add_new_address_command">
        <div class="row">
            <%--            <div class="col-sm-1"></div>--%>
            <div class="form-group col-sm-4 indent">
                <label for="city">${city}</label>
                <input type="text" class="form-control" id="city" name="city" value="${valid_city}" list="cities">
                <datalist id="cities">
                    <option value="Минск"></option>
                    <option value="Брест"></option>
                    <option value="Витебск"></option>
                    <option value="Гомель"></option>
                    <option value="Гродно"></option>
                    <option value="Могилёв"></option>
                </datalist>
                <c:if test="${invalid_city eq 'true'}">${invalid_city_message}</c:if>
            </div>
        </div>
        <div class="row">
            <%--            <div class="col-sm-1"></div>--%>
            <div class="form-group col-sm-4 indent">
                <label for="street">${street}</label>
                <input type="text" class="form-control" id="street" name="street" value="${valid_street}">
                <c:if test="${invalid_street eq 'true'}">${invalid_street_message}</c:if>
            </div>
        </div>
        <div class="row">
            <%--            <div class="col-sm-1"></div>--%>
            <div class="form-group col-sm-2 indent">
                <label for="building">${building}</label>
                <input type="number" class="form-control" id="building" name="building" value="${valid_building}">
                <c:if test="${invalid_building eq 'true'}">${invalid_building_message}</c:if>
            </div>
            <div class="form-group col-sm-2">
                <label for="block">${block}</label>
                <input type="text" class="form-control" id="block" name="block" value="${valid_block}">
                <c:if test="${invalid_block eq 'true'}">${invalid_block_message}</c:if>
            </div>
        </div>
        <div class="row">
            <%--            <div class="col-sm-1"></div>--%>
            <div class="form-group col-sm-2 indent">
                <label for="flat">${flat}</label>
                <input type="number" class="form-control" id="flat" name="flat" value="${valid_flat}">
                <c:if test="${invalid_flat eq 'true'}">${invalid_flat_message}</c:if>
            </div>
            <div class="form-group col-sm-2">
                <label for="entrance">${entrance}</label>
                <input type="number" class="form-control" id="entrance" name="entrance" value="${valid_entrance}">
                <c:if test="${invalid_entrance eq 'true'}">${invalid_entrance_message}</c:if>
            </div>
        </div>
        <div class="row">
            <%--            <div class="col-sm-1"></div>--%>
            <div class="form-group col-sm-2 indent">
                <label for="floor">${floor}</label>
                <input type="number" class="form-control" id="floor" name="floor" value="${valid_floor}">
                <c:if test="${invalid_floor eq 'true'}">${invalid_floor_message}</c:if>
            </div>
            <div class="form-group col-sm-2">
                <label for="intercom_code">${intercom_code}</label>
                <input type="number" class="form-control" id="intercom_code" name="intercom_code"
                       value="${valid_intercom_code}">
                <c:if test="${invalid_intercom_code eq 'true'}">${invalid_intercom_code_message}</c:if>
            </div>
        </div>
        <div class="row">
            <%--            <div class="col-sm-1"></div>--%>
            <div class="col-sm-1 indent">
                <button type="submit" class="btn btn-primary">${add}</button>
            </div>
            <div class="col-sm-2">
                <c:if test="${address_adding_result eq 'true'}">${address_adding_message}</c:if>
            </div>
        </div>
    </form>
</div>
<br/><br/>
<div class="row">
    <div class="col-sm-2"></div>
    <button class="col-sm-2 btn btn-success" data-toggle="collapse"
            data-target="#personal_data">${change_personal_data}</button>
</div>

    <div class="collapse ${personal_data_change_result eq 'true' or personal_data_change_result eq 'false' ? 'in' : ''}"
         id="personal_data">
        <div class="row">
            <h3 class="col-sm-3 indent">${personal_data}</h3>
        </div>
        <div class="row">
            <div class="form-group col-sm-3 indent">
                <label for="first_name">${first_name}</label>
                <input type="text" class="form-control" id="first_name" name="value">
            </div>
            <div class="form-group col-sm-1">
                <button type="submit" class="btn btn-primary" name="type" value="first_name">${change}</button>
            </div>
        </div>

        <form action="${abs}/controller" method="post">
            <input type="hidden" name="command" value="change_personal_data_command">
        <div class="row">
            <div class="form-group col-sm-3 indent">
                <label for="patronymic">${patronymic}</label>
                <input type="text" class="form-control" id="patronymic" name="value">
            </div>
            <div class="form-group col-sm-1">
                <button type="submit" class="btn btn-primary" name="type" value="patronymic">${change}</button>
            </div>
        </div>
        </form>
        <form action="${abs}/controller" method="post">
            <input type="hidden" name="command" value="change_personal_data_command">
        <div class="row">
            <div class="form-group col-sm-3 indent">
                <label for="last_name">${last_name}</label>
                <input type="text" class="form-control" id="last_name" name="value">
            </div>
            <div class="form-group col-sm-1">
                <button type="submit" class="btn btn-primary" name="type" value="last_name">${change}</button>
            </div>
        </div>
        </form>
        <form action="${abs}/controller" method="post">
            <input type="hidden" name="command" value="change_personal_data_command">
        <div class="row">
            <div class="form-group col-sm-3 indent">
                <label for="mobile_number">${mobile_number}</label>
                <input type="tel" class="form-control" id="mobile_number" name="value">
            </div>
            <div class="form-group col-sm-1">
                <button type="submit" class="btn btn-primary" name="type" value="mobile_number">${change}</button>
            </div>
        </div>
        </form>
        <form action="${abs}/controller" method="post">
            <input type="hidden" name="command" value="change_personal_data_command">
        <div class="row">
            <div class="form-group col-sm-3 indent">
                <label for="email_address">${email}</label>
                <input type="email" class="form-control" id="email_address" name="value">
            </div>
            <div class="form-group col-sm-1">
                <button type="submit" class="btn btn-primary" name="type" value="email_address">${change}</button>
            </div>
        </div>
        </form>
    </div>

<br/><br/>
<div class="row">
    <div class="col-sm-2"></div>
    <button class="col-sm-2 btn btn-success" data-toggle="collapse" data-target="#psw_change">${change_psw}</button>
</div>
<form action="${abs}/controller" method="post">
    <input type="hidden" name="command" value="change_user_password_command">
    <input type="hidden" name="action" value="new_password">

    <div class="collapse ${password_change_result eq 'true' or password_change_result eq 'password_mismatch'
    or password_change_result eq 'invalid_message' or password_change_result eq 'incorrect_message' ? 'in' : ''}"
         id="psw_change">
        <div class="row">
            <%--        <div class="col-sm-1"></div>--%>
            <div class="form-group col-sm-4 indent">
                <label for="old_psw">${old_psw}</label>
                <input type="text" class="form-control" id="old_psw" name="old_password">
            </div>
        </div>
        <div class="row">
            <%--        <div class="col-sm-1"></div>--%>
            <div class="form-group col-sm-4 indent">
                <label for="new_psw">${new_psw}</label>
                <input type="text" class="form-control" id="new_psw" name="new_password">
            </div>
        </div>
        <div class="row">
            <%--        <div class="col-sm-1"></div>--%>
            <div class="form-group col-sm-4 indent">
                <label for="psw_confirmation">${psw_confirmation}</label>
                <input type="text" class="form-control" id="psw_confirmation" name="confirm_password">
            </div>
        </div>
        <div class="row">
            <%--        <div class="col-sm-1"></div>--%>
            <div class="col-sm-1 indent">
                <button type="submit" class="btn btn-primary">${change}</button>
            </div>
            <div class="col-sm-2">

            </div>
        </div>
    </div>
</form>
</body>
</html>