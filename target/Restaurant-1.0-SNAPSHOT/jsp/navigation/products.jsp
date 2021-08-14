<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../imports.jspf" %>

<fmt:message key="main.title" var="title"/>
<fmt:message key="main.to_cart" var="to_cart"/>
<fmt:message key="main.wrong_parameter" var="wrong_parameter_message"/>
<fmt:message key="main.zero_number_of_meals" var="zero_number_of_meals_message"/>

<jsp:useBean id="current_menu" scope="request" class="com.kotov.restaurant.model.entity.Menu"/>


<%--@elvariable id="current_product_type" type="java.lang.String"--%>

<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="${abs}/js/plus_minus_input_counter.js"></script>
    <link rel="stylesheet" href="${abs}/css/products.css">
    <title>${title}</title>
</head>
<%@include file="../header/header.jsp" %>

<body class="catalog-body">
<%--@elvariable id="wrong_parameter" type="java.lang.Boolean"--%>
<%--@elvariable id="zero_number_of_meals" type="java.lang.Boolean"--%>
<c:choose>
    <c:when test="${wrong_parameter eq 'true'}"><h1>${wrong_parameter_message}</h1></c:when>
    <c:when test="${zero_number_of_meals eq 'true'}"><h1>${zero_number_of_meals_message}</h1></c:when>
</c:choose>

<c:if test="${wrong_parameter ne 'true' or zero_number_of_meals ne 'true'}">
    <div class="box box_padding catalog-wrapp">
        <div class="catalog">
            <c:forEach items="${current_menu.meals}" var="meal">
                <div class="catalog-item">
                    <div class="product">
                        <div class="product__header">
                            <div class="product__title">
                                    ${meal.title}
                            </div>
                        </div>
                        <div class="product__figure"><img
                                src="${meal.image}"
                                alt="${meal.title}" title="${meal.title}" class="product__img">
                        </div>
                        <div class="product__info">
                            85 г
                        </div>
                        <div class="product__consist">
                            Состав: ${meal.recipe}<br><br><br></div>
                        <div class="">
                            <div class="product__cost">
                                <div class="product__price">
                                        ${meal.price}
                                </div>
                            </div>
                            <form action="${abs}/controller" method="post">
                                <input type="hidden" name="command" value="add_to_cart_command">
                                <input type="hidden" name="selected" value="${meal.id}">
                                <input type="hidden" name="product" value="${current_menu.type.toString()}">
                                <div class="product__actions">
                                    <div class="counter">
                                        <div class="counter__btn counter__btn_minus">-</div>
                                        <input type="text" class="counter__number" name="meal_number" value="1">
                                        <div class="counter__btn counter__btn_plus">+</div>
                                    </div>
                                    <button type="submit" class="btn btn_type_light js_add-to-cart">${to_cart}</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-6"></div>
        <div class="col-sm-3">
                <%--        <jsp:useBean id="pageable" scope="request" type="com.kotov.restaurant.controller.command.Page"/>--%>
            <ul class="pagination">
                <li>
                    <a class="page-link ${pageable.isFirstPage() ? 'disabled' : ''}" href="${abs}/controller?command=show_products_command&product=${current_product_type}&page=${pageable.currentPage - 1}">Previous</a>
                </li>
                <c:forEach var="i" begin="1" end="${pageable.pageCount()}">
                    <li class="${pageable.currentPage eq i ? 'active': ''}">
                        <a class="page-link" href="${abs}/controller?command=show_products_command&product=${current_product_type}&page=${i}">${i}</a>
                    </li>
                </c:forEach>
                <li>
                    <a class="page-link ${pageable.isLastPage() ? 'disabled' : ''}"
                       href="${abs}/controller?command=show_products_command&product=${current_product_type}&page=${pageable.currentPage + 1}">Next</a>
                </li>
            </ul>
        </div>
    </div>
</c:if>
<footer>
    <%@include file="../footer/footer.jsp" %>
</footer>
</body>
</html>