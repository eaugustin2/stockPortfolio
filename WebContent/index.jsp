<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="loginApp">
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>

	<div ng-controller="mainCtrl">
		<form name="loginForm" action="login" method="POST">
			<input type="text" name="loginEmail" ng-model="lEmail" placeholder="Email">
			<br><br>
			<input type="password" name="loginPassword" ng-model="lPassword" placeholder="Password">
			<br><br>
			
			<input type="submit" Value="Login">
		</form>
	</div>

<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
</body>
</html>