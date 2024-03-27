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
                <h1>Search for posts</h1>
                <form action = "buscarImagen" method = "POST">
                    <label for="title">Title contains</label><br>
                    <input type="text" id="title" name="title"><br>

                    <label for="kw">Filter keywords</label><br>
                    <input type="text" id="kw" name="kw"><br>

                    <input type="submit" value="Search">
                </form><br>
                
                <c:if test="${rows != null}">
                    <b>Found ${rows.size()} posts</b><br>
                </c:if>
                
                <c:set var="req" value="${pageContext.request}" />
                <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}" />
                <c:forEach var="row" items="${rows}">
                    <div>
                        <h2>${row['TITLE']}</h2>
                        <img src="${baseURL}/images?name=${row['FILENAME']}" height="256"><br>
                        Captured on ${row['CAPTURE_DATE']} by ${row['AUTHOR']}<br>
                        Uploaded on ${row['STORAGE_DATE']} by ${row['CREATOR']}<br><br>
                        ${row['DESCRIPTION']}<br><br>
                        ${row['KEYWORDS']}<br>
                        <c:if test="${sessionScope.logged_user == row['CREATOR']}">
                            <a href="${baseURL}/modificarImagen.jsp?id=${row['ID']}">Edit post</a><br>
                            <a href="${baseURL}/eliminarImagen.jsp?id=${row['ID']}">Delete post</a><br>
                        </c:if>
                        <br>
                    </div>
                </c:forEach>
                <a href="menu.jsp">Return to menu</a>
            </c:when>
            <c:otherwise>
                <c:redirect url = "error.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
