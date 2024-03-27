<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Register</title>
    </head>
    <body>
        <h1>Register a new account</h1>
        <form action = "register" method = "POST">
            <input type="text" id="username" name="username" placeholder="username"><br>
            <input type="password" id="password" name="password" placeholder="password"><br>
            <input type="submit" value="Create account">
        </form> 
        
        <br><a href="login.jsp">I already have an account</a>
    </body>
</html>