<%@ tag import="com.studerw.activiti.model.document.DocType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags/tasks/form" %>

<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="candidateTasks" required="true" type="java.util.Collection"%>

<spring:eval expression="T(com.studerw.activiti.workflow.WFConstants).TASK_ID_DOC_APPROVAL" var="docApprovalTask" />
<spring:eval expression="T(com.studerw.activiti.workflow.WFConstants).TASK_ID_DOC_COLLABORATE" var="docCollaborateTask" />
<spring:eval expression="T(com.studerw.activiti.workflow.WFConstants).TASK_ID_USER_APPROVAL" var="userApprovalTask" />
<spring:eval expression="T(com.studerw.activiti.model.document.DocType).BOOK_REPORT" var="bookReportTypeStr" />
<spring:eval expression="T(com.studerw.activiti.model.document.DocType).INVOICE" var="invoiceTypeStr" />
<c:choose>
    <c:when test="${empty candidateTasks}">
        <p>You do not currently have any tasks to complete.</p>
    </c:when>
    <c:otherwise>
        <div class="panel-group" id="accordion">
            <c:forEach var="candidateTask" items="${candidateTasks}">
                <c:set var="docType" value="${candidateTask.processVariables['docType']}"/>
                <div class="panel panel-default panel-task">
                    <div class="panel-heading">
                        <c:choose>
                            <c:when test="${fn:startsWith(candidateTask.taskDefinitionKey, userApprovalTask )}">
                                <span class="glyphicon glyphicon-user pull-right"></span>
                            </c:when>
                            <c:when test="${fn:startsWith(candidateTask.taskDefinitionKey, docApprovalTask)}">
                                <span class="glyphicon glyphicon-thumbs-up pull-right"></span>
                            </c:when>
                            <c:when test="${fn:startsWith(candidateTask.taskDefinitionKey, docCollaborateTask)}">
                                <span class="glyphicon glyphicon-info-sign pull-right"></span>
                            </c:when>
                            <c:otherwise>
                                <span class="glyphicon glyphicon-question-sign pull-right"></span>
                            </c:otherwise>
                        </c:choose>

                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne-${candidateTask.id}">
                                    ${candidateTask.name} - <fmt:formatDate type="date" dateStyle="medium" timeStyle="medium" value="${candidateTask.createTime}"/>
                            </a>
                        </h4>
                    </div>
                    <c:choose>
                        <c:when test="${fn:startsWith(candidateTask.taskDefinitionKey, userApprovalTask )}">
                            <tf:userApproval candidateTask="${candidateTask}"/>
                        </c:when>
                        <c:when test="${fn:startsWith(candidateTask.taskDefinitionKey, docApprovalTask)}">
                            <tf:docApproval candidateTask="${candidateTask}"/>
                        </c:when>
                        <c:when test="${fn:startsWith(candidateTask.taskDefinitionKey, docCollaborateTask)}">
                            <tf:docCollaborate candidateTask="${candidateTask}"/>
                        </c:when>
                        <c:otherwise>
                            <tf:unknown candidateTask="${candidateTask}"/>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>

</c:choose>
