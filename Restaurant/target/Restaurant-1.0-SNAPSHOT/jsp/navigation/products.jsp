<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../imports.jspf" %>

<fmt:message key="main.title" var="title"/>
<fmt:message key="main.rub" var="rub"/>
<fmt:message key="main.composition" var="composition"/>
<fmt:message key="main.weight" var="weight"/>
<fmt:message key="main.next" var="next"/>
<fmt:message key="main.previous" var="previous"/>
<fmt:message key="main.to_cart" var="to_cart"/>
<fmt:message key="main.wrong_parameter" var="wrong_parameter_message"/>
<fmt:message key="main.positive_result_of_insert" var="positive_result_of_insert"/>
<fmt:message key="main.negative_result_of_insert" var="negative_result_of_insert"/>
<fmt:message key="main.zero_number_of_meals" var="zero_number_of_meals_message"/>

<jsp:useBean id="current_menu" scope="session" class="com.kotov.restaurant.model.entity.Menu"/>

<%--@elvariable id="current_product_type" type="java.lang.String"--%>
<%--@elvariable id="page" type="java.lang.Integer"--%>
<%--@elvariable id="result_of_insert" type="java.lang.Boolean"--%>
<%--@elvariable id="wrong_parameter" type="java.lang.Boolean"--%>
<%--@elvariable id="pagination_item" type="com.kotov.restaurant.controller.command.PaginationItem"--%>

<!DOCTYPE html>
<html>
<%@include file="../header/header.jsp" %>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="${abs}/js/input_counter.js"></script>
    <script src="${abs}/js/message.js"></script>
    <link rel="stylesheet" href="${abs}/css/products.css">
    <title>${title}</title>
</head>
<body class="catalog-body">
<c:choose>
    <c:when test="${param.wrong_parameter eq 'true'}"><h1>${wrong_parameter_message}</h1></c:when>
    <c:when test="${current_menu.isEmpty()}"><h1>${zero_number_of_meals_message}</h1></c:when>
</c:choose>
    <div class="box box_padding catalog-wrapp">
        <div class="catalog">
            <c:forEach items="${current_menu.meals}" var="order">
                <div class="catalog-item">
                    <div class="product">
                        <div class="product_header">
                            <div class="product_title">${order.title}</div>
                        </div>
                        <div class="product_figure"><img src="${order.image}" alt="${order.title}" title="${order.title}" class="product_img">
                        </div>
                        <div class="product_info">${weight}</div>
                        <div class="product_consist">${composition} ${order.recipe}<br><br><br></div>
                        <div class="">
                            <div class="product__cost">
                                <div class="product_price"><b id="total_price">${order.price}</b> ${rub}</div>
                            </div>
                            <c:if test="${user.role eq 'CLIENT'}">
                                <form action="${abs}/controller" method="post">
                                    <input type="hidden" name="command" value="add_meal_to_cart_command">
                                    <input type="hidden" name="selected" value="${order.id}">
                                    <input type="hidden" name="product" value="${current_menu.type.toString()}">
                                    <input type="hidden" name="page" value="${page}">
                                    <div class="product_actions">
                                        <div class="counter">
                                            <div class="counter_btn counter_btn_minus">-</div>
                                            <input type="text" class="counter_number" id="meal_number" name="meal_number" value="1">
                                            <div class="counter_btn counter_btn_plus">+</div>
                                        </div>
                                        <button type="submit" class="btn btn_type_light js_add-to-cart">${to_cart}</button>
                                    </div>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
<c:choose>
    <c:when test="${param.result_of_insert eq 'true'}"><div class="alert alert-success" id="message9"><b class="valid_message">${positive_result_of_insert}</b></div></c:when>
    <c:when test="${param.result_of_insert eq 'false'}"><div class="alert alert-warning" id="message9"><b class="invalid_message">${negative_result_of_insert}</b></div></c:when>
</c:choose>
    <div class="pages">
            <c:choose>
                <c:when test="${current_menu.isEmpty() ne 'true' or wrong_parameter eq 'true'}">
                <ul class="pagination">
                    <li>
                        <a class="page-link ${pagination_item.isFirstPage() ? 'disabled' : ''}" href="${abs}/controller?command=show_product_info_command&product=${current_product_type}&page=${pagination_item.currentPage - 1}">${previous}</a>
                    </li>
                    <c:forEach var="i" begin="1" end="${pagination_item.pageCount()}">
                        <li class="${pagination_item.currentPage eq i ? 'active': ''}">
                            <a class="page-link" href="${abs}/controller?command=show_product_info_command&product=${current_product_type}&page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                    <li>
                        <a class="page-link ${pagination_item.isLastPage() ? 'disabled' : ''}"
                           href="${abs}/controller?command=show_product_info_command&product=${current_product_type}&page=${pagination_item.currentPage + 1}">${next}</a>
                    </li>
                </ul>
                </c:when>
                <c:otherwise>
                    <jsp:forward page="/jsp/navigation/zero_number_of_meals.jsp"/>
                </c:otherwise>
            </c:choose>
    </div>
<footer>
    <%@include file="../footer/footer.jsp" %>
</footer>
</body>
</html>