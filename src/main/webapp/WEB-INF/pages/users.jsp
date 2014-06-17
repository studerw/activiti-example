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
    <title>Activiti Users</title>

</head>

<body>
<jsp:include page="fragments/navbar-top.jsp"/>

<script id="userTemplate" type="text/template">
    <ul>
        <li><@= userName @></li>
        <li><@= email @></li>
        <li><@= firstName @></li>
        <li><@= lastName @></li>
    </ul>
    <button class="delete">Delete</button>
</script>


<div class="container">

    <div class="start-template">
        <div class="page-header">
            <span class="glyphicon glyphicon-user pull-right"></span>
            <h3>User Management</h3>
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

        <div id="userSelForm">
            <div class="form-group">
                <label for="userSel" class="">User</label>
                <select class="form-control" id="userSel">
                    <option value="">Please Choose a User</option>


                </select>
            </div>
        </div>

        <div id="userList">

        </div>
    </div>

</div>
<jsp:include page="/WEB-INF/pages/fragments/footer.jsp"/>
<script>
    (function ($) {
        $(document).ready(function () {
            $('li#nav-users').addClass('active');
            $( document ).ajaxError(function( event, jqxhr, settings, exception ) {
                    alert('here');
                    console.dir(arguments);
            });
        });
    })(jQuery);
</script>
<script src="${pageContext.request.contextPath}/resources/js/json2.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/underscore.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/backbone.js"></script>
<script type="text/javascript">
    //Need to change backbone templates to avoid problems with JSP syntax
    _.templateSettings = {
        interpolate: /\<\@\=(.+?)\@\>/gim,
        evaluate: /\<\@([\s\S]+?)\@\>/gim,
        escape: /\<\@\-(.+?)\@\>/gim
    };
</script>
<script src="${pageContext.request.contextPath}/resources/js/app/users/main.js"></script>

</body>
</html>
