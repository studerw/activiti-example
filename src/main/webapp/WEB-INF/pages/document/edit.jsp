<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"  %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Documents</title>

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
        <ol class="breadcrumb">
            <li><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/document/list.htm">Documents</a></li>
            <li class="active">Doc ${document.id}</li>
        </ol>

        <c:if test="${error == true}">
            <div class="errorblock">
                <c:if test="${not empty errors}">
                    <div class="errorBox">
                        <c:forEach var="objError" items="${errors}">
                            ${objError.field} - ${objError.defaultMessage}<br>
                        </c:forEach>
                    </div>
                </c:if>
                <c:if test="${not empty errorMsg}">
                    <div class="errorBox">
                        <p><strong>${errorMsg}</strong></p>
                    </div>
                </c:if>
            </div>
        </c:if>

        <c:if test="${not empty msg}">
            <div class="flash">
                <p><strong>${msg}</strong></p>
            </div>
        </c:if>
    </div>

    <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">${document.title}</h3>
            </div>
            <div class="panel-body">
                <form:form cssStyle="margin: 20px" cssClass="form-horizontal" method="POST" commandName="document">

                    <div class="form-group">
                        <label for="docId" class="col-sm-2 control-label">ID</label>
                        <div class="col-sm-10">
                            <p id="docId" class="form-control-static">${document.id}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="author" class="col-sm-2 control-label">Author</label>
                        <div class="col-sm-10">
                            <form:input path="author" id="author" cssClass="form-control" readonly="true" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="groupId" class="col-sm-2 control-label">Group</label>
                        <div class="col-sm-10">
                            <form:input path="groupId" id="groupId" cssClass="form-control" readonly="true" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="title" class="col-sm-2 control-label">Title</label>
                        <div class="col-sm-10">
                            <form:input  cssClass="form-control" id="title" path="title" readonly="false"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="content" class="col-sm-2 control-label">Content</label>
                        <div class="col-sm-10">
                            <form:textarea  cssClass="form-control" id="content" path="content" rows="6" readonly="false"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="summary" class="col-sm-2 control-label">Summary</label>
                        <div class="col-sm-10">
                            <form:textarea  cssClass="form-control" id="summary" path="summary" rows="3" readonly="false"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="createdDate" class="col-sm-2 control-label">Created Date</label>
                        <div class="col-sm-10">
                            <form:input  cssClass="form-control" id="createdDate" path="createdDate" readonly="true"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="state" class="col-sm-2 control-label">State</label>
                        <div class="col-sm-10">
                            <form:input  cssClass="form-control" id="state" path="state" readonly="true"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <div class="checkbox">
                                <label for="isSubmit">
                                    <input name="isSubmit" id="isSubmit" type="checkbox"> Submit for Approval?
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button type="submit" class="btn btn-primary btn-default">Save Document </button>

                    </div>
                    <%--<div class="pull-right">--%>
                        <%--<button type="submit" class="btn btn-primary btn-lg">Submit for Approval</button>--%>
                    <%--</div>--%>
                </form:form>
            </div>
        </div>
    </div>

</div>
<%--end container--%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-1.10.1.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
<script>
    $( document ).ready(function() {
        $("#title").attr('required', '');
        $("#content").attr('required', '');
        $("#group").attr('required', '');
        $("#summary").attr('required', '');
    });

</script>
</body>
</html>
