<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../fragments/head.jsp"/>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap-choosen.css" rel="stylesheet">

    <title>Workflow Edit</title>

    <style type="text/css">
        .panel-task {
            margin: 15px 0;
        }

        div#diagram {
            margin: 20px auto;
        }
    </style>
</head>
<body>
<jsp:include page="../fragments/navbar-top.jsp"/>

<div class="container">

    <div class="start-template">
        <div class="page-header">
            <span class="glyphicon glyphicon-cog pull-right"></span>

            <h3>Workflows</h3>
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
        <h4>${param.group}</h4>

        <div id="docTypeSelForm">
            <div class="form-group">
                <label for="docTypeSel" class="">Document Type</label>
                <select class="form-control" id="docTypeSel">
                    <option value="">Choose a Document Type</option>
                    <c:forEach var="docType" items="${docTypes}">
                        <option value="${docType}">${docType}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div id="groupSelForm" class="hidden">
            <div class="form-group">
                <label for="groupSel" class="">Group</label>
                <select class="form-control" id="groupSel">
                    <option value="">Please Choose a Group</option>
                    <c:forEach var="group" items="${groups}">
                        <option value="${group.id}">${group.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <hr/>

        <div id="dynamicTasks" class="hidden">
            <div class="page-header">
                <%--<span class="glyphicon glyphicon-paperclip pull-right"></span>--%>

                <h4>Dynamic Workflow Tasks
                    <small id="tasksGroupLabel">Default</small>
                </h4>
            </div>


            <div id="diagram" class="center-block">
                <p>
                    <img id="proc-main-diagram" class="img-responsive img-rounded proc-diagram" src="http://placehold.it/800x200.png"
                         alt="Workflow Process Diagram">
                </p>
            </div>

            <div id="userTasks" class="panel panel-info">
                <div class="panel-heading">
                    <span class="glyphicon glyphicon-thumbs-up pull-right"></span>

                    <h3 class="panel-title">User Tasks</h3>
                </div>
                <div class="panel-body">
                    <div class="btn-group pull-right">
                        <button type="button" class="btn btn-default dropdown-toggle btn-primary" data-toggle="dropdown" aria-expanded="false">
                            Add Task<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="${pageContext.request.contextPath}/document/bookReport/create.htm">Book Report</a></li>
                            <li><a href="${pageContext.request.contextPath}/document/invoice/create.htm">Invoice</a></li>
                        </ul>
                    </div>
                    <div id="userTasks-panel" class="clearfix">

                    </div>
                </div>

                <p class="pull-right">
                    <button id="update-button" type="button" style="margin-top: 20px" class="btn btn-primary">Update
                        Workflow
                    </button>
                </p>
            </div>
        </div>
    </div>

</div>

<jsp:include page="../fragments/footer.jsp"/>

<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>
<%--<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>--%>
<script src="${pageContext.request.contextPath}/resources/js/chosen_v1.3.0/chosen.jquery.js"></script>
<script type="application/javascript">
    APP = {} || APP;
    APP.dynamicTaskTypes = "${dynamicTaskTypesJson}";
</script>
<script src="${pageContext.request.contextPath}/resources/js/app/workflow/main.js"></script>
<script>
    (function ($) {
        $(document).ready(function () {
            $('li#nav-workflows').addClass('active');
        });
    })(jQuery);
</script>

</body>
</html>
