<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Activiti Test</title>

    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet" >
    <link href="${pageContext.request.contextPath}/resources/css/app.css" rel="stylesheet" >

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
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
                    <li><a href="${pageContext.request.contextPath}/documents.htm">Documents</a></li>
                    <li><a href="${pageContext.request.contextPath}/tasks.htm">Tasks</a></li>
                    <li><a href="${pageContext.request.contextPath}/j_spring_security_logout">Sign Out</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container">

    <div class="starter-template">
        <h2>User: ${userName}</h2>

        <p>Message ${message}</p>
    </div>
</div>
<%--end container--%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-1.10.1.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
</body>
</html>
