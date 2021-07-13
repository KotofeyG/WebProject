<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="ru-RU" />
<fmt:setBundle basename="localization/locale" />

<!DOCTYPE html>
<html>
    <head><title>Admin_header</title></head>
    <body>
    <form action="Menu.res" method="post">
        <input type="hidden" name="command" value="menu_edit">
        <button>
            Редактировать меню
        </button>
    </form>
    <form action="Order_Booking.res" method="post">
        <input type="hidden" name="command" value="confirmation">
        <button>
            Подтверждение заказов
        </button>
    </form>
    </body>
</html>
