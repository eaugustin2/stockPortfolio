<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="portfolioApp">
<head>
<meta charset="UTF-8">
<title>Stock Portfolio</title>
<link rel="stylesheet" type="text/css" href="styling/styles.css">
</head>
<body>
<%@ page import="java.util.ArrayList" %>
<%
 
//can only get here if there was a session created buy form
if(session.getAttribute("balance") == null){
	response.sendRedirect("index.jsp");
}

String email = (String)session.getAttribute("email"); //going to be used to update stockTable when a purchase is made, i think
String balance = (String)session.getAttribute("balance");

//String ticker = (String)session.getAttribute("ticker");
ArrayList<String> tickerArr = new ArrayList<String>();
tickerArr = (ArrayList<String>)session.getAttribute("ticker");

ArrayList<String> sharesArr = new ArrayList<String>();
sharesArr = (ArrayList<String>)session.getAttribute("shares");

ArrayList<String> valuesArr = new ArrayList<String>();
valuesArr = (ArrayList<String>)session.getAttribute("value");

Double sum = (Double)session.getAttribute("portfolioValue");


//System.out.println(tickerArr.get(0));

//String shares = (String)session.getAttribute("shares");
//System.out.println(email);

%>

<div class="flexContainer">

<div id="listOfStocks">
	 <!-- Add an angular filter once value comes through -->
	
	<h4>Portfolio ({{<%=sum %> | currency}})</h4>
	
	<% for(int i =0; i<tickerArr.size();i++){ %>
	
	<span> <%=tickerArr.get(i) %> <%=sharesArr.get(i) %>  -  share(s)   {{<%=valuesArr.get(i) %> | currency  }} </span> <!-- Put amount that that customer has in that share here -->
	<hr>
	<%} %>
	<br>
	
	<form action="logOut" name="logOutForm" method="POST">
<input type="submit" value="Logout">
</form>
	
</div>

<div>
	<hr id="verticalLine">
</div>

<div id="buyStocks" ng-controller="buyCtrl">

	<!-- Have to create a servlet for transactions, to go to another page for transactions -->
	<!-- <form name="transaction" method="POST" action="transactionForm">
		<input type="submit" value="Transactions">
		<input type="submit" value="Portfolio">
	</form> -->
	
	<a href="transaction.jsp">Transactions</a>


	<h4>Cash - {{<%=balance %> | currency}}</h4>

	<form method="POST" action="tickerSearch" name="buyForm">
		<input type="text" name="ticker" placeholder="Ticker" ng-model="stockTicker" ng-required="true" ng-minlength="1"> 
		<span ng-show="buyForm.ticker.$dirty && buyForm.ticker.$error.required">This field is required</span>
		<br>
		<input type="text" name="tickerQuantity" placeholder="QTY" ng-model="stockQuantity" ng-required="true"> 
		<span ng-show="buyForm.tickerQuantity.$dirty && buyForm.tickerQuantity.$error.required">This field is required</span>
		<br>
		<input type="submit" value="Buy" ng-disabled="buyForm.$invalid">
	</form>
</div>

</div>









<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<script src="JS/portfolio.js"></script>
</body>
</html>