<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error</title>

    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet" >
    <link href="${pageContext.request.contextPath}/resources/css/app.css" rel="stylesheet" >

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/respond.min.js"></script>
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
            <a class="navbar-brand brand" href="#">Activiti Example</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/document/list.htm">Documents</a></li>
                <li><a href="${pageContext.request.contextPath}/tasks.htm">Tasks</a></li>
                <li><a href="${pageContext.request.contextPath}/j_spring_security_logout">Sign Out</a></li>
            </ul>
            <p class="navbar-text navbar-right"><a href="#" class="navbar-link">${userName}</a></p>
            <a href="#" class="navbar-text navbar-right navbar-link">Alerts <span class="badge">0</span></a>

        </div>
    </div>
</div>

<div class="container">

    <div class="start-template">
        <h3>Oops...</h3>
        <h3>${response.message}</h3>

    </div>
</div>
<jsp:include page="/WEB-INF/pages/fragments/footer.jsp"/></body>
</html>
