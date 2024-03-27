package ad.practica02;

import bd.Database;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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



@WebServlet(name = "modificarImagen", urlPatterns = {"/modificarImagen"})
@MultipartConfig()
public class modificarImagen extends HttpServlet {

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
        
        boolean success = false;
        
        // IMAGE
        Part imgpart = request.getPart("file");
        String file_uuid = Utils.get_storage_uuid(imgpart.getSubmittedFileName());
        String storage_path = Utils.uploads_path + File.separator + file_uuid;
        try (BufferedInputStream bin = new BufferedInputStream(imgpart.getInputStream());
            BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(storage_path, false))) {
            boolean image_mod = false;//bin.available()>0;
            int ch;
            while ((ch = bin.read()) != -1) {
                bout.write(ch);
                image_mod = true;
            }
            file_uuid = image_mod ? file_uuid : null;
            
            Database db = new Database();
            int post_id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title");
            title = title.isEmpty() ? null : title;
            String desc = request.getParameter("desc");
            desc = desc.isEmpty() ? null : desc;
            String kw = request.getParameter("kw");
            kw = kw.isEmpty() ? null : kw;
            String user = (String)session.getAttribute("logged_user");
            String author = request.getParameter("author");
            author = author.isEmpty() ? null : author;
            String cdate = request.getParameter("cdate");
            cdate = cdate.isEmpty() ? null : cdate;
            
            success = db.update_post_if_owner(post_id, title, desc, kw, author, user, cdate, file_uuid);
        }
        catch(IOException e) {
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
