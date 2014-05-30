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
    <jsp:include page="../fragments/head.jsp"/>
    <title>Document View</title>

</head>

<body>
<jsp:include page="../fragments/navbar-top.jsp"/>

<div class="container">
    <div class="start-template">
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

    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="glyphicon glyphicon-file pull-right"></span>

            <h3 class="panel-title"><strong>${document.title}</strong></h3>
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
                        <p id="author" class="form-control-static">${document.author}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label for="groupId" class="col-sm-2 control-label">Group</label>

                    <div class="col-sm-10">
                        <form:input path="groupId" id="groupId" cssClass="form-control" readonly="true"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="title" class="col-sm-2 control-label">Title</label>

                    <div class="col-sm-10">
                        <form:input cssClass="form-control" id="title" path="title" readonly="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="content" class="col-sm-2 control-label">Content</label>

                    <div class="col-sm-10">
                        <form:textarea cssClass="form-control" id="content" path="content" rows="6" readonly="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="summary" class="col-sm-2 control-label">Summary</label>

                    <div class="col-sm-10">
                        <form:textarea cssClass="form-control" id="summary" path="summary" rows="3" readonly="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="createdDate" class="col-sm-2 control-label">Created Date</label>

                    <div class="col-sm-10">
                        <form:input cssClass="form-control" id="createdDate" path="createdDate" readonly="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="state" class="col-sm-2 control-label">State</label>

                    <div class="col-sm-10">
                        <form:input cssClass="form-control" id="state" path="state" readonly="true"/>
                    </div>
                </div>

                <%--<div class="pull-right">--%>
                <%--<button type="submit" class="btn btn-primary btn-lg">Submit for Approval</button>--%>
                <%--</div>--%>
            </form:form>
        </div>
    </div>
    <hr/>
    <c:if test="${not empty historicTasks}">
        <c:if test="${document.state  ne 'DRAFT' && document.state ne 'PUBLISHED'}">
            <div id="diagram" class="center-block">
                <h3>Document Approval Workflow
                <small>${document.groupId}</small></h3>

                <p>
                    <img class="img-responsive img-rounded proc-diagram"
                         src="${pageContext.request.contextPath}/workflow/document/${document.id}/diagram"
                         alt="Workflow Process Diagram">
                </p>
            </div>
        </c:if>

        <div id="historicTasks" class="panel panel-info">
            <div class="panel-heading">
                <span class="glyphicon glyphicon-time pull-right"></span>

                <h3 class="panel-title">Approval History</h3>
            </div>
            <div class="panel-body">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <td>Task ID</td>
                        <td>Description</td>
                        <td>User</td>
                        <td>Date Completed</td>
                        <td>Action</td>
                        <td>Comment</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="hTask" items="${historicTasks}">
                        <tr>

                            <td>${hTask.id}</td>
                            <td>${hTask.name}</td>
                            <td>${hTask.userId}</td>
                            <td><spring:eval expression="hTask.completedDate"/></td>
                            <td>${hTask.localVars['taskOutcome']}</td>
                            <td>${hTask.comments[0].fullMessage}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
</div>
<jsp:include page="/WEB-INF/pages/fragments/footer.jsp"/>
<script>
    $(document).ready(function () {
        $("#title").attr('required', '');
        $("#content").attr('required', '');
        $("#group").attr('required', '');
        $("#summary").attr('required', '');
        $('li#nav-docs').addClass('active');
    });

</script>
</body>
</html>
