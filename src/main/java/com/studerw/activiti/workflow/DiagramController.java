package com.studerw.activiti.workflow;

import com.studerw.activiti.model.Response;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.workflow.DynamicUserTask;
import com.studerw.activiti.web.BaseController;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/workflow/diagram")
public class DiagramController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(DiagramController.class);

    @Autowired protected RepositoryService repositoryService;
    @Autowired protected WorkflowService workflowSrvc;
    @Autowired protected WorkflowBuilder workflowBldr;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/processDefs/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDiagramByProcDefId(
            @PathVariable("id") String id) throws IOException {

        LOG.debug("fetching diagram for process {}", id);

        byte[] bytes = workflowSrvc.getProcessDefinitionDiagram(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "image/png");
        return new ResponseEntity<byte[]>(bytes, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/dynamicTasks", method = RequestMethod.POST)
    public ResponseEntity<byte[]> getBaseDiagramByTasks(
            @RequestBody List<DynamicUserTask> dynamicUserTasks) {

        LOG.debug("fetching diagram for tasks: {}", dynamicUserTasks);

        BpmnModel model = workflowBldr.buildModel(dynamicUserTasks, DocType.GENERAL, String.valueOf(new Random().nextInt()));
        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally{
            IOUtils.closeQuietly(in);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", MediaType.IMAGE_PNG_VALUE);
        return new ResponseEntity<byte[]>(bytes, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/dynamicTasks", method = RequestMethod.POST, params = "base64", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Response<String>> getBaseDiagramBase64ByTasks(
            @RequestBody List<DynamicUserTask> dynamicUserTasks,
            @RequestParam(value = "base64") boolean base64) {

        LOG.debug("fetching base64 encoded diagram for tasks: {}", dynamicUserTasks);

        BpmnModel model = workflowBldr.buildModel(dynamicUserTasks, DocType.GENERAL, String.valueOf(new Random().nextInt()));
        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally{
            IOUtils.closeQuietly(in);
        }
        bytes = Base64.encode(bytes);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", MediaType.TEXT_PLAIN_VALUE);
        Response<String> response = new Response<String>(true, "", new String(bytes, Charset.forName("UTF-8")));
        return new ResponseEntity<Response<String>>(response, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{docType}/{group}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDiagramByDocTypeAndGroup(
            @PathVariable(value = "docType") DocType docType,
            @PathVariable(value = "group") String group) {

        LOG.debug("finding diagram for docType = {} and group = {}", docType, group);
        ProcessDefinition procDefinition = this.workflowSrvc.findProcDef(docType, group);
        BpmnModel model = this.repositoryService.getBpmnModel(procDefinition.getId());
        new BpmnAutoLayout(model).execute();
        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "image/png");
        return new ResponseEntity<byte[]>(bytes, responseHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "/documents/{docId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getActiveDocDiagram(
            @PathVariable("docId") String docId) throws IOException {
        LOG.debug("fetching diagram for docId{}", docId);

        byte[] bytes = workflowSrvc.getActiveDocumentDiagram(docId);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "image/png");
        return new ResponseEntity<byte[]>(bytes, responseHeaders, HttpStatus.OK);

    }
}
