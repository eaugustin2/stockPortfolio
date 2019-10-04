<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Transactions</title>
</head>
<body>
<%@ page import="java.util.ArrayList" %>

<div id="transactionLayout">
<h2><strong>Transactions</strong></h2>


<a href="portfolio.jsp">Portfolio</a> <br> <br>

<%
//String ticker = (String)session.getAttribute("ticker");
ArrayList<String> tickerArr = new ArrayList<String>();
tickerArr = (ArrayList<String>)session.getAttribute("ticker");

ArrayList<String> sharesArr = new ArrayList<String>();
sharesArr = (ArrayList<String>)session.getAttribute("shares");

ArrayList<String> valuesArr = new ArrayList<String>();
valuesArr = (ArrayList<String>)session.getAttribute("value");


%>

<% for(int i =0; i<tickerArr.size();i++){ %>
	
	<span> BUY (<%=tickerArr.get(i) %>) <%=sharesArr.get(i) %>  -  share(s)  @ {{<%=valuesArr.get(i) %> | currency  }} </span> <!-- Put amount that that customer has in that share here -->
	<hr>
	<%} %>
</div>



</body>
</html>