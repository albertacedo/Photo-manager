
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Search Results</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.logged_user != null}">
                <h1>Posts</h1>
                <c:set var="req" value="${pageContext.request}" />
                <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}" />
                <c:forEach var="image" items="${images}">
                    <div>
                        <h2>${image.getTitle()}</h2>
                        ID ${image.getId()}<br>
                        <img src="${baseURL}/image?id=${image.getId()}" height="256"><br>
                        Captured on ${image.getCaptureDate()} by ${image.getAuthor()}<br>
                        Uploaded on ${image.getStorageDate()} by ${image.getCreator()}<br><br>
                        ${image.getDescription()}<br>
                        ${image.getKeywords()}<br>
                        <c:if test="${sessionScope.logged_user == image.getCreator()}">
                            <a href="${baseURL}/modificarImagen.jsp?id=${image.getId()}">Edit post</a><br>
                            <a href="${baseURL}/eliminarImagen.jsp?id=${image.getId()}">Delete post</a><br>
                        </c:if>
                    </div>
                    <br>
                </c:forEach>
                <a href="index.jsp">Return to menu</a>
            </c:when>
            <c:otherwise>
                <c:redirect url = "error.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
