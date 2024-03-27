package ad.practica02;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bd.Database;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.io.File;



@WebServlet(name = "eliminarImagen", urlPatterns = {"/eliminarImagen"})
public class eliminarImagen extends HttpServlet {

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
        try {
            int post_id = Integer.parseInt(request.getParameter("id"));
            String user = (String)session.getAttribute("logged_user");
            Database db = new Database();
            String deleted_file = db.delete_post_if_owner(post_id, user);
            // db deletion suficient for success
            success = deleted_file != null;
            if (success) {
                File img = new File(Utils.uploads_path + File.separator + deleted_file);
                img.delete();
            }
            
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        if (success) {
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
