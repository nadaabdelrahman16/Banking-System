<%-- 
    Document   : transactions
    Created on : Dec 22, 2020, 2:29:22 AM
    Author     : Nada Abdelrahman
 

--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@ page import = "java.io.*,java.util.*" %>
<link rel="stylesheet" href="mystyle3.css">
<% Class.forName("com.mysql.jdbc.Driver").newInstance(); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>transaction Page</title>
      
            <script type="text/javascript">
function dil(form)
{
   for(var i=0; i<(form.elements.length); i++)
   {
		if(form.elements[i].value === "")
		{
		   alert("Fill out all Fields");
		   document.F1.AccNumber.focus();
		   return false;
		}
   }
    if(isNaN(document.F1.AccNumber.value))
   {
       alert("Account ID must  be  number & can't be null")
	   document.F1.AccNumber.value="";
	   document.F1.AccNumber.focus();
	   return false;
   }
    if(isNaN(document.F1.amount.value))
   {  
       alert("Amount must  be  number & can't be null")
	   document.F1.amount.value="";
	   document.F1.amount.focus();
	   return false;
   }
   return true;
}

function dil2(form)
{
   for(var i=0; i<(form.elements.length); i++)
   {
		if(form.elements[i].value === "")
		{
		   alert("Fill out all Fields");
		   document.F2.CancelID.focus();
		   return false;
		}
   }
    if(isNaN(document.F2.CancelID.value))
   {
       alert("Transaction ID must  be  number & can't be null")
	   document.F2.CancelID.value="";
	   document.F2.CancelID.focus();
	   return false;
   }   
   return true;
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
            int i=0;

            try {
                Con = DriverManager.getConnection(url, user, password);
                Stmt = Con.createStatement();
                String ID  = request.getSession().getAttribute("session_ID").toString(); 
                int CustomerID= Integer.parseInt(ID);
                RS = Stmt.executeQuery("SELECT * FROM BankTransaction WHERE BTFromAccount='"
                        + CustomerID 
                        + "' " + " OR BTToAccount ='"+ CustomerID + "'");
                 
               } catch (Exception cnfe) {
                System.err.println("Exception: " + cnfe);
            }   
           %>
                   <table border="1">
                   <tr>
                   <th>Bank Transaction ID</th>
                   <th>transaction Date</th>
                   <th>transaction amount</th>
                   <th>Source Account</th>
                   <th>Destination Account ID</th>
                   
                   </tr> 
                         <%
                           while (RS.next()) { %>
                         <tr>
                         <td><%= RS.getInt("BankTransactionID")%></td>
                         <td><%= RS.getString("BTCreationDate")%></td>
                         <td><%= RS.getString("BTAmount")%></td>
                         <td><%= RS.getString("BTFromAccount")%></td>
                         <td><%= RS.getString("BTToAccount")%></td>
                       </tr>
                       
                      
                       
                  <%}%> 
                   </table>
                   <form action='index.html'>
                       <input type="submit" onclick="location.href='index.html';" value="Log out" />
                       </form>
                  
                 
                 <form name="F1" method='post' action="transfer" onSubmit="return dil(this)">
                   <fieldset >
                       <legend> <b>Transfer Transaction</b> </legend>
                   <label for="AccNumber"> account number:</label>
                   <input type="int" id="AccNumber" name="AccNumber"><br><br>
                   <label for="amount"> amount to be transferred:</label>
                   <input type="number" id="amount" name="amount"><br><br>
                   <input type="submit" value="Transfer">
                   </fieldset>
                   </form>  

                    <form  name=F2 method='post' action="cancel" id="formCancel" onSubmit="return dil2(this)">
                   <fieldset >
                       <legend> <b>Cancel Transaction</b> </legend>
                   <label for="AccNumber">transaction ID:</label>
                   <input type="int" id="CancelID" name="CancelID"><br><br>
                   <input type="submit" value="Cancel">
                   </fieldset>
                   </form>  
                   
                  
                   
                        <%if(request.getAttribute("errorCancel")=="your data is invalid")//alerts messages when errors occurs in cancel process
			{%>
                        <script>alert("Cancel Transaction is rejected, this transaction is more 1 day old it passes");</script>
			<%}
                         if(request.getAttribute("errorCancel")=="your data is valid"){
                        %>
                        <script>alert("Cancel Transaction done successfully.");</script>
                        <%} 
                        if (request.getAttribute("errorCancel")=="TransactionID wrong"){%>
                             <script>alert("Your Transaction ID is Wrong, Check it and try again.");</script>
                        <%} %>
                       
                        
                        
                        <%if(request.getAttribute("errorTransfer")=="your amount is invalid")//alerts messages when errors occurs in Transaction process
			{%>
                        <script>alert("Transaction is rejected, your Blance is less than  Transaction's amount");</script>
			<%}
                         if(request.getAttribute("errorTransfer")=="your AccountID is invalid"){
                        %>
                        <script>alert("Transaction is rejected, Account ID isn't exist ");</script>
                        <%} 
                        if(request.getAttribute("errorTransfer")=="your data is valid"){%>
                             <script>alert("Your Transaction is Done successfully.");</script>
                        <%} %>
                         
    </body>
</html>


    </body>
</html>
