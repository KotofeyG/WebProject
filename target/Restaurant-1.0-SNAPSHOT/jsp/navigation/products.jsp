<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../imports.jspf" %>

<fmt:message key="main.title" var="title"/>
<fmt:message key="main.to_cart" var="to_cart"/>

<jsp:useBean id="product" scope="request" class="com.kotov.restaurant.model.entity.Menu"/>

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
<div class="box box_padding catalog-wrapp">
    <div class="catalog">
        <c:forEach items="${product.meals}" var="meal">
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
                            <input type="hidden" name="product" value="${product.type.toString()}">
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
<footer>
    <%@include file="../footer/footer.jsp" %>
</footer>
</body>
</html>