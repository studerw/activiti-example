<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="candidateTask" required="true" type="com.studerw.activiti.model.task.CandidateTask"%>

<spring:eval expression="T(com.studerw.activiti.model.document.DocType).BOOK_REPORT" var="bookReportTypeStr" />
<spring:eval expression="T(com.studerw.activiti.model.document.DocType).INVOICE" var="invoiceTypeStr" />

<div id="collapseOne-${candidateTask.id}" class="panel-collapse collapse">
    <div class="panel-body">
        <form class="form-horizontal" role="form" method="post"
              action="${pageContext.request.contextPath}/tasks/collaborate">
            <div class="form-group">
                <label class="col-sm-2 control-label">Description</label>

                <div class="col-sm-10">
                    <p class="form-control-static">${candidateTask.name}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Created</label>

                <div class="col-sm-10">
                    <p class="form-control-static">${candidateTask.createTime}</p>
                </div>
            </div>
            <hr/>

            <div class="form-group">
                <c:set var="docType" value="${candidateTask.processVariables['docType']}"/>
                <c:choose>
                    <c:when test="${docType == bookReportTypeStr}">
                        <c:set var="subUrl" value="bookReport" scope="page"/>
                        <c:set var="docLabel" value="Book Report" scope="page"/>
                    </c:when>
                    <c:when test="${docType == invoiceTypeStr}">
                        <c:set var="subUrl" value="invoice" scope="page"/>
                        <c:set var="docLabel" value="Invoice" scope="page"/>
                    </c:when>
                    <c:otherwise>
                        <%--TODO should probably do some alert--%>
                        <c:set var="subUrl" value="UNKNOWN" scope="page"/>
                        <c:set var="docLabel" value="UNKNOWN" scope="page"/>
                    </c:otherwise>

                </c:choose>

                <label class="col-sm-2 control-label">Document</label>

                <div class="col-sm-10">
                    <p class="form-control-static">
                    <a href="${pageContext.request.contextPath}/document/${subUrl}/view.htm?id=${candidateTask.processVariables['businessKey']}"
                            onclick="window.open(this.href, 'View Document','left=20,top=20,width=800,height=600,scrollbars=1,toolbar=0,resizable=1'); return false;" >View ${docLabel}</a>
                    </p>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">Author</label>

                <div class="col-sm-10">
                    <p class="form-control-static"> ${candidateTask.processVariables['docAuthor']}</p>
                </div>
            </div>
            <%--<div class="form-group">--%>
                <%--<label class="col-sm-2 control-label">Title</label>--%>

                <%--<div class="col-sm-10">--%>
                    <%--<p class="form-control-static"> ${candidateTask.processVariables['docTitle']}</p>--%>
                <%--</div>--%>
            <%--</div>--%>
            <hr/>
            <div class="form-group">
                <label for="comment-${candidateTask.id}"
                       class="col-sm-2 control-label">Comment</label>

                <div class="col-sm-10">
                    <textarea class="form-control" rows="3" id="comment-${candidateTask.id}"
                              name="comment" required></textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-primary">Complete Task</button>
                </div>
            </div>
            <input type="hidden" name="taskId" value="${candidateTask.id}"/>
        </form>
    </div>
</div>
