<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Activiti Test</title>

    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/app.css" rel="stylesheet">
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
                <li><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/document/list.htm">Documents</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/tasks.htm">Tasks</a></li>
                <li><a href="${pageContext.request.contextPath}/j_spring_security_logout">Sign Out</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container">

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
            <div class="msg">
                <p class="bg-success">${msg}</p>
            </div>
        </c:if>
    </div>

    <div id="bs-content" class="starter-template">
        <div class="panel panel-default">
            <!-- Default panel contents -->
            <div class="panel-heading">Tasks for ${userName}</div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${not empty tasks}">
                        <table class="table table-striped table-bordered" style="text-align: left">
                            <c:forEach var="task" items="${tasks}">
                                <tr>
                                    <td>
                                        <form class="form-horizontal" role="form" method="post"
                                              action="${pageContext.request.contextPath}/tasks/approve.htm">
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Description</label>

                                                <div class="col-sm-10">
                                                    <p class="form-control-static">${task.name}</p>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Created</label>

                                                <div class="col-sm-10">
                                                    <p class="form-control-static"><fmt:formatDate type="date"
                                                                                                   dateStyle="medium"
                                                                                                   timeStyle="medium"
                                                                                                   value="${task.createTime}"/>
                                                    </p>
                                                </div>
                                            </div>
                                            <c:set var="userReg" value="${task.processVariables['userForm']}"/>

                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">New User ID</label>

                                                <div class="col-sm-10">
                                                    <p class="form-control-static"> ${userReg.userName}</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Group</label>

                                                <div class="col-sm-10">
                                                    <p class="form-control-static"> ${userReg.group}</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">New User Name</label>

                                                <div class="col-sm-10">
                                                    <p class="form-control-static">${userReg.firstName} ${userReg.lastName}</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">New User Email</label>

                                                <div class="col-sm-10">
                                                    <p class="form-control-static"> ${userReg.email}</p>
                                                </div>
                                            </div>
                                            <hr/>
                                            <div class="form-group">
                                                <label for="comment-${task.id}"
                                                       class="col-sm-2 control-label">Comment</label>

                                                <div class="col-sm-10">
                                                    <textarea class="form-control" rows="3" id="comment-${task.id}"
                                                              name="comment" required></textarea>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-sm-offset-2 col-sm-10">
                                                    <div class="radio">
                                                        <label>
                                                            <input type="radio" name="approved"
                                                                   id="optionsRadios1-${task.id}"
                                                                   value="true" checked>Approve this new User
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-sm-offset-2 col-sm-10">
                                                    <div class="radio">
                                                        <label>
                                                            <input type="radio" name="approved"
                                                                   id="optionsRadios2-${task.id}"
                                                                   value="false">Deny this new User
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-sm-offset-2 col-sm-10">
                                                    <button type="submit" class="btn btn-primary">Complete</button>
                                                </div>
                                            </div>
                                            <input type="hidden" name="taskId" value="${task.id}"/>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="bg-info">User has no assigned or candidate tasks</p>
                    </c:otherwise>
                </c:choose>
            </div>


        </div>


    </div>
</div>
<%--end container--%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-1.10.1.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
<%--<script src="${pageContext.request.contextPath}/resources/js/app/tasks/main.js"></script>--%>
</body>
</html>
