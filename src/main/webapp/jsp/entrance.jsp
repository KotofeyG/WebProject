<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="ru-RU" />
<fmt:setBundle basename="localization/locale" />

<!DOCTYPE html>
<html>
    <head>
        <title>Entrance</title>
    </head>
    <body>
        <form action="Menu.res" method="post">
            <input type="hidden" name="command" value="menu">
            <button>
                Меню
            </button>
        </form>
        <form action="Comments.res" method="post">
            <input type="hidden" name="command" value="comments">
            <button>
                Отзывы
            </button>
        </form>
        <form action="Login.res" method="post">
            <input type="hidden" name="command" value="login">
            <button>
                Войти
            </button>
        </form>
        <form action="Registration.res" method="post">
            <input type="hidden" name="command" value="registration">
            <button>
                Регистрация
            </button>
        </form>
    </body>
</html>
