<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="udd" scope="session" class="java.lang.String"/>
<!DOCTYPE html>
<html>
<head>
    <title>Main</title>
</head>
<body>

<%@include file="../header/header.jsp" %>
<div class="wrapper container-fluid">
        ${user.toString()}
<%--        ${udd.tostring()}--%>
</div>
<footer><%@include file="../footer/footer.jsp" %></footer>
</body>
</html>