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
                Leave blank any fields you wish to maintain.<br>
                <form action = "modificarImagen?id=${param['id']}" method = "POST" enctype="multipart/form-data">
                    <label for="title">Title</label><br>
                    <input type="text" id="title" name="title"><br>

                    <label for="desc">Description</label><br>
                    <input type="text" id="desc" name="desc"><br>

                    <label for="kw">Keywords</label><br>
                    <input type="text" id="kw" name="kw"><br>

                    <label for="author">Author</label><br>
                    <input type="text" id="author" name="author"><br>

                    <label for="cdate">Capture date</label><br>
                    <input type="date" id="cdate" name="cdate"><br>

                    <input type="file" id="file" name="file"><br>

                    <input type="submit" value="Save">
                </form>
            </c:when>
            <c:otherwise>
                <c:redirect url = "error.jsp" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
