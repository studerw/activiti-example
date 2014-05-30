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
                        <option <c:if test="${group.id eq param.group}"> selected </c:if> value="${group.id}">${group.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <hr/>

        <c:choose>
            <c:when test="${isDefault}">
                <h4>Default Document Approval Workflow</h4>
            </c:when>
            <c:otherwise>
                <h4>${param.group} Document Approval Workflow</h4>
            </c:otherwise>
        </c:choose>

        <div id="diagram" class="center-block">
            <p>
                <img class="img-responsive img-rounded proc-diagram" src="${pageContext.request.contextPath}/workflow/diagrams/${defaultDocProcId}"
                     alt="Workflow Process Diagram">
            </p>
        </div>

        <div id="approvals" class="panel panel-info">
            <div class="panel-heading">
                <span class="glyphicon glyphicon-thumbs-up pull-right"></span>

                <h3 class="panel-title">Approvals</h3>
            </div>
            <div class="panel-body">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <td>Position</td>
                        <td>Candidate Groups</td>
                        <td>Candidate Users</td>
                        <td>Edit</td>
                        <td>Delete</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="approval" items="${approvals}">
                        <tr>
                            <td>${approval.position}</td>
                            <td>${approval.candidateGroups}</td>
                            <td>${approval.candidateUsers}</td>
                            <td>
                                <c:if test="${not empty param.group}">
                                    <button type="button" class="btn btn-default">
                                        <span class="glyphicon glyphicon-edit"></span>
                                    </button>
                                </c:if>
                            </td>

                            <td>
                                <c:if test="${not empty param.group}">
                                    <button type="button" class="btn btn-default">
                                        <span class="glyphicon glyphicon-trash"></span>
                                    </button>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>

</div>

</div>


<jsp:include page="../fragments/footer.jsp"/>

<%--<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>--%>
<%--<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>--%>
<%--<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>--%>
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
