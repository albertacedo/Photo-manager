/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica03_client_servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.ws.WebServiceRef;
import ws.Image;
import ws.Practica03WS;
import ws.Practica03WS_Service;

/**
 *
 * @author alumne
 */
@WebServlet(name = "eliminarImagen", urlPatterns = {"/eliminarImagen"})
public class eliminarImagen extends HttpServlet {
    
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Practica03_WS/Practica03WS.wsdl")
    private Practica03WS_Service service;

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
        HttpSession session = request.getSession(true);
        if (session.getAttribute("logged_user") == null) {
            response.sendRedirect("error.jsp");
            return;
        }
        
        String post_id = request.getParameter("id");
        
        if (post_id == null) {
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
            request.setAttribute("err_msg", "You need a valid id to delete an image");
            rd.forward(request, response);
        }

        int ret = -1;
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String user = (String)session.getAttribute("logged_user");
            
            //validated user in searchResults.jsp but we repet here for security
            Image img = SearchbyId(id);
            
            if(img.getCreator().equals(user)){
                ret = deleteImage(id);
            }
            
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        if (ret ==  0) {
            RequestDispatcher rd = request.getRequestDispatcher("success.jsp");  
            request.setAttribute("success_msg", "Post deleted successfully");
            rd.forward(request, response);
        }
        else {
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
            request.setAttribute("err_msg", "Post cant be deleted by user");
            rd.forward(request, response);
        }
    
    }
    
    
    public int deleteImage(int id) {
            Practica03WS port = service.getPractica03WSPort();
            return port.deleteImage(id);
    }
    
    public Image SearchbyId(int id) {
        Practica03WS port = service.getPractica03WSPort();
        return port.searchbyId(id);
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
