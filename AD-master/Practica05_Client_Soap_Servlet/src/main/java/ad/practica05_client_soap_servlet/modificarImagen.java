/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica05_client_soap_servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.ws.WebServiceRef;
import soap.Soap;
import soap.Soap_Service;
import soap.Image;


@WebServlet(name = "modificarImagen", urlPatterns = {"/modificarImagen"})
@MultipartConfig()
public class modificarImagen extends HttpServlet {
    
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Practica05_WS_SoapRest/Soap.wsdl")
    private Soap_Service service;

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

        HttpSession session = request.getSession(true);
        if (session.getAttribute("logged_user") == null) {
            response.sendRedirect("error.jsp");
            return;
        }
        
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String desc = request.getParameter("desc");
        String kw = request.getParameter("kw");
        String author = request.getParameter("author");
        String cdate = request.getParameter("cdate");
        Part imgpart = request.getPart("file");
        
        if (id == null || title == null || desc == null || kw == null
                || author == null || cdate == null) {
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
            request.setAttribute("err_msg", "Authoritzation revoked to modify image");
            rd.forward(request, response);
            return;
        }
        
        boolean success = false;
        
        try {
            int post_id = Integer.parseInt(id);
            title = title.isEmpty() ? null : title;
            desc = desc.isEmpty() ? null : desc;
            kw = kw.isEmpty() ? null : kw;
            author = author.isEmpty() ? null : author;
            cdate = cdate.isEmpty() ? null : cdate;
            String filename = (imgpart == null) ? null : imgpart.getSubmittedFileName();
            filename = (filename == null || filename.isEmpty()) ? null : filename;

            String user = (String)session.getAttribute("logged_user");
            Image img = SearchbyId(post_id);

            if(!img.equals(null) && img.getCreator().equals(user)){
                img.setId(post_id);
                img.setTitle(title);
                img.setDescription(desc);
                img.setKeywords(kw);
                img.setAuthor(author);
                img.setCaptureDate(cdate);
                img.setFilename(filename);
                success = true;
                if (imgpart != null) {
                    try (BufferedInputStream bin = new BufferedInputStream(imgpart.getInputStream());
                        ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
                        int ch;
                        while ((ch = bin.read()) != -1) {
                            bout.write(ch);
                        }
                        bout.flush();
                        byte[] data = bout.toByteArray();
                        img.setData(data);
                    }
                    catch(IOException e) {
                        System.err.println(e.getMessage());
                        success = false;
                    }
                }
                if (success) {
                    success = ModifyImage(img) >= 0;
                }
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
        if (success) {
            RequestDispatcher rd = request.getRequestDispatcher("success.jsp");  
            request.setAttribute("success_msg", "Post updated successfully");
            rd.forward(request, response);
        }
        else {
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
            request.setAttribute("err_msg", "Post cant be modified by user");
            rd.forward(request, response);
        }

    }
    
    public int ModifyImage(Image image) {
            Soap port = service.getSoapPort();
            return port.modifyImage(image);
    }
    
    public Image SearchbyId(int id) {
        Soap port = service.getSoapPort();
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
