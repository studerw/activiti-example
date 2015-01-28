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
    <title>Activiti Example Debug</title>

</head>

<body>
<jsp:include page="fragments/navbar-top.jsp"/>

<div class="container">

    <div class="start-template">
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

        <div class="jumbotron">
            <h2 style="color: ${appName};">Activiti Example Debug</h2>

            <p>IP address: ${pageContext.request.remoteAddr}</p>

            <p><code>request.getRemoteUser()</code>: ${pageContext.request.remoteUser}</p>
            <p><code>request.getUserPrincipal()</code>: ${pageContext.request.userPrincipal}</p>
            <%--<p><code>((Authentication)httpServletRequest.getUserPrincipal()).getAuthorities()</code>: <br/>--%>
            <%--${authorities}--%>
            <%--</p>--%>
        </div>


        <div class="alert alert-info">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            <h3>Session Variables: ${fn:length(sessionScope)}</h3>
            <hr/>
            <div>
                <c:forEach items="${sessionScope}" var="item">
                    <p>
                    <h4>${item.key}</h4>
                    Value: ${item.value}<br/>
                    </p>
                    <hr/>
                </c:forEach>
            </div>
        </div>

        <div class="alert alert-info">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            <h3>Cookies: ${fn:length(cookie)}</h3>
            <hr/>
            <div>
                <c:forEach var="current" items="${cookie}">
                    <p>
                    <h4>${current.value.name}</h4>
                    Value: ${current.value.value}<br/>
                    </p>
                    <hr/>
                </c:forEach>
            </div>
        </div>

        <div class="alert alert-info">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            <h3>Spring Contexts: ${fn:length(appContexts)}</h3>
            <hr/>
            <div>
                <c:forEach var="entry" items="${appContexts}">
                    <p>
                    <h4>${entry.key}</h4>
                    <ul>
                        <c:forEach var="bean" items="${entry.value}">
                            <li>${bean}</li>
                        </c:forEach>
                    </ul>
                    </p>
                    <hr/>
                </c:forEach>
            </div>
        </div>

        <div class="alert alert-info">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            <h3>Workflow Process Definitions (all versions): ${fn:length(procDefs)}</h3>
            <ul>
                <c:forEach var="procDef" items="${procDefs}">
                    <li>${procDef.id} - ${procDef.key}</li>
                </c:forEach>
            </ul>
            <hr/>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/pages/fragments/footer.jsp"/>
<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>
<script type="text/javascript">
    (function ($) {
        $(document).ready(function () {
            $('li#nav-home').addClass('active');
            $(document).ajaxError(function (event, jqxhr, settings, thrownError) {
                if (jqxhr.status === 419) {
                    bootbox.alert("Caught error code 419 - You've been logged out of the SSO session from another application. Please close the browser and login again.", function () {
                    });
                }
            });
//            $('#ajaxPing').on("click", function (event) {
//                $.ajax({
//                    type: 'GET',
//                    dataType: 'json',
//                    url: SERVLET_CONTEXT + '/dashboard/ajax',
//                    headers: {
//                        Accept: "application/json"
//                    },
//                    success: function (data) {
//                        bootbox.alert(data.message, function () {
//                        });
//                    },
//                    error: function (error) {
//                        bootbox.alert("Error from the original call", function () {
//                        });
//                    }
//                });
//            });
        });
    })(jQuery);

</script>
</body>
</html>
