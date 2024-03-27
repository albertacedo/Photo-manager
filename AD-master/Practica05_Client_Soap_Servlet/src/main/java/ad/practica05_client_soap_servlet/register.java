
package ad.practica05_client_soap_servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import soap.Soap;
import soap.Soap_Service;
import soap.Image;


@WebServlet(name = "register", urlPatterns = {"/register"})
public class register extends HttpServlet {
    
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
        
        String input_user = request.getParameter("username");
        String input_pass = request.getParameter("password");
        
        
        if (input_user == null || input_pass == null || input_user.isEmpty() || input_pass.isEmpty()) {
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
            request.setAttribute("err_msg", "User and password are required to register");
            rd.forward(request, response);
            return;
        }
        
        boolean success = createAccount(input_user, input_pass);
        
        HttpSession session = request.getSession(true);
        if (success) {
            session.setAttribute("logged_user", input_user);
            response.sendRedirect("index.jsp");
        }
        else {
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");  
            request.setAttribute("err_msg", "Invalid user or password");
            rd.forward(request, response);
        }
    }
    
    public boolean createAccount(String user, String password) {
        Soap port = service.getSoapPort();
        return port.createAccount(user, password);
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
