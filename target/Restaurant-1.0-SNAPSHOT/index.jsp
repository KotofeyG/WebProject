<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="jsp/imports.jspf" %>

<fmt:message key="index.title" var="title"/>

<!DOCTYPE html>
<html>
<head><title>${title}</title></head>
    <body>
        <jsp:forward page="/jsp/navigation/main.jsp"/>
<%--            <jsp:forward page="/WEB-INF/main.jsp"/>--%>
    </body>
</html>