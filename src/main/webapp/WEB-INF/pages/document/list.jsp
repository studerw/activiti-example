f<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../fragments/head.jsp"/>
    <title>Documents</title>
</head>

<body>
<jsp:include page="../fragments/navbar-top.jsp"/>

<div class="container">

    <div class="start-template">
        <div class="page-header">
            <span class="glyphicon glyphicon-paperclip pull-right"></span>
            <h2>Documents <small>${userName}</small></h2>
        </div>

        <div class="centered">
            <c:if test="${not empty msg}">
                <div class="flash">
                    <p><strong>${msg}</strong></p>
                </div>
            </c:if>
        </div>


        <c:choose>
            <c:when test="${empty documents}">
                <p>The user does not have any group documents</p>
            </c:when>
            <c:otherwise>
                <table class="table table-striped">
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Group</th>
                        <th>Created Date</th>
                        <th>State</th>
                    </tr>

                    <c:forEach items="${documents}" var="doc">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/document/view.htm?id=${doc.id}">${doc.title}</a></td>
                            <td>${doc.author}</td>
                            <td>${doc.groupId}</td>
                            <td>${doc.createdDate}</td>
                            <td>${doc.state}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
        <hr/>
        <div class="pull-right">
            <a href="${pageContext.request.contextPath}/document/add.htm" class="btn btn-primary" role="button">
                Create New Document
            </a>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/pages/fragments/footer.jsp"/>
<script>
    (function($){
        $(document).ready(function () {
            $('li#nav-docs').addClass('active');
        });
    })(jQuery);
</script>

</body>
</html>
