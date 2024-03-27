<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Index</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.logged_user != null}">
                <h1>Welcome back ${sessionScope.logged_user}!</h1>
                <a href="registrarImagen.jsp">Upload Image</a><br>
                <a href="list.jsp">Show all images</a><br>
                <br>
                <h2>Search:</h2>
                <a href="buscarID.jsp">Search image by ID</a><br>
                <a href="buscarAuthor.jsp">Search images by author</a><br>
                <a href="buscarFecha.jsp">Search images by date</a><br>
                <a href="buscarTitulo.jsp">Search images by title</a><br>
                <a href="buscarKeywords.jsp">Search images by keywords</a><br>
                <a href="buscarTituloKeywords.jsp">Search images by title and keywords</a><br>
                <br>
                <h3>________________________</h3>
                <a href="login.jsp">Login as another user</a><br>
                <a href="register.jsp">Register a new account</a><br>
                
            </c:when>
            <c:otherwise>
                <c:redirect url = "login.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
