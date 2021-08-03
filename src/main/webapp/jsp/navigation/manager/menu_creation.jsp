<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="menu_creation.title" var="title"/>
<fmt:message key="menu_creation.cancel" var="cancel"/>
<fmt:message key="menu_creation.menu_title" var="menu_title"/>
<fmt:message key="menu_creation.menu_type" var="menu_type"/>
<fmt:message key="menu_creation.menu_creation" var="menu_creation"/>
<fmt:message key="menu_creation.valid_data_check" var="valid_data_check"/>
<fmt:message key="menu_creation.invalid_data_check" var="invalid_data_check"/>
<fmt:message key="meal_management.negative_meal_search_result_message" var="negative_meal_search_message"/>

<%--@elvariable id="menu_creation_result" type="java.lang.String"--%>
<%--@elvariable id="meal_search_result" type="java.lang.Boolean"--%>

<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>
<%@include file="../../header/header.jsp" %>
<div class="container-fluid">
    <form id="menu-form" class="form-inline" method="post" action="${abs}/controller">
        <input type="hidden" name="command" value="menu_creation_command">
        <c:choose>
            <c:when test="${meal_search_result eq 'true'}"><%@include file="fragment/meal_table.jspf" %></c:when>
            <c:otherwise>${negative_meal_search_message}</c:otherwise>
        </c:choose>
        <a href="${abs}/controller?command=menu_management_command">
            <button type="button" class="btn btn-default form-group form-group-menu-creation" data-bs-dismiss="modal">${cancel}</button>
        </a>
        <div class="form-group form-group-menu-creation">
            <label for="title">${menu_title}</label>
            <input type="text" id="title" class="form-control" name="title">
        </div>
        <div class="form-group form-group-menu-creation">
            <label for="type">${menu_type}</label>
            <input type="text" id="type" class="form-control" name="type">
        </div>
        <button type="submit" form="menu-form" class="btn btn-default form-group form-group-menu-creation"
                >${menu_creation}
        </button>
    </form>
    <c:choose>
        <c:when test="${menu_creation_result eq 'valid'}">${valid_data_check}</c:when>
        <c:when test="${menu_creation_result eq 'invalid'}">${invalid_data_check}</c:when>
    </c:choose>
</div>
</body>
</html>