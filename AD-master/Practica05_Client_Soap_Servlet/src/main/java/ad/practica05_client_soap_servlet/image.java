/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad.practica05_client_soap_servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import soap.Soap;
import soap.Soap_Service;
import soap.Image;



@WebServlet(name = "image", urlPatterns = {"/image"})
public class image extends HttpServlet {
    
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
        HttpSession session = request.getSession(true);
        if (session.getAttribute("logged_user") == null) {
            response.sendRedirect("error.jsp");
            return;
        }
        
        String id = request.getParameter("id");
        if (id == null) {
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
            request.setAttribute("err_msg", "Image id is required");
            rd.forward(request, response);
        }
        
        // FUNCIONA. PERO PETA AL MOSTRAR MUCHAS IMAGENES.
        // PETA POR QUE EL ARRAY DE BYTES DE LA IMAGEN ES DEMASIADO PARA SOAP¿
        // SOCKET ERROR, HE CONSEGUIDO QUE FUNCIONE UN PAR DE VECES, PERO EXPLOTA
        // EN LA MAYORIA DE CASOS
        
        
        Image img = SearchbyId(Integer.valueOf(id));
        
        if (img == null) return;
        if (img.getData().length < 1) return;
        try (ByteArrayInputStream bin = new ByteArrayInputStream(img.getData());
             BufferedOutputStream bout = new BufferedOutputStream(response.getOutputStream());) {
            String contentType = getServletContext().getMimeType(img.getFilename());
            response.setContentType(contentType);
            int ch;
            while ((ch = bin.read()) != -1) {
                bout.write(ch);
            }
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
            request.setAttribute("err_msg", "Image can't be shown");
            rd.forward(request, response);
        }
        
        
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
