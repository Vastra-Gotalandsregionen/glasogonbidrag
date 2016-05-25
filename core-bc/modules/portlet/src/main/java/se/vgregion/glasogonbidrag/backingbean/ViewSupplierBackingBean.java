package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.api.data.SupplierRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller("viewSupplierBean")
@Scope(value = "request")
public class ViewSupplierBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ViewSupplierBackingBean.class);

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private FacesUtil util;

    private Supplier supplier;
    private List<Invoice> invoices;

    public Supplier getSupplier() {
        return supplier;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    @PostConstruct
    protected void init() {
        LOGGER.info("ViewSupplierBackingBean - init()");

        String name = util.fetchProperty("supplierName");

        supplier = repository.find(name);
        invoices = invoiceRepository.findAllBySupplier(supplier);
    }
}
