<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Success</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.logged_user != null}">
                <h1>Success</h1>
                <c:if test="${requestScope.success_msg != null}">
                    <h2>${requestScope.success_msg}</h2>
                </c:if>
                <c:if test="${requestScope.back_url != null}">
                    <a href="${requestScope.back_url}">${requestScope.back_msg}</a><br>
                </c:if>
                <a href="menu.jsp">Return to menu</a>
            </c:when>
            <c:otherwise>
                <c:redirect url = "error.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
