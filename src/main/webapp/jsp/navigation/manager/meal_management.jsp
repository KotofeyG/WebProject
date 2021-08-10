<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../imports.jspf" %>

<fmt:message key="meal_management.title" var="meal_title"/>
<fmt:message key="meal_management.meal_creation" var="new_dish_adding"/>
<fmt:message key="meal_management.meal_activation" var="activation"/>
<fmt:message key="meal_management.meal_archiving" var="archiving"/>
<fmt:message key="meal_management.meal_deletion" var="deletion"/>
<fmt:message key="meal_management.meal_modal_title" var="meal_modal_title"/>
<fmt:message key="meal_management.valid_data_check" var="valid_meal_creation_data"/>
<fmt:message key="meal_management.invalid_data_check" var="invalid_meal_creation_data"/>
<fmt:message key="meal_management.not_unique_data_check" var="not_unique_meal_creation_data"/>
<fmt:message key="meal_management.negative_meal_search_result_message" var="negative_meal_search_message"/>
<fmt:message key="meal_management.action_result_positive_message" var="action_result_positive_message"/>
<fmt:message key="meal_management.action_result_negative_message" var="action_result_negative_message"/>

<%--@elvariable id="meal_search_result" type="java.lang.Boolean"--%>
<%--@elvariable id="meal_action_result" type="java.lang.Boolean"--%>
<%--@elvariable id="meal_creation_data" type="java.lang.String"--%>

<!DOCTYPE html>
<html>
<head>
    <title>${meal_title}</title>
</head>
<body>
<%@include file="../../header/header.jsp" %>
<div class="container-fluid">
    <form action="${abs}/controller" method="post">
        <input type="hidden" name="command" value="meal_list_action_command">
        <c:choose>
            <c:when test="${meal_search_result eq 'true'}"><%@include file="fragment/meal_table.jspf" %></c:when>
            <c:otherwise>${negative_meal_search_message}</c:otherwise>
        </c:choose>
        <div class="container text-center">
            <button type="button" data-toggle="modal" data-target="#create-meal-modal" class="btn btn-primary">
                <span class="glyphicon glyphicon-plus-sign"></span> ${new_dish_adding}
            </button>
            <button type="submit" name="action" value="unblock" class="btn btn-success">
                <span class="glyphicon glyphicon-ok-sign"></span> ${activation}
            </button>
            <button type="submit" name="action" value="block" class="btn btn-warning">
                <span class="glyphicon glyphicon-remove-sign"></span> ${archiving}
            </button>
            <button type="submit" name="action" value="delete" class="btn btn-danger">
                <span class="glyphicon glyphicon-minus-sign"></span> ${deletion}
            </button>
        </div>
    </form>
    <c:choose>
        <c:when test="${meal_creation_data eq 'valid'}">${valid_meal_creation_data}</c:when>
        <c:when test="${meal_creation_data eq 'invalid'}">${invalid_meal_creation_data}</c:when>
        <c:when test="${meal_creation_data eq 'not_unique'}">${not_unique_meal_creation_data}</c:when>
        <c:when test="${meal_action_result eq 'true'}">${action_result_positive_message}</c:when>
        <c:when test="${meal_action_result eq 'false'}">${action_result_negative_message}</c:when>
    </c:choose>
    <div id="create-meal-modal" class="modal fade">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">${meal_modal_title}</h4>
                </div>
                <div class="modal-body">
                    <form name="" action="${abs}/controller" method="post" id="meal-form" enctype="multipart/form-data">
                        <input type="hidden" name="command" value="meal_creation_command">
                        <div class="form-group">
                            <label for="title">${meal_title}</label>
                            <input type="text" id="title" class="form-control" name="title">
                        </div>
                        <div class="form-group">
                            <label for="type">${meal_type}</label>
                            <input type="text" id="type" class="form-control" list="list" name="type">
                            <datalist id="list">
                                <option value="ROLL"></option>
                                <option value="NIGIRI"></option>
                            </datalist>
                        </div>
                        <div class="form-group">
                            <label for="price">${meal_price}</label>
                            <input type="number" id="price" class="form-control" name="price">
                        </div>
                        <div class="form-group">
                            <label for="recipe">${meal_ingredients}</label>
                            <input type="text" id="recipe" class="form-control" name="recipe">
                        </div>
                        <div class="form-group">
                            <label for="image">${meal_image}</label>
                            <input type="file" id="image" name="image">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="submit" form="meal-form" class="btn btn-secondary"
                            data-bs-dismiss="modal">${new_dish_adding}
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>