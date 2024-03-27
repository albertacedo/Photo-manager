/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica04_client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet(name = "registrarImagen", urlPatterns = {"/registrarImagen"})
@MultipartConfig()
public class registrarImagen extends HttpServlet {

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

        String title = request.getParameter("title");
        String desc = request.getParameter("desc");
        String kw = request.getParameter("kw");
        String creator = (String) session.getAttribute("logged_user");
        String author = request.getParameter("author");
        String cdate = request.getParameter("cdate");
        Part imgpart = request.getPart("file");
        String filename = imgpart.getSubmittedFileName();

        if (title == null || desc == null || kw == null || creator == null
                || author == null || cdate == null || imgpart == null || filename == null) {
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
            request.setAttribute("err_msg", "All fields are required to post an image");
            rd.forward(request, response);
        }

        
        boolean success = false;
        try (BufferedInputStream bin = new BufferedInputStream(imgpart.getInputStream());
            ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            int ch;
            while ((ch = bin.read()) != -1) {
                bout.write(ch);
            }
            bout.flush();
            Image img = new Image();
            img.setTitle(title);
            img.setDescription(desc);
            img.setKeywords(kw);
            img.setCreator(creator);
            img.setAuthor(author);
            img.setCaptureDate(cdate);
            img.setFilename(imgpart.getSubmittedFileName());
            img.setData(bout.toByteArray());
            
            RestClient rest = new RestClient();
            success = rest.registerImage(img);
        }

        if (success) {
            RequestDispatcher rd = request.getRequestDispatcher("success.jsp");
            request.setAttribute("success_msg", "Post uploaded successfully");
            request.setAttribute("back_url", "registrarImagen.jsp");
            request.setAttribute("back_msg", "Post another image");
            rd.forward(request, response);
        } else {
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
            request.setAttribute("err_msg", "Error processing your post");
            rd.forward(request, response);
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
