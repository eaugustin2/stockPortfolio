<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="loginApp">
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="styling/styles.css">
</head>
<body>

<div id="flexContainer">

<div id="photoContainer">
<!-- insert a photo here to be side by side -->
 <img src="Images/indexPhoto2.jpg" alt="indexPhoto" id="indexPhoto"> 

</div>
	

	<div ng-controller="mainCtrl" id="loginContainer">
	
	<div id="loginForm">
	<h3>Login</h3>
	
		<form name="loginForm" action="login" method="POST">
			
			<input type="text" name="loginEmail" ng-model="lEmail" ng-required="true" placeholder="Email">
			<span ng-show="loginForm.loginEmail.$dirty && loginForm.loginEmail.$error.required">
				Must enter email
			</span>
			<br><br>
			<input type="password" name="loginPassword" ng-model="lPassword" ng-required="true" placeholder="Password">
			<span ng-show="loginForm.loginPassword.$dirty && loginForm.loginPassword.$error.required">
				Must enter a password
			</span>
			<br><br>
			
			<input type="submit" Value="Login" ng-disabled="loginForm.$invalid"> <br>
			
			<p>Need an account? <a href="register.jsp">Register Here</a></p>
		</form>
	</div>
</div>



<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<script src="JS/functionality.js"></script>
</body>
</html>