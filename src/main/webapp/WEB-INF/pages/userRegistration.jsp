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
    <title>New User Registration</title>
    <jsp:include page="fragments/head.jsp"/>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand brand" href="#">Activiti Example</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container">
    <div class="start-template">

        <c:if test="${error == true}">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="errorblock">
                        <c:if test="${not empty errors}">
                            <div class="errorBox">
                                <c:forEach var="objError" items="${errors}">
                                    ${objError.field} - ${objError.defaultMessage}<br>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">New User Registration</h3>
            </div>
            <div class="panel-body">
                <form style="margin: 20px" class="form-horizontal" role="form" id="registerHere" method="post"
                      action="${pageContext.request.contextPath}/userRegistration.htm">
                    <div class="form-group">
                        <label for="userName" class="col-sm-2 control-label">User Name</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="userName" id="userName" required
                                   autofocus>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="group" class="col-sm-2 control-label">Group</label>

                        <div class="col-sm-10">
                            <select class="form-control" name="group" id="group" required>
                                <c:forEach var="group" items="${groups}">
                                    <option value="${group.id}">${group.name}</option>
                                </c:forEach>
                            </select>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="firstName" class="col-sm-2 control-label">First Name</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="firstName" name="firstName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastName" class="col-sm-2 control-label">Last Name</label>

                        <div class="col-sm-10">
                            <input type="test" class="form-control" id="lastName" name="lastName" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">Email</label>

                        <div class="col-sm-10">
                            <input type="email" class="form-control" id="email" name="email" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">Password</label>

                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="password" id="password"
                                   placeholder="Password" required>
                        </div>
                    </div>
                    <div class="pull-right">
                        <button type="submit" class="btn btn-primary">Submit for Approval</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
