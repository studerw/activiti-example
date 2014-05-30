<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../fragments/head.jsp"/>
    <title>Workflow</title>
    <style type="text/css">
        .panel-task {
            margin: 15px 0;
        }
        div#diagram{
            margin: 20px auto;
        }
    </style>
    <script type="text/javascript">
        var DOC_APPROVAL_ROOT_ID = '${defaultDocProcId}';
    </script>
</head>
<body>
<jsp:include page="../fragments/navbar-top.jsp"/>

<div class="container">

    <div class="start-template">
        <div class="page-header">
            <span class="glyphicon glyphicon-cog pull-right"></span>
            <h2>Workflows</h2>
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
        <div id="groupSelForm">
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

        <div class="page-header">
            <span class="glyphicon glyphicon-paperclip pull-right"></span>
            <h3>Document Approval Workflow
            <small id="groupTitle">Default</small></h3>
        </div>


        <div id="diagram" class="center-block">
            <p>
                <img id="proc-main-diagram" class="img-responsive img-rounded proc-diagram" src="${pageContext.request.contextPath}/workflow/diagrams/${defaultDocProcId}"
                     alt="Workflow Process Diagram">
            </p>
        </div>

        <div id="approvals" class="panel panel-info hidden">
            <div class="panel-heading">
                <span class="glyphicon glyphicon-thumbs-up pull-right"></span>

                <h3 class="panel-title">Approvals</h3>
            </div>
            <div class="panel-body" id="approvals-panel">

            </div>
            <p class="pull-right">
                <button id="update-button" type="button" style="margin-top: 20px" class="btn btn-primary">Update Workflow</button>
            </p>
        </div>

    </div>

</div>

</div>


<jsp:include page="../fragments/footer.jsp"/>

<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>
<%--<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>--%>
<script src="${pageContext.request.contextPath}/resources/js/chosen_v1.1.0/chosen.jquery.js"></script>
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
