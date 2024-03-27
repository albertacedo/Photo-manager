<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Search</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.logged_user != null}">
                <h1>Search for posts by title and keywords</h1>
                <form action = "buscar" method = "POST">
                    <label for="title">Title contains</label><br>
                    <input type="text" id="title" name="title"><br>

                    <label for="kw">Filter keywords</label><br>
                    <input type="text" id="kw" name="kw"><br>

                    <input type="submit" value="Search">
                </form>
                <a href="index.jsp">Return to menu</a>
            </c:when>
            <c:otherwise>
                <c:redirect url = "error.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
