<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="settings.title" var="title"/>
<fmt:message key="settings.addresses" var="addresses"/>
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
<fmt:message key="settings.password_changing_message" var="password_changing_message"/>
<fmt:message key="settings.discount_card_adding_positive_message" var="discount_card_adding_positive_message"/>
<fmt:message key="settings.discount_card_adding_negative_message" var="discount_card_adding_negative_message"/>

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
<%--@elvariable id="personal_data_changing_result" type="java.lang.Boolean"--%>
<%--@elvariable id="password_changing_result" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_first_name" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_patronymic" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_last_name" type="java.lang.Boolean"--%>
<%--@elvariable id="invalid_mobile_number" type="java.lang.String"--%>
<%--@elvariable id="invalid_email" type="java.lang.String"--%>
<%--@elvariable id="password_changing_result" type="java.lang.String"--%>

<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="${abs}/js/show_hide_address.js"></script>
    <title>${title}</title>
</head>
<body>
<%@include file="../../header/header.jsp" %>
<div class="row">
    <div class="col-sm-1"></div>
    <h3 class="col-sm-3">${addresses}</h3>
</div>
<div class="row">
    <div class="col-sm-4"></div>
    <button class="col-sm-2 btn btn-success">${add_address}</button>
</div>
<div class="address">
    <form action="${abs}/controller" method="post">
        <input type="hidden" name="command" value="setting_action_list_command">
        <input type="hidden" name="action" value="address">
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="form-group col-sm-4">
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
            <div class="col-sm-1"></div>
            <div class="form-group col-sm-4">
                <label for="street">${street}</label>
                <input type="text" class="form-control" id="street" name="street" value="${valid_street}">
                <c:if test="${invalid_street eq 'true'}">${invalid_street_message}</c:if>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="form-group col-sm-2">
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
            <div class="col-sm-1"></div>
            <div class="form-group col-sm-2">
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
            <div class="col-sm-1"></div>
            <div class="form-group col-sm-2">
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
            <div class="col-sm-1"></div>
            <div class="col-sm-1">
                <button type="submit" class="btn btn-default">${add}</button>
            </div>
            <div class="col-sm-2">
                <c:if test="${address_adding_result eq 'true'}">${address_adding_message}</c:if>
            </div>
        </div>
    </form>
</div>
<div class="row">
    <div class="col-sm-1"></div>
    <h3 class="col-sm-3">${discounts}</h3>
</div>
<div class="row">
    <div class="col-sm-4"></div>
    <button class="col-sm-2 btn btn-success">${add_discount_card}</button>
</div>
<div class="discount_card">
    <form action="${abs}/controller" method="post">
        <input type="hidden" name="command" value="setting_action_list_command">
        <input type="hidden" name="action" value="discount_card">
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="form-group col-sm-4">
                <label for="card_number">${card_number}</label>
                <input type="text" class="form-control" id="card_number" name="card_number">
            </div>
        </div>
        <div class="row">
            <div class="col-sm-1"></div>
            <div class="col-sm-1">
                <button type="submit" class="btn btn-default">${add}</button>
            </div>
            <div class="col-sm-3">
                <c:choose>
                    <c:when test="${discount_card_adding_result eq 'true'}">${discount_card_adding_positive_message}</c:when>
                    <c:when test="${discount_card_adding_result eq 'false'}">${discount_card_adding_negative_message}</c:when>
                </c:choose>
            </div>
        </div>
    </form>
</div>
<form action="${abs}/controller" method="post">
    <input type="hidden" name="command" value="setting_action_list_command">
    <input type="hidden" name="action" value="personal_data">
    <div class="row">
        <div class="col-sm-1"></div>
        <h3 class="col-sm-3">${personal_data}</h3>
    </div>
    <div class="row">
        <div class="col-sm-4"></div>
        <button class="col-sm-2 btn btn-success">${change_personal_data}</button>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="form-group col-sm-3">
            <label for="first_name">${first_name}</label>
            <input type="text" class="form-control" id="first_name" name="first_name" value="${valid_first_name}">
            <c:if test="${invalid_first_name eq 'true'}">${invalid_first_name_message}</c:if>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="form-group col-sm-3">
            <label for="patronymic">${patronymic}</label>
            <input type="text" class="form-control" id="patronymic" name="patronymic" value="${valid_patronymic}">
            <c:if test="${invalid_patronymic eq 'true'}">${invalid_patronymic_message}</c:if>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="form-group col-sm-3">
            <label for="last_name">${last_name}</label>
            <input type="text" class="form-control" id="last_name" name="last_name" value="${valid_last_name}">
            <c:if test="${invalid_last_name eq 'true'}">${invalid_last_name_message}</c:if>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="form-group col-sm-3">
            <label for="mobile_number">${mobile_number}</label>
            <input type="tel" class="form-control" id="mobile_number" name="mobile_number"
                   value="${valid_mobile_number}">
            <c:choose>
                <c:when test="${invalid_mobile_number eq 'invalid_message'}">${invalid_mobile_number_message}</c:when>
                <c:when test="${invalid_mobile_number eq 'not_unique_message'}">${not_unique_mobile_number_message}</c:when>
            </c:choose>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="form-group col-sm-3">
            <label for="email_address">${email}</label>
            <input type="email" class="form-control" id="email_address" name="email_address" value="${valid_email}">
            <c:choose>
                <c:when test="${invalid_email eq 'invalid_message'}">${invalid_email_message}</c:when>
                <c:when test="${invalid_email eq 'not_unique_message'}">${not_unique_email_message}</c:when>
            </c:choose>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-1">
            <button type="submit" class="btn btn-default">${change}</button>
        </div>
        <div class="col-sm-3">
            <c:if test="${personal_data_changing_result eq 'true'}">${personal_data_changing_message}</c:if>
        </div>
    </div>
</form>

<form action="${abs}/controller" method="post">
    <input type="hidden" name="command" value="setting_action_list_command">
    <input type="hidden" name="action" value="new_password">
    <div class="row">
        <div class="col-sm-4"></div>
        <button class="col-sm-2 btn btn-success">${change_psw}</button>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="form-group col-sm-2">
            <label for="old_psw">${old_psw}</label>
            <input type="text" class="form-control" id="old_psw" name="old_password">
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="form-group col-sm-2">
            <label for="new_psw">${new_psw}</label>
            <input type="text" class="form-control" id="new_psw" name="new_password">
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="form-group col-sm-2">
            <label for="psw_confirmation">${psw_confirmation}</label>
            <input type="text" class="form-control" id="psw_confirmation" name="confirm_password">
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-1">
            <button type="submit" class="btn btn-default">${change}</button>
        </div>
        <div class="col-sm-2">
            <c:choose>
                <c:when test="${password_changing_result eq 'incorrect_message'}">${incorrect_password_message}</c:when>
                <c:when test="${password_changing_result eq 'invalid_message'}">${invalid_passport_message}</c:when>
                <c:when test="${password_changing_result eq 'password_mismatch'}">${password_mismatch_message}</c:when>
                <c:when test="${password_changing_result eq 'true'}">${password_changing_message}</c:when>
            </c:choose>
        </div>
    </div>
</form>
</body>
</html>