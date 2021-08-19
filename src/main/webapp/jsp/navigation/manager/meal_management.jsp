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

<%--@elvariable id="meal_action_result" type="java.lang.Boolean"--%>
<%--@elvariable id="meal_creation_result" type="java.lang.String"--%>

<!DOCTYPE html>
<html>
<%@include file="../../header/header.jsp" %>
<head>
    <script src="${abs}/js/message.js"></script>
    <link rel="stylesheet" href="${abs}/css/meal_management.css">
    <title>${meal_title}</title>
</head>
<body>
<div class="container-fluid">
    <form action="${abs}/controller" method="post">
        <input type="hidden" name="command" value="meal_list_action_command">
        <c:choose>
            <c:when test="${meals.isEmpty() ne 'true'}">
                <div class="button-group">
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
                <c:choose>
                    <c:when test="${meal_creation_result eq 'valid'}">
                        <div class="alert alert-success" id="message"><b class="valid_message">${valid_meal_creation_data}</b></div>
                    </c:when>
                    <c:when test="${meal_creation_result eq 'invalid'}">
                        <div class="alert alert-warning" id="message"><b class="invalid_message">${invalid_meal_creation_data}</b></div>
                    </c:when>
                    <c:when test="${meal_creation_result eq 'not_unique'}">
                        <div class="alert alert-warning" id="message"><b class="invalid_message">${not_unique_meal_creation_data}</b></div>
                    </c:when>
                    <c:when test="${meal_action_result eq 'true'}">
                        <div class="alert alert-success" id="message"><b class="valid_message">${action_result_positive_message}</b></div>
                    </c:when>
                    <c:when test="${meal_action_result eq 'false'}">
                        <div class="alert alert-warning" id="message"><b class="invalid_message">${action_result_negative_message}</b></div>
                    </c:when>
                </c:choose>
                <%@include file="fragment/meal_table.jspf" %>
            </c:when>
            <c:otherwise><h1>${negative_meal_search_message}</h1></c:otherwise>
        </c:choose>
    </form>
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