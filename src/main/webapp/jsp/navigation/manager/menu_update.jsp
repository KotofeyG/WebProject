<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="menu_update.title" var="title"/>
<fmt:message key="menu_update.dish_list" var="dish_list"/>
<fmt:message key="menu_update.cancel" var="cancel"/>
<fmt:message key="menu_update.menu_title" var="menu_title"/>
<fmt:message key="menu_update.menu_type" var="menu_type"/>
<fmt:message key="menu_update.change" var="change"/>
<fmt:message key="menu_update.meal_id" var="meal_id"/>
<fmt:message key="menu_update.meal_title" var="meal_title"/>
<fmt:message key="menu_update.meal_image" var="meal_image"/>
<fmt:message key="menu_update.meal_type" var="meal_type"/>
<fmt:message key="menu_update.meal_price" var="meal_price"/>
<fmt:message key="menu_update.meal_ingredients" var="meal_ingredients"/>
<fmt:message key="menu_update.meal_status" var="meal_status"/>
<fmt:message key="menu_update.meal_add_delete" var="add_delete"/>
<fmt:message key="menu_update.append_meal" var="append"/>
<fmt:message key="menu_update.remove_meal" var="remove"/>
<fmt:message key="menu_update.title_change" var="title_change"/>
<fmt:message key="menu_update.type_change" var="type_change"/>
<fmt:message key="menu_update.new_title" var="new_title"/>
<fmt:message key="menu_update.new_type" var="new_type"/>
<fmt:message key="meal_management.empty_picture" var="empty_picture"/>

<jsp:useBean id="meals" scope="request" type="java.util.Map"/>

<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>
<div class="container-fluid">
    <%@include file="../../header/header.jsp" %>
    <div class="scroll-table">
        <table class="table-condensed table-bordered mealTable">
            <caption><h3 class="text-center">${dish_list}</h3></caption>
            <thead>
            <tr>
                <td colspan="2">
                    <a href="${abs}/controller?command=menu_management_command">
                        <button type="button" class="btn btn-warning form-group" data-bs-dismiss="modal">${cancel}</button>
                    </a>
                </td>
                <td colspan="2">
                    <b>${menu_title} ${requestScope.selected_menu.title}</b>
                    <button class="btn-success" type="button" data-toggle="modal" data-target="#title_change">${change}</button>
                </td>
                <td colspan="2">
                    <b>${menu_type} ${requestScope.selected_menu.type}</b>
                    <button class="btn-success" type="button" data-toggle="modal" data-target="#type_change">${change}</button>
                </td>
            </tr>
            <tr>
                <th>${meal_id}</th>
                <th>${meal_title}</th>
                <th>${meal_image}</th>
                <th>${meal_type}</th>
                <th>${meal_price}</th>
                <th>${meal_ingredients}</th>
                <th>${meal_status}</th>
                <th>${add_delete}</th>
            </tr>
            </thead>
        </table>
        <div class="scroll-table-body">
            <table class="table-condensed table-bordered mealTable">
                <tbody>
                <c:forEach items="${meals}" var="entry">
                    <tr>
                        <td>${entry.key.id}</td>
                        <td>${entry.key.title}</td>
                        <td> <img src="${entry.key.image}" class="img-thumbnail" alt="${empty_picture}"/></td>
                        <td>${entry.key.type}</td>
                        <td>${entry.key.price}</td>
                        <td>${entry.key.recipe}</td>
                        <td>${entry.key.active}</td>
                        <td>
                            <form action="controller" method="post">
                                <input type="hidden" name="command" value="menu_update_command">
                                <input type="hidden" name="selected" value="${entry.key.id}"/>
                                <input type="hidden" name="selected_menu" value="${requestScope.selected_menu.id}"/>
                                <c:choose>
                                    <c:when test="${entry.value eq 'true'}">
                                        <button type="submit" name="action" value="remove" class="btn-danger">${remove}
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" name="action" value="append" class="btn-success">${append}
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div id="title_change" class="modal fade" role="dialog">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">${title_change}</h4>
                </div>
                <div class="modal-body">
                    <form action="controller" method="post" id="menu-title-form" class="form-inline">
                        <input type="hidden" name="command" value="menu_update_command">
                        <div class="form-group">
                            <label for="title">${new_title}</label>
                            <input type="text"  id="title" class="form-control" name="title">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="submit" form="menu-title-form" class="btn btn-secondary" name="action"
                            value="title_change" data-bs-dismiss="modal">${change}
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div id="type_change" class="modal fade" role="dialog">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">${type_change}</h4>
                </div>
                <div class="modal-body">
                    <form action="controller" id="menu-type-form" class="form-inline" method="post">
                        <input type="hidden" name="command" value="menu_update_command">
                        <div class="form-group">
                            <label for="type">${new_type}</label>
                            <input type="text" class="form-control" name="type" value="" id="type">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="submit" form="menu-type-form" class="btn btn-secondary" name="action"
                            value="type_change" data-bs-dismiss="modal">${change}
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>