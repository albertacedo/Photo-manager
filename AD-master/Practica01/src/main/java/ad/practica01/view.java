/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica01;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import bd.Database;
import java.util.Map;
/**
 *
 * @author pyro
 */
@WebServlet(name = "view", urlPatterns = {"/view"})
public class view extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection connection = null;
        response.setContentType("text/html;charset=UTF-8");
        Database bd = new Database();
        try (PrintWriter out = response.getWriter()) {
            Map<String,Object> test = bd.create_user(22, 34.2);
            String thing = (String)test.get("test");
            out.println("<h2>"+thing+"</h2>");
            
            String query;
            PreparedStatement statement;
            
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2"); 
            
            out.println("<h2>Usuarios</h2");
            // Select information from users and images and show in the web
            query = "select * from usuarios";
            statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();    

            while (rs.next()) {
                // read the result set
                out.println("Id usuario = " + rs.getString("id_usuario"));
                out.println("<br>Password = " + rs.getString("password"));
                out.println("<br><br>");
            }
            
            out.println("<h2>Imagenes subidas</h2");
            query = "select * from image";
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();                                    

            while (rs.next()) {
                // read the result set
                out.println("Id image = " + rs.getString("id"));
                out.println("<br>Titulo = " + rs.getString("title"));
                out.println("<br>Descripción = " + rs.getString("description"));
                out.println("<br>Keywords = " + rs.getString("keywords"));
                out.println("<br>Autor = " + rs.getString("author"));
                out.println("<br>Creador = " + rs.getString("creator"));
                out.println("<br>Fecha = " + rs.getString("capture_date"));
                out.println("<br>Fecha subida = " + rs.getString("storage_date"));
                out.println("<br>Archivo = " + rs.getString("filename"));
                out.println("<br><br>");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }   
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
