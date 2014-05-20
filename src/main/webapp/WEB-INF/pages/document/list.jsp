f<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Documents</title>

    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/app.css" rel="stylesheet">
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
                <li><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/document/list.htm">Documents</a></li>
                <li><a href="${pageContext.request.contextPath}/tasks.htm">Tasks</a></li>
                <li><a href="${pageContext.request.contextPath}/j_spring_security_logout">Sign Out</a></li>
            </ul>
            <p class="navbar-text navbar-right"><a href="#" class="navbar-link">${userName}</a></p>
            <a href="#" class="navbar-text navbar-right navbar-link">Alerts <span class="badge">0</span></a>

        </div>

    </div>
</div>

<div class="container">

    <div class="starter-template">
        <div class="page-header">
            <span class="glyphicon glyphicon-paperclip pull-right"></span>
            <h2>Documents <small>${userName}</small></h2>
        </div>

        <div class="centered">
            <c:if test="${not empty msg}">
                <div class="flash">
                    <p><strong>${msg}</strong></p>
                </div>
            </c:if>
        </div>


        <c:choose>
            <c:when test="${empty documents}">
                <p>The user does not have any group documents</p>
            </c:when>
            <c:otherwise>
                <table class="table table-striped">
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Group</th>
                        <th>Created Date</th>
                        <th>State</th>
                    </tr>

                    <c:forEach items="${documents}" var="doc">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/document/view.htm?id=${doc.id}">${doc.title}</a></td>
                            <td>${doc.author}</td>
                            <td>${doc.groupId}</td>
                            <td>${doc.createdDate}</td>
                            <td>${doc.state}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
        <hr/>
        <div class="pull-right">
            <a href="${pageContext.request.contextPath}/document/add.htm" class="btn btn-primary" role="button">
                Create New Document
            </a>
        </div>
    </div>
</div>
<%--end container--%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-1.10.1.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
</body>
</html>
