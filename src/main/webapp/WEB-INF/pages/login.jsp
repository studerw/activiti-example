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
    <title>Sign In</title>
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
    </div>
</div>

<div class="container">
    <div class="start-template" style="width: 500px; margin: 10px auto;">
        <c:if test="${error == true}">
            <div class="errorblock">
                <p>Your login attempt was not successful.</p>

                <p> Cause: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</p>
            </div>
        </c:if>
        <c:if test="${not empty msg}">
            <div class="flash">
                <p><strong>${msg}</strong></p>
            </div>
        </c:if>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Login</h3>
            </div>
            <div class="panel-body">
                <form style="margin: 20px;" class="form-horizontal" role="form" action="j_spring_security_check"
                      method="post">
                    <div class="form-group">
                        <label for="j_username" class="col-sm-2 control-label">User</label>

                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="j_username" name="j_username" required
                                   placeholder="User Name" autofocus>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="j_password" class="col-sm-2 control-label">Password</label>

                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="j_password" name="j_password"
                                   placeholder="Password" required>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <button class="btn btn-primary btn-default pull-right" name="submit" type="submit">Sign in</button>
                </form>
            </div>
        </div>

        <div class="pull-right">
            <button class="btn" data-toggle="modal" data-target="#userModal">
                <span class="glyphicon glyphicon-user"></span> Users
            </button>
        </div>


        <%--<div class="centered" style="margin: 15px 0;">--%>
            <%--<h4><a href="${pageContext.request.contextPath}/userRegistration.htm">New User Registration</a></h4>--%>
        <%--</div>--%>


        </button>
        <!-- Modal -->
        <div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">Current Users</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table table-bordered table-striped">
                            <thead class=" bg-info">
                            <tr>
                                <th>User</th>
                                <th>Groups</th>
                                <%--<th>Permissions</th>--%>
                            </tr>
                            </thead>
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td><strong>${user.key}</strong></td>
                                    <td>${user.value}</td>
                                </tr>
                            </c:forEach>
                        </table>
                        <p>All passwords are, by default, the same as the username (e.g. kermit/kermit)
                        <br/>
                        New Users will have whatever password is set when registering.</p>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
