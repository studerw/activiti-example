<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript">
    var SERVLET_CONTEXT = '${pageContext.request.contextPath}';
</script>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar">hello</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand brand" href="#">Activiti Example</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li id="nav-home"><a href="${pageContext.request.contextPath}/index.htm">Home</a></li>
                <li id="nav-docs"><a href="${pageContext.request.contextPath}/document/list.htm">Documents</a></li>
                <li id="nav-tasks"><a href="${pageContext.request.contextPath}/tasks.htm">Tasks</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown" id="nav-alerts">
                    <a class="dropdown-toggle" data-toggle="dropdown"
                       href="${pageContext.request.contextPath}/alerts.htm">Alerts
                        <span id="alertCount" class="badge">${fn:length(alerts)}</span><b class="caret"></b></a>
                    <ul id="alertMenuGroup" class="dropdown-menu list-group alert-dropdown">
                        <c:choose>
                            <c:when test="${empty alerts}">
                                <li class="list-group-item">
                                    <div class="alert alert-info alert-dropdown-div">You have no alerts</div>
                                </li>
                            </c:when>
                            <c:when test="${not empty alerts}">
                                <c:forEach var="alert" items="${alerts}">
                                    <c:choose>
                                        <c:when test="${alert.priority == 1}">
                                            <c:set var="alertClass" value="alert-success"/>
                                        </c:when>
                                        <c:when test="${alert.priority == 2}">
                                            <c:set var="alertClass" value="alert-info"/>
                                        </c:when>
                                        <c:when test="${alert.priority == 3}">
                                            <c:set var="alertClass" value="alert-warning"/>
                                        </c:when>
                                        <c:when test="${alert.priority == 4}">
                                            <c:set var="alertClass" value="alert-danger"/>
                                        </c:when>
                                    </c:choose>
                                    <li class="alert list-group-item alert-dismissable" data-id="${alert.id}">
                                        <div class="alert-dropdown-div ${alertClass}">
                                            <button type="button" class="close" style="margin: 20px;" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <p class="list-group-item-text alert-date">Created: <strong><spring:eval expression="alert.createdDate"/></strong></p><br/>
                                            <p class="list-group-item-text">From: <strong>${alert.createdBy}</strong></p><br/>
                                            <p class="list-group-item-text">${alert.message}</p>
                                            <br/>
                                        </div>
                                    </li>
                                </c:forEach>
                            </c:when>
                        </c:choose>

                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">${userName} <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/j_spring_security_logout">Log Out</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
