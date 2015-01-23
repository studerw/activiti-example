package com.studerw.activiti.model.document;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author William Studer
 */
public class Invoice extends Document {

    @NotEmpty
    protected String payee;
    @NotNull
    protected BigDecimal amount;

    public Invoice() {
        super();
        this.docType = DocType.INVOICE;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayee() {

        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }
}
