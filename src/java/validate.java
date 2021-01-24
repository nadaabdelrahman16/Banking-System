/*
 Name :Nada Abdelrahman 
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.io.*; 
import java.util.ArrayList; 
import javax.servlet.*; 
import javax.servlet.http.*;
import javax.servlet.http.HttpSession;
/**
 *
 * @author DELL
 */
@WebServlet(urlPatterns = {"/validate"})
public class validate extends HttpServlet {
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
        try (PrintWriter out = response.getWriter()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/hello";
                String user = "root";
                String password = "1234";
                Connection Con = null;
                Statement Stmt = null;

                Con = DriverManager.getConnection(url, user, password);
                Stmt = Con.createStatement();
                String CustomerID = request.getParameter("ID");
                String CustomerPass = request.getParameter("Password");

               // Query to select all customer records in database to validate 
                String line = ("select * from customer where CustomerID ='"
                        + CustomerID 
                        + " ' AND CustomerPassword='" 
                        + CustomerPass
                        + "'");
                ResultSet RS= Stmt.executeQuery(line);
                boolean Rows = RS.next();

                if(Rows==true){//if entered ID and password exist in customer table
                    
                    HttpSession session = request.getSession(true); //to create session object
                    session.setAttribute("session_ID",CustomerID); //will make the key available in all pages.
//                  session.setAttribute("session_lName", CustomerPass);
                    request.getSession().setAttribute(user, url);
                    request.getSession().setAttribute("session_ID",CustomerID);
                    RequestDispatcher rd=request.getRequestDispatcher("customerhome.jsp"); 
                    rd.forward(request, response);
                }
                else{ //redirect back to login.HTML. with error message
                    
                     request.setAttribute("error", "your data is invalid");
                     out.print("<h2> Error in Account ID or Password </h2>\n"); 
                     RequestDispatcher rd=request.getRequestDispatcher("index.html");  
                     rd.include(request, response);
                }
                
               
                Stmt.close();
                Con.close();
    
            } catch (Exception ex) {
                ex.printStackTrace();
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
