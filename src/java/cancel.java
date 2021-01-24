/*
    Name :Nada Abdelrahman 
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 *
 * @author DELL
 */
@WebServlet(urlPatterns = {"/cancel"})
public class cancel extends HttpServlet {

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
                Statement Stmt1 = null;
                String ID  = request.getSession().getAttribute("session_ID").toString(); 
                int CustomerID= Integer.parseInt(ID);
                
                Con = DriverManager.getConnection(url, user, password);
                Stmt = Con.createStatement();
   
                String  Acc = request.getParameter("CancelID");
                int TransactionID =Integer.parseInt(Acc);
                

                String query1 = "SELECT * from bankaccount "
                        + "inner JOIN banktransaction ON bankaccount.BankAccountID = banktransaction.bankaccount_BankAccountID and banktransaction.BankTransactionID = "
                        + TransactionID ;
                ResultSet RS= Stmt.executeQuery(query1);
                boolean Rows = RS.next();
                
                if(Rows!=true){
                    request.setAttribute("errorCancel", "TransactionID wrong");
                    RequestDispatcher rd=request.getRequestDispatcher("transactions.jsp");  
                    rd.forward(request, response);
                }
                int amount = RS.getInt("BTAmount");
                int balance =RS.getInt("BACurrentBalance");
                int BTToAccount=  RS.getInt("BTToAccount");
                Date TransactionDate =RS.getDate("BTCreationDate");
                
                long millis=System.currentTimeMillis();  
                java.sql.Date CurrentDate= new java.sql.Date(millis); 
                Duration diff = Duration.between(CurrentDate.toLocalDate().atStartOfDay(),TransactionDate.toLocalDate().atStartOfDay());
                long diffDays= diff.toDays();
               
                
                if((diffDays==0)||(diffDays==1)){ // check if this transaction is only 1 day old or not 
                    
                    // if this transaction is 1 day old it passes
                String query3 = "update BankAccount set BACurrentBalance = ? where Customer_CustomerID = ?"; 
                PreparedStatement pstmt=Con.prepareStatement(query3); 
                int new_balance = balance - amount ;
                pstmt.setInt(1,new_balance);    
                pstmt.setInt(2,BTToAccount);  
                   
                int x=pstmt.executeUpdate(); 
           
        
                String query4 = "delete from BankTransaction where BankTransactionID = ?" ;
                PreparedStatement pstmt2=Con.prepareStatement(query4); 
                pstmt2.setInt(1,TransactionID);
                int y = pstmt2.executeUpdate(); 
                
                
                String query5 = "update BankAccount set BACurrentBalance = ? where Customer_CustomerID = ?"; 
                PreparedStatement pstmt3=Con.prepareStatement(query5); 
                int CustomerBalance;
                CustomerBalance = RS.getInt("BACurrentBalance")+amount ;
                pstmt3.setInt(1,CustomerBalance);    
                pstmt3.setInt(2,RS.getInt("BTFromAccount"));  
                int z=pstmt3.executeUpdate(); 
                
                request.setAttribute("errorCancel", "your data is valid");
                RequestDispatcher rd=request.getRequestDispatcher("transactions.jsp");  
                rd.forward(request, response);
               
                }
                else{
                    request.setAttribute("errorCancel", "your data is invalid");
                    RequestDispatcher rd=request.getRequestDispatcher("transactions.jsp");  
                    rd.forward(request, response);
                }
                Stmt.close();
                Con.close();
       
            } catch (Exception ex) {
                ex.printStackTrace();
            }}
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
