package com.studerw.activiti.model.document;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;

/**
* @author William Studer
*/
public class DocStateEditor extends PropertyEditorSupport {

    private static final Logger log = LoggerFactory.getLogger(DocStateEditor.class);

    @Override
    public String getAsText (){
        DocState docState = (DocState) this.getValue();
        if (docState == null){
            return null;
        }
        log.trace("Converted docState: " + docState.name() + "  to string: " + docState.toString());
        return docState.toString();
    }

    @Override
    public void setAsText (String s){
        if (StringUtils.isBlank(s)){
            setValue(null);
            return;
        }
        DocState docState = DocState.valueOf(s);
        log.trace("Converted DocState: {} ---> {}", s, docState.name());
        setValue(docState);
    }

}
