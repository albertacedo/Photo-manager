<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Login</title>
    </head>
    <body>
        <h1>Please introduce your credentials</h1>
        <form action = "login" method = "POST">
            <input type="text" id="username" name="username" placeholder="username"><br>
            <input type="password" id="password" name="password" placeholder="password"><br>
            <input type="submit" value="Login">
        </form>
        
        <br><a href="register.jsp">I don't have an account</a>
    </body>
</html>
