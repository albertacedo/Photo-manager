<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Modify a post</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${(sessionScope.logged_user != null) && (param['id'] != null)}">
                <c:set var="req" value="${pageContext.request}" />
                <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}" />        
                <img src="${baseURL}/image?id=${param['id']}" height="256"><br>
                <h2>Are you sure?</h2>
                <form action = "eliminarImagen?id=${param['id']}" method = "POST" enctype="multipart/form-data">
                    <input type="submit" value="Yes, delete">
                </form>
                <br><a href="index.jsp">Return to menu</a>
            </c:when>
            <c:otherwise>
                <c:redirect url = "error.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>