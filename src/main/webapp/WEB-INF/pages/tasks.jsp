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

            <h3>Tasks
                <small>${pageContext.request.remoteUser}</small>
            </h3>
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

    <t:candidate candidateTasks="${candidateTasks}" />

</div>


<jsp:include page="fragments/footer.jsp"/>

<%--<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>--%>
<%--<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>--%>
<%--<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>--%>
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
