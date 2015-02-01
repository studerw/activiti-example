package com.studerw.activiti.workflow;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.workflow.DynamicUserTask;
import com.studerw.activiti.model.workflow.DynamicUserTaskType;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Used to parse process definitions.
 *
 * @author William Studer
 *         Date: 5/29/14
 */
@Service("workflowParser")
public class WorkflowParser {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowParser.class);

    @Autowired protected RepositoryService repoSrvc;
    @Autowired protected WorkflowService workflowService;

    /**
     *
     * @param processId the definition of the process Id
     * @return a list of {@code DynamicUserTask tasks} contained in the dynamic subprocess
     * of the given process definition. If a definition cannot be found, an unchecked IllegalArgumentException
     * is thrown.
     */
    public List<DynamicUserTask> getTasksFromProcDef(String processId) {
        Assert.hasText(processId);
        //ProcessDefinition procDef = this.repoSrvc.createProcessDefinitionQuery().
        return null;
    }
}
