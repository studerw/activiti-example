<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<%@ attribute name="candidateTask" required="true" type="com.studerw.activiti.model.task.CandidateTask"%>

<div id="collapseOne-${candidateTask.id}" class="panel-collapse collapse">
    <div class="panel-body">
        <h3>Unknown task type: ${candidateTask.taskDefinitionKey}</h3>
    </div>
</div>
