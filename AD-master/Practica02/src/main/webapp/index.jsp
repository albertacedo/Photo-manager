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
            <c:when test="${sessionScope.logged_user == null}">
                <c:redirect url = "login.jsp" />
            </c:when>
            <c:otherwise>
                <c:redirect url = "menu.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
