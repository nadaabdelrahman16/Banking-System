<%-- 
    Document   : customerhome
    Created on : Dec 18, 2020, 11:46:44 AM
    Author     : Nada Abdelrahman
    Name :Nada Abdelrahman Mabrouk Soukr
    ID :20170315
    Group : DS-IS 1
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@ page import = "java.io.*,java.util.*" %>
<link rel="stylesheet" href="mystyle2.css">
<% Class.forName("com.mysql.jdbc.Driver").newInstance(); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer</title>
            
            <script type="text/javascript">
			function showMessage(){
				alert("Hello, you can't add account because you already have an account.");
			}
		</script>
    </head>
    <body>
       
         <%
            String url = "jdbc:mysql://localhost:3306/hello";
            String user = "root";
            String password = "1234";
            String Line;
            Connection Con = null;
            Statement Stmt = null;
            ResultSet RS = null;
          

            try {
                Con = DriverManager.getConnection(url, user, password);
                Stmt = Con.createStatement();
                String CustomerID =(String) request.getSession().getAttribute("session_ID");
               
                RS = Stmt.executeQuery("SELECT * FROM BankAccount where Customer_CustomerID ='"
                        + CustomerID 
                        + "'");
                
                 
               } catch (Exception cnfe) {
                System.err.println("Exception: " + cnfe);
            }
            boolean Rows = RS.next();
                if(Rows!=true){
                    %>
                    <h1> you don't have Bank Account so you have to add Account</h1>
                    <form>
                        <input type="button" onclick="location.href='addaccount';" value="Add account" />
                    </form>
               <% } 
                if(Rows ==true){
                    %>
                   <table border="1">
                   <tr>
                   <th>User Account Current Balance</th>
                   <th>User Account Number</th>
                   </tr> 
                         <tr>
                         <td><%=RS.getString("BACurrentBalance")%></td>
                          <td><%=RS.getInt("BankAccountID")%></td>
                       </tr>
                       
                       </table>
                       <br/>
        
                       <form>
                       <input type="button" onclick="location.href='index.html';" value="Log out" />
                       <input type="button" onclick="location.href='transactions.jsp';" value="Transaction" />
                       <input type="button" value="Add account" onClick='showMessage()'/></form>
                  <%}%>     
    </body>
</html>

<%
    RS.close();
    Stmt.close();
    Con.close();
%>
    </body>
</html>
