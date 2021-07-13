<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="ru-RU" />
<fmt:setBundle basename="localization/locale" />
<!DOCTYPE html>
<html>
    <head>
        <title>Main</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <h1>The Last Samurai</h1>
        <c:set var="role">${sessionScope.user.getRole()}</c:set>
        <c:choose>
            <c:when test="${role eq 'ADMIN'}"><%@include file="header/admin_header.jsp"%></c:when>
            <c:when test="${role eq 'CLIENT'}"><%@include file="header/client_header.jsp"%></c:when>
            <c:otherwise><%@include file="header/admin_header.jsp"%></c:otherwise>
        </c:choose>
        <form action="History.res" method="post">
            <input type="hidden" name="command" value="history">
            <button>
                История заказов
            </button>
        </form>
        <form action="Settings.res" method="post">
            <input type="hidden" name="command" value="settings">
            <button>
                Настройки
            </button>
        </form>
        <form action="Comments.res" method="post">
            <input type="hidden" name="command" value="comments">
            <button>
                Отзывы
            </button>
        </form>
        <form action="Logout.res" method="post">
            <input type="hidden" name="command" value="logout">
            <button>
                Выйти
            </button>
        </form>
        <%@include file="footer/footer.jsp"%>
    </body>
</html>

<%--<c:otherwise><%@include file="header/guest_header.jsp"%></c:otherwise>--%>