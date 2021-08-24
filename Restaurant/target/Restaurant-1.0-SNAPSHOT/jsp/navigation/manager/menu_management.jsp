<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="menu_management.title" var="menu_title"/>
<fmt:message key="menu_management.menu_creation" var="menu_creation"/>
<fmt:message key="menu_management.menu_adding_to_main_page" var="menu_adding_to_main_page"/>
<fmt:message key="menu_management.menu_update" var="menu_update"/>
<fmt:message key="menu_management.menu_deletion" var="menu_deletion"/>
<fmt:message key="menu_management.negative_menu_search_result_message" var="negative_menu_search_message"/>
<fmt:message key="menu_management.action_negative_message" var="action_negative_message"/>
<fmt:message key="menu_management.action_positive_message" var="action_positive_message"/>

<%--@elvariable id="menu_search_result" type="java.lang.Boolean"--%>
<%--@elvariable id="menu_action_result" type="java.lang.Boolean"--%>

<!DOCTYPE html>
<html>
<%@include file="../../header/header.jsp" %>
<head>
    <script src="${abs}/js/message.js"></script>
    <link rel="stylesheet" href="${abs}/css/meal_management.css">
    <title>${menu_title}</title>
</head>
<body>
<div class="container-fluid">
    <form action="${abs}/controller" method="post">
        <div class="button-group">
            <a href="controller?command=show_menu_creation_info_command">
                <button type="button" class="btn btn-primary">
                    <span class="glyphicon glyphicon-plus-sign"></span> ${menu_creation}
                </button>
            </a>
            <button type="submit" class="btn btn-success" name="command" value="add_menu_to_main_page_command">
                <span class="glyphicon glyphicon-edit"></span> ${menu_adding_to_main_page}
            </button>
            <button type="submit" class="btn btn-warning" name="command" value="show_menu_details_command">
                <span class="glyphicon glyphicon-edit"></span> ${menu_update}
            </button>
            <button type="submit" class="btn btn-danger" name="command" value="menu_delete_command">
                <span class="glyphicon glyphicon-minus-sign"></span> ${menu_deletion}
            </button>
        </div>
        <c:choose>
            <c:when test="${menu_action_result eq 'true'}"><div class="alert alert-success" id="message"><b class="valid_message">${action_positive_message}</b></div></c:when>
            <c:when test="${menu_action_result eq 'false'}"><div class="alert alert-warning" id="message"><b class="invalid_message">${action_negative_message}</b></div></c:when>
        </c:choose>
        <c:choose>
            <c:when test="${menus.isEmpty() eq 'false'}"><%@include file="fragment/menu_table.jspf" %></c:when>
            <c:otherwise><h1>${negative_menu_search_message}</h1></c:otherwise>
        </c:choose>
    </form>
</div>
</body>
</html>