package com.studerw.activiti;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.studerw.activiti.document.DocumentService;
import com.studerw.activiti.model.Document;
import com.studerw.activiti.model.TaskForm;
import com.studerw.activiti.task.LocalTaskService;
import com.studerw.activiti.user.UserService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: studerw
 * Date: 5/21/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class DocumentApproveSubProcessTest {
    private static final Logger log = Logger.getLogger(ActivitiSpringTest.class);
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    @Autowired
    IdentityService identityService;
    @Autowired
    UserService userService;
    @Autowired
    DocumentService documentService;
    @Autowired
    LocalTaskService localTaskService;
    @Autowired
    RepositoryService repoSrvc;

    @Test
    public void testDocApprovalFlow() throws InterruptedException, IOException {
        setSpringSecurity("kermit");
        Document doc = new Document();
        doc.setGroupId("engineering");
        doc.setCreatedDate(new Date());
        doc.setTitle("title");
        doc.setSummary("Summary");
        doc.setContent("content");
        doc.setAuthor("kermit");
        String docId;
        docId = documentService.createDocument(doc);
        log.debug("new doc id: " + docId);
        this.documentService.submitForApproval(docId);



        setSpringSecurity("fozzie");
        List<TaskForm> tasks = this.localTaskService.getTasks("fozzie");
        assertTrue(tasks.size() == 1);
        TaskForm currentTask = tasks.get(0);
        log.debug("got task: " + tasks.get(0).getName());

        //http://forums.activiti.org/content/process-diagram-highlighting-current-process
        RepositoryServiceImpl impl = (RepositoryServiceImpl)repoSrvc;
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(currentTask.getProcessDefinitionId());
        BpmnModel bpmnModel = repoSrvc.getBpmnModel(pde.getId());
        InputStream in = ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", runtimeService.getActiveActivityIds(currentTask.getProcessInstanceId()));
        FileUtils.copyInputStreamToFile(in, new File("target/current-diagram.png"));

        localTaskService.approveTask(true, "task approved blah blah blah", tasks.get(0).getId());
        HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().
                includeProcessVariables().processInstanceBusinessKey(docId).singleResult();

        assertNotNull(pi);
        log.debug("Duration time in millis: " + pi.getDurationInMillis());
        List<HistoricTaskInstance> hTasks;
        hTasks = historyService.createHistoricTaskInstanceQuery().includeTaskLocalVariables().processInstanceBusinessKey(docId).list();
        assertTrue(hTasks.size() == 2);
        HistoricTaskInstance approval = hTasks.get(1);
        Map<String, Object> vars = approval.getTaskLocalVariables();
        List<Comment> comments = taskService.getTaskComments(approval.getId());
    }

    private void setSpringSecurity(String userName){
        List<Group> groups = this.identityService.createGroupQuery().groupMember(userName).groupType("security-role").list();
        List<String> groupStr = Lists.newArrayList();
        for (Group g : groups) {
            groupStr.add(g.getId());
        }
        Collection<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(Joiner.on(",").skipNulls().join(groupStr));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(userName, "pw", true, true, true, true, auths);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
