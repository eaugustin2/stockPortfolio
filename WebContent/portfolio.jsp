<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="portfolioApp">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
//can only get here if there was a session created buy form
if(session.getAttribute("balance") == null){
	response.sendRedirect("index.jsp");
}

String email = (String)session.getAttribute("email"); //going to be used to update stockTable when a purchase is made, i think
String balance = (String)session.getAttribute("balance");

String ticker = (String)session.getAttribute("ticker");
String shares = (String)session.getAttribute("shares");
//System.out.println(email);

%>



<h3>Email: <%=email %> </h3> 



<div id="listOfStocks">
	<h4>Portfolio ()</h4> <!-- Add an angular filter once value comes through -->
	<span><%=ticker %> - <%=shares %> shares </span>
	<hr>
</div>

<div id="buyStocks" ng-controller="buyCtrl">

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

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<script src="JS/portfolio.js"></script>
</body>
</html>