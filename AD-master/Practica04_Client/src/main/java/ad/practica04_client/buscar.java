/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica04_client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author pyro
 */
@WebServlet(name = "buscar", urlPatterns = {"/buscar"})
public class buscar extends HttpServlet {
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
        
        List<Image> search_results = new ArrayList<>();
        
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String keywords = request.getParameter("kw");
        String day = request.getParameter("day");
        day = day != null && day.length() > 0 ? day : null;
        String month = request.getParameter("month");
        String year = request.getParameter("year");
        String author = request.getParameter("author");
        
        RestClient rest = new RestClient();
        
        if (id != null) {
            if(id.isEmpty()){
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
                request.setAttribute("err_msg", "No data send in form");
                rd.forward(request, response);
                return;
            }
            search_results = rest.searchByID(Integer.valueOf(id));
        }
        else if (title != null && keywords != null) {
            if(title.isEmpty() && keywords.isEmpty()){
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
                request.setAttribute("err_msg", "No data send in form");
                rd.forward(request, response);
                return;
            }
            search_results = rest.searchByTitleKeywords(title, keywords);
        }
        else if (keywords != null) {
            if(keywords.isEmpty()){
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
                request.setAttribute("err_msg", "No data send in form");
                rd.forward(request, response);
                return;
            }
            search_results = rest.searchByKeywords(keywords);
        }
        else if(title != null){
            if(title.isEmpty()){
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
                request.setAttribute("err_msg", "No data send in form");
                rd.forward(request, response);
                return;
            }
            search_results = rest.SearchbyTitle(title);
        }
        else if (year != null && month != null) {
            if (day != null) {
                String date = day + "/" + month + "/" + year;
                search_results = rest.searchByCreationDate(date);
            }
            else {
                String date = month + "/" + year;
                search_results = rest.searchByCreationDate(date);
            }
            
        }
        else if(author != null){
            if(author.isEmpty()){
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
                request.setAttribute("err_msg", "No data send in form");
                rd.forward(request, response);
                return;
            }
            search_results = rest.SearchbyAuthor(author);
        }
        else {
            search_results = rest.listImages();
        }
        RequestDispatcher rd = request.getRequestDispatcher("searchResults.jsp");
        request.setAttribute("images", search_results);
        rd.forward(request, response);
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
