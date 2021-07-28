<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="menu_creation.title" var="title"/>
<fmt:message key="menu_creation.cancel" var="cancel"/>
<fmt:message key="menu_creation.menu_title" var="menu_title"/>
<fmt:message key="menu_creation.menu_type" var="menu_type"/>
<fmt:message key="menu_creation.menu_creation" var="menu_creation"/>
<fmt:message key="menu_creation.valid_data_check" var="valid_data_check"/>
<fmt:message key="menu_creation.not_unique_data_check" var="not_unique_data_check"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${abs}/css/main.css">
    <title>${title}</title>
</head>
<body>
<%@include file="../../header/header.jsp" %>
<div class="container-fluid">
    <form id="menu-form" class="form-inline" method="post" action="${abs}/controller">
        <input type="hidden" name="command" value="add_new_menu_command" formenctype="text/plain">
        <%@include file="fragment/meal_table.jspf" %>
        <a href="${abs}/controller?command=find_all_menu_command&page=menu_management">
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
                data-bs-dismiss="modal">${menu_creation}
        </button>
    </form>
    <c:choose>
        <%--@elvariable id="menu_creation_data" type="java.lang.String"--%>
        <c:when test="${menu_creation_data eq 'valid'}">${valid_data_check}</c:when>
        <c:when test="${menu_creation_data eq 'not_unique'}">${not_unique_data_check}</c:when>
    </c:choose>
</div>
</body>
</html>
