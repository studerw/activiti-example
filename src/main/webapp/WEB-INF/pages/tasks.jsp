<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Activiti Example Tasks</title>

    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/app.css" rel="stylesheet">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/resources/js/html5shiv.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/respond.min.js"></script>
    <![endif]-->
    <style type="text/css">
        .panel.panel-default {
            margin: 15px 0;
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
                <li><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/document/list.htm">Documents</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/tasks.htm">Tasks</a></li>
                <li><a href="${pageContext.request.contextPath}/j_spring_security_logout">Sign Out</a></li>
            </ul>
            <p class="navbar-text navbar-right"><a href="#" class="navbar-link">${userName}</a></p>
            <a href="#" class="navbar-text navbar-right navbar-link">Alerts <span class="badge">0</span></a>
        </div>
    </div>
</div>

<div class="container">

    <div class="start-template">
        <div class="page-header">
            <span class="glyphicon glyphicon-tasks pull-right"></span>

            <h2>Tasks
                <small>${userName}</small>
            </h2>
        </div>

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


    <c:choose>
        <c:when test="${not empty tasks}">
            <div class="panel-group" id="accordion">
                <c:forEach var="task" items="${tasks}">
                    <c:set var="currId" value="${task.id}"/>
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <c:choose>
                                <c:when test="${task.name eq 'Approve New User Account'}">
                                    <span class="glyphicon glyphicon-user pull-right"></span>
                                </c:when>
                                <c:when test="${task.name eq 'Approve Document'}">
                                    <span class="glyphicon glyphicon-paperclip pull-right"></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="glyphicon glyphicon-tasks pull-right"></span>
                                </c:otherwise>
                            </c:choose>

                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne-${task.id}">
                                        ${task.name} - ${task.createTime}
                                </a>
                            </h4>
                        </div>
                        <div id="collapseOne-${currId}" class="panel-collapse collapse">
                            <div class="panel-body">
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
                                    <c:if test="${task.name eq 'Approve New User Account'}">
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
                                    </c:if>
                                    <c:if test="${task.name eq 'Approve Document'}">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Document Title</label>

                                            <div class="col-sm-10">
                                                <p class="form-control-static"><a
                                                        href="${pageContext.request.contextPath}/document/view.htm?id=${task.processVariables['docId']}"
                                                        target="_blank">View document</a>
                                                </p>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Document Author</label>

                                            <div class="col-sm-10">
                                                <p class="form-control-static"> ${task.processVariables['docAuthor']}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Document State</label>

                                            <div class="col-sm-10">
                                                <p class="form-control-static"> ${task.processVariables['docTitle']}</p>
                                            </div>
                                        </div>
                                    </c:if>
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
                                                           value="true" checked>Approve
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
                                                           value="false">Reject
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
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <p>You do not currently have any tasks to complete.</p>
        </c:otherwise>
    </c:choose>
</div>


</div>
</div>


<div id="footer">
    <div class="container">
        <p class="text-muted">Place sticky footer content here.</p>
    </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/jquery-1.10.1.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.js"></script>
<%--<script src="${pageContext.request.contextPath}/resources/js/app/tasks/main.js"></script>--%>
</body>
</html>
