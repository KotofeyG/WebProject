<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head><title>Registration</title></head>
    <body>
        <form name="LoginForm" method="post" action="controller">
            <input type="hidden" name="command" value="sign_in" />
            <input type="text" name="login" value="" />
            <input type="password" name="password" value="" />
            <input type="submit" value="Log in" />
        </form>
    </body>
</html>