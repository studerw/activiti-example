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
    <title>Activiti Example</title>

</head>

<body>
<jsp:include page="fragments/navbar-top.jsp"/>


<div class="container">

    <div class="start-template">
        <c:if test="${not empty alerts}">
            <div class="alert alert-warning">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <p>You currently have ${fn:length(alerts)} alert(s) not yet acknowledged.</p>
            </div>
        </c:if>
        <c:if test="${taskCount gt 0}">
            <div class="alert alert-warning">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                <p>There are ${taskCount} <a href="${pageContext.request.contextPath}/tasks.htm" class="alert-link">&nbsp; task(s) </a>
                    assigned to you or one of your groups waiting to be completed</p>
            </div>
        </c:if>
        <div class="jumbotron">
            <h2>Activiti Example</h2>

            <p class="lead">Proof of concept showing user and group management, document creation and approval
                workflows, alerting and task management, and general workflow creation.</p>

            <p>The application uses the <a href="http://activiti.org/">Activiti</a> Open Source Workflow Management API
            </p>

            <p><a class="btn btn-lg btn-success" href="http://activiti.org/" role="button">See More</a></p>
        </div>

        <div class="row marketing">
            <div class="col-lg-6">
                <h4><a href="${pageContext.request.contextPath}/document/list.htm" class="">Documents</a></h4>

                <p>Create, View, and Edit Group Documents.</p>

                <h4><a href="${pageContext.request.contextPath}/tasks.htm" class="">Tasks</a></h4>

                <p>View and complete group and assigned tasks</p>

            </div>

            <div class="col-lg-6">
                <h4><a href="${pageContext.request.contextPath}/workflow/index.htm" class="">Workflows</a></h4>

                <p>View and edit group document approval workflows</p>

                <h4><a href="${pageContext.request.contextPath}/users.htm" class="">Users and Groups</a></h4>

                <p>Manage Users and Groups</p>


            </div>
        </div>

        <div class="row marketing hidden">
            <div class="col-lg-6">
                <h4><a href="http://activiti.org/">Activiti</a></h4>
                <ul class="list-group">
                    <li><a href="https://github.com/Activiti/Activiti">GitHub Codebase</a></li>
                    <li><a href="http://forums.activiti.org/forums/activiti-users">User Forums</a></li>
                    <li><a href="http://www.activiti.org/userguide/">User Guide</a></li>
                    <li><a href="http://activiti.org/javadocs/">Javadoc API</a></li>
                </ul>

            </div>
        </div>
    </div>

</div>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp"/>
<script>
    (function ($) {
        $(document).ready(function () {
            $('a.navbar-brand').addClass('navbar-brand-active');
        });
    })(jQuery);
</script>
</body>
</html>
