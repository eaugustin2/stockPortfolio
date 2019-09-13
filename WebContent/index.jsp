<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="loginApp">
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>

	<h3>Login</h3>

	<div ng-controller="mainCtrl">
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

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<script src="JS/functionality.js"></script>
</body>
</html>