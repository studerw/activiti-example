package com.studerw.activiti.task;

import com.studerw.activiti.model.Response;
import com.studerw.activiti.model.task.TaskApprovalForm;
import com.studerw.activiti.model.task.CandidateTask;
import com.studerw.activiti.model.task.TaskCollaborationForm;
import com.studerw.activiti.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author William Studer
 * Date: 5/18/14
 */
@Controller
public class TaskController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);

    @Autowired protected LocalTaskService taskService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/tasks.htm", method = RequestMethod.GET)
    public String index(ModelMap model, HttpServletRequest request) {
        List<CandidateTask> candidateTasks = this.taskService.findCandidateTasks(currentUserName());
        model.addAttribute("candidateTasks", candidateTasks);
        return "tasks";
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<CandidateTask>>> getTasks(HttpServletRequest request) {
        String userName = currentUserName();
        LOG.debug("TaskController: tasks for user: " + userName);
        List<CandidateTask> tasks = taskService.findCandidateTasks(userName);
        LOG.debug("returning json response of: " + tasks.size() + " for user : " + userName);
        Response res = new Response(true, "tasks for user: " + userName, tasks);
        return new ResponseEntity<Response<List<CandidateTask>>>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/tasks/approve", method = RequestMethod.POST)
    public String approve(@Valid @ModelAttribute TaskApprovalForm approvalForm,
                          BindingResult result,
                          final RedirectAttributes redirectAttributes){
        LOG.debug("task approval: {}", approvalForm.toString());

        if (result.hasFieldErrors()) {
            return "tasks";
        }

        this.taskService.approveOrRejectDoc(approvalForm);

        redirectAttributes.addFlashAttribute("msg", "The task has been completed.");
        return "redirect:/tasks.htm";
    }

    @RequestMapping(value = "/tasks/collaborate", method = RequestMethod.POST)
    public String approve(@Valid @ModelAttribute TaskCollaborationForm collaborationForm,
                          BindingResult result,
                          final RedirectAttributes redirectAttributes){
        LOG.debug("task collaboration: {}", collaborationForm.toString());

        if (result.hasFieldErrors()) {
            return "tasks";
        }

        this.taskService.collaborateTask(collaborationForm.getTaskId(), collaborationForm.getComment());
        redirectAttributes.addFlashAttribute("msg", "The task has been completed.");
        return "redirect:/tasks.htm";
    }



}
