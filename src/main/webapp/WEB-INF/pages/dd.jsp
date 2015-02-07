<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="fragments/head.jsp"/>
    <%--<link href="${pageContext.request.contextPath}/resources/js/chosen_v1.3.0/chosen.css" rel="stylesheet">--%>

    <title>Drag and Drop</title>

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
<jsp:include page="fragments/navbar-top.jsp"/>

<div class="container">

    <div class="start-template">HERE</div>

</div>

<jsp:include page="fragments/footer.jsp"/>

<script src="${pageContext.request.contextPath}/resources/js/jquery-ui-1.11.2/jquery-ui.js"></script>
<script>
    (function ($) {
        $(document).ready(function () {
            $('a.navbar-brand').addClass('navbar-brand-active');
        });
    })(jQuery);
</script>

</body>
</html>
