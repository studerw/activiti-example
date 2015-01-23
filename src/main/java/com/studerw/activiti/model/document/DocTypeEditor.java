package com.studerw.activiti.model.document;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;

/**
* @author William Studer
*/
public class DocTypeEditor extends PropertyEditorSupport {

    private static final Logger log = LoggerFactory.getLogger(DocTypeEditor.class);

    @Override
    public String getAsText() {
        DocType DocType = (DocType) this.getValue();
        if (DocType == null) {
            return null;
        }
        log.trace("Converted DocType: " + DocType.name() + "  to string: " + DocType.toString());
        return DocType.toString();
    }

    @Override
    public void setAsText(String s) {
        if (StringUtils.isBlank(s)) {
            setValue(null);
            return;
        }
        DocType docType = DocType.valueOf(s);
        log.trace("Converted DocType: {} ---> {}", s, docType.name());
        setValue(docType);
    }

}
