<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="fragments/head.jsp"/>
    <title>Alerts</title>
    <style type="text/css">
        .panel-task {
            margin: 15px 0;
        }
    </style>
</head>

<body>
<jsp:include page="fragments/navbar-top.jsp"/>

<div class="container">

    <div class="start-template">
        <div class="page-header">
            <span class="glyphicon glyphicon-envelope pull-right"></span>

            <h2>Alerts
                <small>${userName}</small>
            </h2>
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


    <c:choose>
        <c:when test="${not empty alerts}">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <span class="glyphicon glyphicon-envelope pull-right"></span>
                    <h4 class="panel-title">
                        Alerts
                    </h4>
                </div>
                <div class="panel-body">
                    <c:forEach var="alert" items="${alerts}">

                    </c:forEach>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <p>You do not have any new alerts.</p>
        </c:otherwise>
    </c:choose>
</div>


<jsp:include page="fragments/footer.jsp"/>

<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>
<%--<script src="${pageContext.request.contextPath}/resources/js/app/tasks/main.js"></script>--%>
<script>
    (function ($) {
        $(document).ready(function () {
            $('li#nav-alerts').addClass('active');
        });
    })(jQuery);
</script>

</body>
</html>
