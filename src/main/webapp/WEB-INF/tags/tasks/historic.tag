<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<%@ attribute name="tagList" required="true" type="java.util.Collection" %>
<%@ attribute name="document" required="true" type="com.studerw.activiti.model.document.Document" %>

<c:if test="${not empty historicTasks}">
    <c:if test="${document.docState  ne 'DRAFT' && document.docState ne 'PUBLISHED' && document.docState ne 'EMAILED'}">
        <div id="diagram" class="center-block">
            <h4>Workflow</h4>

            <p>
                <img class="img-responsive img-rounded proc-diagram" style="width: 100%"
                    src="${pageContext.request.contextPath}/workflow/diagram/documents/${document.id}"
                     <%--src="http://placehold.it/800x150.png"--%>
                     alt="Workflow Process Diagram">
            </p>
        </div>
    </c:if>
    <div id="historicTasks" class="panel panel-info">
        <div class="panel-heading">
            <span class="glyphicon glyphicon-time pull-right"></span>

            <h3 class="panel-title">Workflow History</h3>
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
