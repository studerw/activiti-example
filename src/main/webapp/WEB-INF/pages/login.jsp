<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign In</title>

    <!-- Bootstrap core CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/resources/css/app.css" rel="stylesheet" media="screen">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/respond.min.js"></script>
    <![endif]-->

    <style type="text/css">
        body {
            padding-top: 40px;
            padding-bottom: 40px;
            /*background-color: #eee;*/
        }

        .centered {
            margin: 0 auto;
        }

        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }

        .form-signin .checkbox {
            font-weight: normal;
        }

    </style>
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
            <a class="navbar-brand brand" href="#">Activiti Example</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/j_spring_security_logout">Sign Out</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container">
    <div class="starter-template" style="width: 550px; margin: 10px auto;">
        <div class="centered">
            <c:if test="${error == true}">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="bg-danger">
                            <p>Your login attempt was not successful.</p>

                            <p> Cause: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</p>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty msg}">
                <div class="flash">
                   <p><strong>${msg}</strong></p>
                </div>
            </c:if>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Login</h3>
            </div>
            <div class="panel-body">
                <form style="margin: 20px;" class="form-horizontal" role="form" action="j_spring_security_check"
                      method="post">
                    <div class="form-group">
                        <label for="j_username" class="col-sm-2 control-label">User</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="j_username" name="j_username" required
                                   autofocus>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="j_password" class="col-sm-2 control-label">Password</label>

                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="j_password" name="j_password"
                                   placeholder="Password" required>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <button class="btn btn-primary btn-lg pull-right" name="submit" type="submit">Sign in</button>
                </form>
            </div>
        </div>

        <div class="pull-right">
            <button class="btn" data-toggle="modal" data-target="#userModal">
                <span class="glyphicon glyphicon-user"></span> Users
            </button>
        </div>

        <div class="centered" style="margin: 15px 0;">
            <h4><a href="${pageContext.request.contextPath}/userRegistration.htm">New User Registration</a></h4>
        </div>


        </button>
        <!-- Modal -->
        <div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">Current Users</h4>
                    </div>
                    <div class="modal-body">
                        <ul class="list-group">
                            <c:forEach var="user" items="${users}">
                                <li class="list-group-item"><strong>${user.key}: </strong> (${user.value})</li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="footer">
    <div class="container">
        <p class="text-muted">Place sticky footer content here.</p>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/jquery-1.10.1.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
</body>
</html>
