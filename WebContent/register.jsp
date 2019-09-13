<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="registerApp">
<head>
<meta charset="UTF-8">
<title>Registration Form</title>
</head>
<body>

	<h3>Register</h3>
	
	<div ng-controller="registerCtrl">
	
		<form name="registerForm" method="POST" action="register">
			<input type="text" name="fName" placeholder="First Name" ng-model="firstName" ng-required="true" ng-minlength="2">
			<span ng-show="registerForm.fName.$dirty && registerForm.fName.$error.required">
				Must enter a first name
			</span>
			
			<span ng-show="registerForm.fName.$dirty && registerForm.fName.$error.minlength">
				Must be atleast 2 characters long
			</span>
			<br>
			
			<input type="text" name="lName" placeholder="Last Name" ng-model="lastName" ng-required="true" ng-minlength="2">
			<span ng-show="registerForm.lName.$dirty && registerForm.lName.$error.required">
				Must enter a last name
			</span>
			
			<span ng-show="registerForm.lName.$dirty && registerForm.lName.$error.minlength">
				Must be atleast 2 characters long
			</span>
			<br>
			
			<input type="text" name="email" placeholder="Email" ng-model="regEmail" ng-required="true"> <!-- Need regex for email format -->
			<span ng-show="registerForm.email.$dirty && registerForm.email.$error.required">
				Must enter an email
			</span>
			<br>
			
			<input type="password" name="regPassword" placeholder="Password" ng-model="rPassword" ng-required="true">
			<br>
			<input type="submit" value="Register" ng-disabled="registerForm.$invalid">
			
		</form>
		
	</div>
	
	
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<script src="JS/register.js"></script>
</body>
</html>