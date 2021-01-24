# Banking-System
Banking System Internal Transfer Transaction Management

application has 4 pages as following

• Login.HTML where customer enter his/her ID and password to login

• Once the customer pressed login button, the servlet page called “validate” is fired. In this
page you have to connect to the database and check whether the entered ID and password
exist in customer table or not. If those ID and password existed declare a session to store that
parameters (ID) in it, then redirect to the third page “customerhome.jsp”. On the other hand,
if those ID and password didn’t exist redirect back to login.HTML.

• In “customerhome.jsp” you have to check whether the logged in customer had bank accounts
or not view the logged in customer account balance –by checking if he/she exists in
“BankAccount “table or not- If s/he didn’t exist you have to allow him press button “Add
Account”, consequently redirect Servlet called “addaccount”. On the other hand, if he was
existed this button must be impressed. 

• If account exists for the logged in customer System View User Account Current Balance
• Once the customer pressed the button “Add Account” and was redirected to servlet called
“addaccount”. The system generates a random Account Number for the customer with initial
amount=1000 then automatically redirected to the “customerhome.jsp”.
• User can view list of transaction ordered in “transactions.jsp”.
• User can make Transfer transaction in the page “transactions.jsp” as well to existing account
number as follows:
✓ User enter account number to transfer to and amount to be transferred.
✓ Confirmation message will be displayed after transaction is done successfully
✓ The transfer amount will be deducted from the account balance and added to the
other account balance
• User can cancel Transfer transaction with the following scenario:
✓ User select one transaction from list of transaction
• System redirect to a page that check if this transaction is only 1 day old or not
✓ if this transaction is 1 day old it passes: system allow user to confirm cancel
 and the balance is deducted from the account back to the original account
✓ else transaction is rejected: system does not allow user to confirm cancel
