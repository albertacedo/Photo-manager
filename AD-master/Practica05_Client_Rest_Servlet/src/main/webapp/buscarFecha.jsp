
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Search by Date</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.logged_user != null}">
                <h1>Search for posts by upload date</h1>
                <form action = "buscar" method = "POST">
                    <label for="day">Day</label><br>
                    <input type="number" id="day" name="day" placeholder="20"><br>
                    <label for="month">Month</label><br>
                    <input type="number" id="month" name="month" placeholder="02" required><br>
                    <label for="year">Year</label><br>
                    <input type="number" id="year" name="year" placeholder="2002" required><br>

                    <input type="submit" value="Search">
                </form>
                <a href="index.jsp">Return to menu</a>
            </c:when>
            <c:otherwise>
                <c:redirect url = "login.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
