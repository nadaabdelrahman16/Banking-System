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
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 *
 * @author DELL
 */
@WebServlet(urlPatterns = {"/transfer"})
public class transfer extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * 
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
                String  Acc = request.getParameter("AccNumber");
                String amount=request.getParameter("amount");  
                int AccountID =Integer.parseInt(Acc);  
                int Amount =Integer.parseInt(amount);
             
                //query to check if customer can transfer this amount or not 
                String query1 = ("select BACurrentBalance from BankAccount where Customer_CustomerID ='"
                        + CustomerID
                        + "'");
                ResultSet RS= Stmt.executeQuery(query1);
                boolean Rows = RS.next();
                int CustomerBalance = RS.getInt("BACurrentBalance");
                if((CustomerBalance)>=(Amount)){
                    
                        String query2 = ("select * from BankAccount where Customer_CustomerID ='"
                        + AccountID
                        + "'");
                        
                        ResultSet RS1= Stmt.executeQuery(query2);
                        boolean Rows1 = RS1.next();
                        if(Rows1!=true){//Account Id isn't exist in database so it is wrong
                            request.setAttribute("errorTransfer", "your AccountID is invalid");
                            RequestDispatcher rd=request.getRequestDispatcher("transactions.jsp");  
                            rd.include(request, response);
                        }
                        
                        int balance = RS1.getInt("BACurrentBalance");
                
                
                        String query3 = "update BankAccount set BACurrentBalance = ? where Customer_CustomerID = ?"; 
                        PreparedStatement pstmt=Con.prepareStatement(query3); 
                        int new_balance = Amount+ balance ;
                        pstmt.setInt(1,new_balance);    
                        pstmt.setInt(2,AccountID );  
                   
                        int x=pstmt.executeUpdate(); 
            
                        request.getSession().setAttribute(user, url);
                        
                        String query4 = "update BankAccount set BACurrentBalance = ? where Customer_CustomerID = ?"; 
                        PreparedStatement pstmt1=Con.prepareStatement(query4); 
                        int NewCustomerBalance = CustomerBalance-Amount ;
                        pstmt1.setInt(1,NewCustomerBalance);    
                        pstmt1.setInt(2,CustomerID );  
                   
                        int y=pstmt1.executeUpdate(); 
               
        
                        String query5="Insert into BankTransaction(BankTransactionID,BTCreationDate,BTAmount,BTFromAccount,BTToAccount,bankaccount_BankAccountID) values (?,?,?,?,?,?);";    
                        PreparedStatement pstmt2=Con.prepareStatement(query5); 
                        long millis=System.currentTimeMillis();  
                        java.sql.Date date=new java.sql.Date(millis); 
                        int bankid=RS1.getInt("BankAccountID");
                        Random rand = new Random(); //instance of random class
                        int upperbound = 1000 ;
                        //generate random values from 0-1999
                        int random = rand.nextInt(upperbound); 
                        checkRandom(Stmt,random); //function to sign banktransaction 
                        pstmt2.setInt(1, random); 
                        pstmt2.setDate(2,date);
                        pstmt2.setInt(3,Amount);    
                        pstmt2.setInt(4,CustomerID);    
                        pstmt2.setInt(5,AccountID );    
                        pstmt2.setInt(6,bankid); 
                 
             
                int z = pstmt2.executeUpdate();
                request.setAttribute("errorTransfer", "your data is valid");
                RequestDispatcher rd=request.getRequestDispatcher("transactions.jsp");  
                rd.forward(request, response);
                }
                else{
                    request.setAttribute("errorTransfer", "your amount is invalid");
                    RequestDispatcher rd=request.getRequestDispatcher("transactions.jsp");  
                    rd.include(request, response);
                }
                
                
          
                Stmt.close();
                Con.close();
       
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
    }
    public int checkRandom(Statement Stmt, int random){
        int upperbound = 1000 ;
        String query="select BankTransactionID from BankTransaction ";
        try{
            ResultSet RS=Stmt.executeQuery(query);
                while (RS.next()){
                    if(RS.getInt("BankTransactionID")== random){
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
