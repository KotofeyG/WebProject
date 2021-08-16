<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../imports.jspf" %>

<fmt:message key="main.zero_number_of_meals" var="zero_number_of_meals_message"/>

<!DOCTYPE html>
<html>
<%@include file="../header/header.jsp" %>
<head>
    <link rel="stylesheet" href="${abs}/css/zero_number_of_meals.css">
    <title>Title</title>
</head>
<body>
    <h1>${zero_number_of_meals_message}
</body>
</html>
