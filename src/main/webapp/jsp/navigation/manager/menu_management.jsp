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
<%--@elvariable id="unselected_menu" type="java.lang.Boolean"--%>

<!DOCTYPE html>
<html>
<head>
    <title>${menu_title}</title>
</head>
<body>
<%@include file="../../header/header.jsp" %>
<div class="container-fluid">
    <form action="${abs}/controller" method="post">
        <c:choose>
            <c:when test="${menu_search_result eq 'true'}"><%@include file="fragment/menu_table.jspf"%></c:when>
            <c:otherwise>${negative_menu_search_message}</c:otherwise>
        </c:choose>
        <div class="container text-center">
            <a href="controller?command=menu_creation_command">
                <button type="button" class="btn btn-primary">
                    <span class="glyphicon glyphicon-plus-sign"></span> ${menu_creation}
                </button>
            </a>
            <button type="submit" class="btn btn-success" name="command" value="menu_adding_to_main_command">
                <span class="glyphicon glyphicon-edit"></span> ${menu_adding_to_main_page}
            </button>
            <button type="submit" class="btn btn-warning" name="command" value="menu_update_command">
                <span class="glyphicon glyphicon-edit"></span> ${menu_update}
            </button>
            <button type="submit" class="btn btn-danger" name="command" value="menu_delete_command">
                <span class="glyphicon glyphicon-minus-sign"></span> ${menu_deletion}
            </button>
        </div>
    </form>
    <c:choose>
        <c:when test="${unselected_menu eq 'true'}">${action_negative_message}</c:when>
        <c:when test="${unselected_menu eq 'false'}">${action_positive_message}</c:when>
    </c:choose>
</div>
</body>
</html>