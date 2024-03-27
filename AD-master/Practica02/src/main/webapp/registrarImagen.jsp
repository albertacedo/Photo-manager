<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" />
        <title>Upload Image</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${sessionScope.logged_user != null}">
                <h1>Upload an image</h1>
                <form action = "registrarImagen" method = "POST" enctype="multipart/form-data">
                    <label for="title">Title</label><br>
                    <input type="text" id="title" name="title" required><br>

                    <label for="desc">Description</label><br>
                    <input type="text" id="desc" name="desc" required><br>

                    <label for="kw">Keywords</label><br>
                    <input type="text" id="kw" name="kw" required><br>

                    <label for="author">Author</label><br>
                    <input type="text" id="author" name="author" required><br>

                    <label for="cdate">Capture date</label><br>
                    <input type="date" id="cdate" name="cdate" required><br>

                    <input type="file" id="file" name="file" required><br>

                    <input type="submit" value="Post">
                </form>
            </c:when>
            <c:otherwise>
                <c:redirect url = "error.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
