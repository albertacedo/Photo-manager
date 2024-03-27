<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Error</title>
    </head>
    <body>
        <h1>Something went wrong</h1>
        <c:if test="${requestScope.err_msg != null}">
            <h2>${requestScope.err_msg}</h2>
        </c:if>
        <c:choose>
            <c:when test="${sessionScope.logged_user == null}">
                <a href="login.jsp">Login</a><br>
                <a href="register.jsp">Register</a>
            </c:when>
            <c:otherwise>
                <a href="menu.jsp">Return to menu</a>
            </c:otherwise>
        </c:choose>
    </body>
</html>
