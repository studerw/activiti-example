package com.studerw.activiti.workflow;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.Approval;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * User: studerw
 * Date: 5/30/14
 */
@Component
@Lazy(value = false)
public class GroupApprovalWorkflowInitiator {
    private static final Logger log = LoggerFactory.getLogger(GroupApprovalWorkflowInitiator.class);
    @Autowired com.studerw.activiti.user.UserService userSrvc;
    @Autowired WorkflowBuilder workflowBldr;
    @Autowired RepositoryService repoSrvc;
    @Autowired WorkflowService workflowSrvc;

    @PostConstruct
    public void postConstruct(){
        List<Group> groups = userSrvc.getAllAssignmentGroups();
        for(Group group: groups){
            if (!workflowSrvc.groupDocApproveWorkflowExists(group.getId())){
                log.info("Creating default doc approval workflow deployment for group {}", group.getId());
                Approval approval = new Approval();
                approval.getCandidateGroups().add(group.getId());

                BpmnModel model = workflowBldr.documentApprove(Lists.newArrayList(approval), group.getId());
                workflowSrvc.updateGroupDocApproveWorkflow(model, group.getId());

            }
        }
    }


}
