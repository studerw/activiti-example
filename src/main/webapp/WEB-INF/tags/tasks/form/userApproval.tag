<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="candidateTask" required="true" type="com.studerw.activiti.model.task.CandidateTask" %>

<div id="collapseOne-${candidateTask.id}" class="panel-collapse collapse">
    <div class="panel-body">
        <form class="form-horizontal" role="form" method="post"
              action="${pageContext.request.contextPath}/userRegistration">
            <div class="form-group">
                <label class="col-sm-2 control-label">Description</label>
                <div class="col-sm-10">
                    <p class="form-control-static">${candidateTask.name}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Created</label>
                <div class="col-sm-10">
                    <p class="form-control-static">${candidateTask.createTime}</p>
                </div>
            </div>
            <hr/>
            <c:set var="userReg" value="${candidateTask.processVariables['userForm']}"/>
            <div class="form-group">
                <label class="col-sm-2 control-label">New User ID</label>
                <div class="col-sm-10">
                    <p class="form-control-static"> ${userReg.userName}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">Group</label>
                <div class="col-sm-10">
                    <p class="form-control-static"> ${userReg.group}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">New User Name</label>
                <div class="col-sm-10">
                    <p class="form-control-static">${userReg.firstName} ${userReg.lastName}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">New User Email</label>
                <div class="col-sm-10">
                    <p class="form-control-static"> ${userReg.email}</p>
                </div>
            </div>
            <hr/>
            <div class="form-group">
                <label for="comment-${candidateTask.id}" class="col-sm-2 control-label">Comment</label>
                <div class="col-sm-10">
                    <textarea class="form-control" rows="3" id="comment-${candidateTask.id}" name="comment" required></textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <div class="radio">
                        <label>
                            <input type="radio" name="approved"
                                   id="optionsRadios1-${candidateTask.id}"
                                   value="true" checked>Approve
                        </label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <div class="radio">
                        <label>
                            <input type="radio" name="approved"
                                   id="optionsRadios2-${candidateTask.id}"
                                   value="false">Reject
                        </label>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-primary">Complete Task</button>
                </div>
            </div>
            <input type="hidden" name="taskId" value="${candidateTask.id}"/>
        </form>
    </div>
</div>