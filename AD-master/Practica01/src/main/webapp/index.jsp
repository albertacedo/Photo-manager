<%-- 
    Document   : index
    Created on : Sep 21, 2021, 10:42:27 AM
    Author     : pyro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hola! Esto es lo que hemos implementado:</h1>
        <% out.println("<a href='register.html'>Formulario de registro</a><br>"); %>
        <% out.println("<a href='post_image.html'>Formulario de subida de imagen</a><br>"); %>
        <% out.println("<a href='view'>Visualización de usuarios y posts</a>"); %>
    </body>
</html>
