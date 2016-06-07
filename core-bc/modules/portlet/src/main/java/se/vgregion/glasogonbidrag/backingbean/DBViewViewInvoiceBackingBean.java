package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.annotation.PostConstruct;

@Component(value = "viewInvoiceBean")
@Scope(value = "request")
public class DBViewViewInvoiceBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DBViewViewInvoiceBackingBean.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private FacesUtil util;

    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    @PostConstruct
    protected void init() {
        LOGGER.info("DBViewViewInvoiceBackingBean - init()");

        Long id = util.fetchId("invoiceId");

        invoice = invoiceRepository.findWithParts(id);
    }
}
