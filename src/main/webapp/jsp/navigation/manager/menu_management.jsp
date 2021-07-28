<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="menu_management.title" var="menu_title"/>
<fmt:message key="menu_management.menus" var="menu_list"/>
<fmt:message key="menu_management.menu_id" var="menu_id"/>
<fmt:message key="menu_management.menu_title" var="menu_title"/>
<fmt:message key="menu_management.menu_type" var="menu_type"/>
<fmt:message key="menu_management.menu_creation_date" var="menu_creation_date"/>
<fmt:message key="menu_management.menu_update_date" var="menu_update_date"/>
<fmt:message key="menu_management.menu_creation" var="menu_creation"/>
<fmt:message key="menu_management.menu_update" var="menu_update"/>
<fmt:message key="menu_management.menu_deletion" var="menu_deletion"/>

<jsp:useBean id="menus" scope="request" type="java.util.List"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${abs}/css/main.css">
    <title>${menu_title}</title>
</head>
<body>
<%@include file="../../header/header.jsp" %>
<div class="container-fluid">
    <%@include file="fragment/management_navbar.jspf" %>
    <form action="${abs}/controller" method="post">
        <input type="hidden" name="command" value="find_all_meals_command">
        <input type="hidden" name="page" value="menu_update">
        <div class="scroll-table">
            <table class="table-condensed table-bordered mealTable">
                <caption><h3 class="text-center">${menu_list}</h3></caption>
                <thead>
                <tr>
                    <th></th>
                    <th>${menu_id}</th>
                    <th>${menu_title}</th>
                    <th>${menu_type}</th>
                    <th>${menu_creation_date}</th>
                    <th>${menu_update_date}</th>
                </tr>
                </thead>
            </table>
            <div class="scroll-table-body">
                <table class="table-condensed table-bordered mealTable">
                    <tbody>
                    <c:forEach items="${menus}" var="menu">
                        <tr>
                            <td>
                                <label class="form-check-label">
                                    <input type="checkbox" class="form-check-input" name="selected" value="${menu.menuId}">
                                </label>
                            </td>
                            <td>${menu.menuId}</td>
                            <td>${menu.title}</td>
                            <td>${menu.type}</td>
                            <td>${menu.created}</td>
                            <td>${menu.updated}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="container text-center">
            <a href="controller?command=find_all_meals_command&page=menu_creation">
                <button type="button" class="btn btn-success">
                    <span class="glyphicon glyphicon-plus-sign"></span> ${menu_creation}
                </button>
            </a>
            <button type="submit" class="btn btn-success">
                <span class="glyphicon glyphicon-edit"></span> ${menu_update}
            </button>
            <button type="submit" name="action" value="delete" class="btn btn-danger">
                <span class="glyphicon glyphicon-minus-sign"></span> ${menu_deletion}
            </button>
        </div>
    </form>
</div>
</body>
</html>