/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica03_client_servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebParam;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import ws.Image;
import ws.Practica03WS;
import ws.Practica03WS_Service; 

/**
 *
 * @author pyro
 */
@WebServlet(name = "buscar", urlPatterns = {"/buscar"})
public class buscar extends HttpServlet {

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
        
        List<Image> search_results = new ArrayList<>();
        
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String keywords = request.getParameter("kw");
        String day = request.getParameter("day");
        day = day != null && day.length() > 0 ? day : null;
        String month = request.getParameter("month");
        String year = request.getParameter("year");
        String author = request.getParameter("author");
        
        if (id != null) {
            Image img = SearchbyId(Integer.valueOf(id));
            search_results.add(img);
        }
        else if (title != null && keywords != null) {
            search_results = (List<Image>)SearchTitleKeywords(title, keywords);
        }
        else if (keywords != null) {
            search_results = (List<Image>)SearchbyKeywords(keywords);
        }
        else if(title != null){
            search_results = (List<Image>)SearchbyTitle(title);
        }
        else if (year != null && month != null) {
            if (day != null) {
                String date = day + "/" + month + "/" + year;
                search_results = (List<Image>)SearchCreaDate(date);
            }
            else {
                String date = month + "/" + year;
                search_results = (List<Image>)SearchCreaDate(date);
            }
            
        }
        else if(author != null){
            search_results = (List<Image>)SearchbyAuthor(author);
        }
        else {
            search_results = (List<Image>)ListImages();
        }
        
        request.setAttribute("images", search_results);
        RequestDispatcher rd = request.getRequestDispatcher("searchResults.jsp");
        rd.forward(request, response);
    }
    
    public Image SearchbyId(int id) {
        Practica03WS port = service.getPractica03WSPort();
        return port.searchbyId(id);
    }
    
    public List<Image> ListImages() {
        Practica03WS port = service.getPractica03WSPort();
        return port.listImages();
    }
    
    public List<Image> SearchTitleKeywords(String Title, String Keywords) {
        Practica03WS port = service.getPractica03WSPort();
        return port.searchTitleKeywords(Title, Keywords);
    }
    
    public List<Image> SearchbyKeywords(String keywords) {
        Practica03WS port = service.getPractica03WSPort();
        return port.searchbyKeywords(keywords);
    }
    
    public List<Image> SearchCreaDate(String creaDate) {
        Practica03WS port = service.getPractica03WSPort();
        return port.searchCreaDate(creaDate);
    }
    
    public List<Image> SearchbyTitle(String title) {
        Practica03WS port = service.getPractica03WSPort();
        return port.searchbyTitle(title);
    }
    
    public List<Image> SearchbyAuthor(String author) {
        Practica03WS port = service.getPractica03WSPort();
        return port.searchbyAuthor(author);
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
