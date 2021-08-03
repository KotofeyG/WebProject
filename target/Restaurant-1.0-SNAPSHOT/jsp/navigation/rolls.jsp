<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../imports.jspf" %>

<fmt:message key="main.title" var="title"/>

<jsp:useBean id="ROLLS" scope="application" class="com.kotov.restaurant.model.entity.Menu"/>

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
        <c:forEach items="${ROLLS.meals}" var="meal">
            <div class="catalog__item">
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
                        <div class="product__actions">
                            <div class="counter">
                                <div class="counter__btn counter__btn_minus">-</div>
                                <input type="text" class="counter__number" value="1">
                                <div class="counter__btn counter__btn_plus">+</div>
                            </div>
                            <button type="submit" class="btn btn_type_light js_add-to-cart">В корзину</button>
                        </div>
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