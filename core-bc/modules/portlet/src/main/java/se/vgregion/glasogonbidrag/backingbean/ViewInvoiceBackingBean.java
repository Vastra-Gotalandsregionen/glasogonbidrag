package se.vgregion.glasogonbidrag.backingbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.data.InvoiceRepository;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

@Component(value = "viewInvoiceBean")
@Scope(value = "request")
public class ViewInvoiceBackingBean {

    @Autowired
    private InvoiceRepository invoiceRepository;

    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    @PostConstruct
    protected void init() {
        System.out.println("ViewInvoiceBackingBean - init");

        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();

        Map<String, String> parameterMap =
                (Map<String, String>)externalContext.getRequestParameterMap();
        Long id = Long.parseLong(parameterMap.get("invoiceId"));

        System.out.println("ViewInvoiceBackingBean - find invoice: " + id);

        invoice = invoiceRepository.findWithParts(id);
    }
}
