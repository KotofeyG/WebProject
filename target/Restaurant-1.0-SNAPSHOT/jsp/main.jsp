<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="ru-RU" />
<fmt:setBundle basename="localization/locale" />

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Main</title>
    </head>
    <body>
        <div class="container-fluid">
                <%@include file="header/header.jsp"%>
        </div>
    </body>
</html>
