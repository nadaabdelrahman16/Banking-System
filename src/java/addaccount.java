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
import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author DELL
 */
@WebServlet(urlPatterns = {"/addaccount"})
public class addaccount extends HttpServlet {

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
                String ID  = request.getSession().getAttribute("session_ID").toString();
                int CustomerID= Integer.parseInt(ID);
                
                //query to create new BankAccount
                String query1 ="Insert into bankaccount(BankAccountID,Customer_CustomerID,BACreationDate,BACurrentBalance) values (?,?,?,?);";    
                PreparedStatement pstmt=Con.prepareStatement(query1);  
                
                Random rand = new Random(); //instance of random class
                int upperbound = 2000 ;
                  //generate random values from 0-1999
                int random = rand.nextInt(upperbound); 
                checkRandom(Stmt,random); //function to sign bankAccount ID unique
                
                long millis=System.currentTimeMillis();  //Date of creation this account
                java.sql.Date date=new java.sql.Date(millis); 
                
                pstmt.setInt(1, random);    
                pstmt.setInt(2, CustomerID);    
                pstmt.setDate(3,date);    
                pstmt.setInt(4, 1000);    
                int x= pstmt.executeUpdate();  
                
                RequestDispatcher rd=request.getRequestDispatcher("customerhome.jsp");  
                rd.forward(request, response);
 
            Stmt.close();
            Con.close();
            
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
      
     public int checkRandom(Statement Stmt, int random){
        int upperbound = 2000 ;
        String query="select BankAccountID from BankAccount ";
        try{
            ResultSet RS=Stmt.executeQuery(query);
                while (RS.next()){
                    if(RS.getInt("BankAccountID")== random){
                        Random rand = new Random();
                        random = rand.nextInt(upperbound);
                        checkRandom(Stmt,random);
                    }
                }
        }
         catch (SQLException e)
       {
        // do something appropriate with the exception, *at least*:
        e.printStackTrace();
          }
    return random ;
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
