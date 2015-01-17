<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="fragments/head.jsp"/>
    <title>Tasks</title>
    <style type="text/css">
        div.panel.panel-default.panel-task {
            margin-bottom: 30px;
        }
    </style>
</head>

<body>
<jsp:include page="fragments/navbar-top.jsp"/>

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
                    <div class="panel panel-default panel-task">
                        <div class="panel-heading">
                            <c:choose>
                                <c:when test="${task.name eq 'Approve New User Account'}">
                                    <span class="glyphicon glyphicon-user pull-right"></span>
                                </c:when>
                                <c:when test="${fn:startsWith(task.taskDefinitionKey, 'approveDocUserTask')}">
                                    <span class="glyphicon glyphicon-paperclip pull-right"></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="glyphicon glyphicon-tasks pull-right"></span>
                                </c:otherwise>
                            </c:choose>

                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne-${task.id}">
                                        ${task.name} - <fmt:formatDate type="date"
                                                                       dateStyle="medium"
                                                                       timeStyle="medium"
                                                                       value="${task.createTime}"/>
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
                                            <p class="form-control-static">${task.createTime}</p>
                                        </div>
                                    </div>
                                    <hr/>
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
                                    <c:if test="${fn:startsWith(task.taskDefinitionKey, 'approveDocUserTask')}">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Document</label>

                                            <div class="col-sm-10">
                                                <p class="form-control-static"><a
                                                        href="${pageContext.request.contextPath}/document/view.htm?id=${task.processVariables['docId']}"
                                                        onclick="window.open(this.href, 'View Document','left=20,top=20,width=800,height=600,scrollbars=1,toolbar=0,resizable=1'); return false;" >View document</a>
                                                </p>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Author</label>

                                            <div class="col-sm-10">
                                                <p class="form-control-static"> ${task.processVariables['docAuthor']}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Title</label>

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


<jsp:include page="fragments/footer.jsp"/>

<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>
<%--<script src="${pageContext.request.contextPath}/resources/js/app/tasks/main.js"></script>--%>
<script>
    (function ($) {
        $(document).ready(function () {
            $('li#nav-tasks').addClass('active');
        });
    })(jQuery);
</script>

</body>
</html>
