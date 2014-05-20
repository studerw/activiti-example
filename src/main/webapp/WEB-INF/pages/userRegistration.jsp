<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>New User Register</title>
    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/resources/css/app.css" rel="stylesheet" media="screen">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand brand" href="#">Activiti Test</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container">
    <div class="starter-template">

        <c:if test="${error == true}">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="errorblock">
                        <c:if test="${not empty errors}">
                            <div class="errorBox">
                                <c:forEach var="objError" items="${errors}">
                                    ${objError.field} - ${objError.defaultMessage}<br>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">New User Registration</h3>
            </div>
            <div class="panel-body">
                <form style="margin: 20px" class="form-horizontal" role="form" id="registerHere" method="post"
                      action="${pageContext.request.contextPath}/userRegistration.htm">
                    <div class="form-group">
                        <label for="userName" class="col-sm-2 control-label">User Name</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="userName" id="userName" required
                                   autofocus>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="group" class="col-sm-2 control-label">Group</label>

                        <div class="col-sm-10">
                            <select class="form-control" name="group" id="group" required>
                                <c:forEach var="group" items="${groups}">
                                    <option value="${group.id}">${group.name}</option>
                                </c:forEach>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="firstName" class="col-sm-2 control-label">First Name</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="firstName" name="firstName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastName" class="col-sm-2 control-label">Last Name</label>

                        <div class="col-sm-10">
                            <input type="test" class="form-control" id="lastName" name="lastName" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">Email</label>

                        <div class="col-sm-10">
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">Password</label>

                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="password" id="password"
                                   placeholder="Password" required>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button type="submit" class="btn btn-primary btn-lg">Submit for Approval</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.10.1.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
</body>
</html>
