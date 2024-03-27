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
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 *
 * @author pyro
 */
@WebServlet(name = "do_post_image", urlPatterns = {"/do_post_image"})
public class do_post_image extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        Connection connection = null;
        
        try (PrintWriter out = response.getWriter()) {
            String query;
            PreparedStatement statement;
            
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2");
            
            String title = request.getParameter("title");
            String desc = request.getParameter("desc");
            String kw = request.getParameter("kw");
            String user = request.getParameter("user");
            String creator = request.getParameter("creator");
            String cdate = request.getParameter("cdate");
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String filename = request.getParameter("filename");
            
            
            query = "select * from usuarios where id_usuario = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, user);
            ResultSet rs = statement.executeQuery();
            boolean user_exists = rs.next(); // its a primary key so either empty or 1 result
            
            if (user_exists) {
                query = "INSERT INTO IMAGE (TITLE, DESCRIPTION, KEYWORDS, AUTHOR, CREATOR, CAPTURE_DATE, STORAGE_DATE, FILENAME) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(query);           
                statement.setString(1, title);
                statement.setString(2, desc);
                statement.setString(3, kw);
                statement.setString(4, user);
                statement.setString(5, creator);
                statement.setString(6, cdate);
                statement.setString(7, date);
                statement.setString(8, filename);            
                statement.executeUpdate();
                out.println("Image successfully posted!");
            }
            else {
                out.println("Incorrect username!");
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
