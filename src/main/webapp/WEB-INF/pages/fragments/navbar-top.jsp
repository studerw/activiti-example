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

                <%--<li class="dropdown">--%>
                    <%--<a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>--%>
                    <%--<ul class="dropdown-menu">--%>
                        <%--<li><a href="#">Action</a></li>--%>
                        <%--<li><a href="#">Another action</a></li>--%>
                        <%--<li><a href="#">Something else here</a></li>--%>
                        <%--<li class="divider"></li>--%>
                        <%--<li class="dropdown-header">Nav header</li>--%>
                        <%--<li><a href="#">Separated link</a></li>--%>
                        <%--<li><a href="#">One more separated link</a></li>--%>
                    <%--</ul>--%>
                <%--</li>--%>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#" class="">Alerts <span class="badge">0</span></a></li>
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
