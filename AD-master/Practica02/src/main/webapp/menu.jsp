<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Menu Page</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.logged_user != null}">
                <h1>Welcome back ${sessionScope.logged_user}!</h1>
                <a href="login.jsp">Login as another user</a><br>
                <a href="register.jsp">Register a new account</a><br>
                <br>
                <a href="registrarImagen.jsp">Post an image</a><br>
                <a href="list.jsp">Show posts</a><br>
                <a href="buscarImagen.jsp">Search posts</a>
            </c:when>
            <c:otherwise>
                <c:redirect url = "error.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
