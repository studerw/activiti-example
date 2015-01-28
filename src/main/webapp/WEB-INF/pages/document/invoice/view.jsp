<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/tasks" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../../fragments/head.jsp"/>
    <title>View Invoice</title>

</head>

<body>
<jsp:include page="../../fragments/navbar-top.jsp"/>

<div class="container">
    <div class="start-template">
        <ol class="breadcrumb">
            <li><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/document/list.htm">Documents</a></li>
            <li class="active">${document.title}</li>
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
            <h3 class="panel-title"><strong>Invoice ${document.title}</strong></h3>
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
                    <label for="title" class="col-sm-2 control-label">Title</label>

                    <div class="col-sm-10">
                        <form:input cssClass="form-control" id="title" path="title" readonly="true"/>
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
                    <label for="payee" class="col-sm-2 control-label">Payee</label>

                    <div class="col-sm-4">
                        <form:input cssClass="form-control" id="payee" path="payee"  readonly="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="amount" class="col-sm-2 control-label">Amount</label>

                    <div class="col-sm-4">
                        <form:input cssClass="form-control" id="amount" path="amount" readonly="true" />

                    </div>
                </div>
                <div class="form-group">
                    <label for="createdDate" class="col-sm-2 control-label">Created Date</label>

                    <div class="col-sm-10">
                        <form:input cssClass="form-control" id="createdDate" path="createdDate" readonly="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="docState" class="col-sm-2 control-label">State</label>

                    <div class="col-sm-10">
                        <form:input cssClass="form-control" id="docState" path="docState" readonly="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="docType" class="col-sm-2 control-label">Doc Type</label>

                    <div class="col-sm-10">
                        <form:input cssClass="form-control" id="docType" path="docType" readonly="true"/>
                    </div>
                </div>

                <%--<div class="pull-right">--%>
                <%--<button type="submit" class="btn btn-primary btn-lg">Submit for Approval</button>--%>
                <%--</div>--%>
            </form:form>
        </div>
    </div>
    <hr/>
    <t:historic tagList="${historicTasks}" document="${document}"/>
</div>
<jsp:include page="/WEB-INF/pages/fragments/footer.jsp"/>
<script>
    $(document).ready(function () {
        $('li#nav-docs').addClass('active');
    });

</script>
</body>
</html>
