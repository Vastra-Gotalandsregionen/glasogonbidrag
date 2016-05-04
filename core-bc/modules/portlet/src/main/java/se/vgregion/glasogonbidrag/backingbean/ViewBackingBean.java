package se.vgregion.glasogonbidrag.backingbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.data.SupplierRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Component(value = "viewBean")
@Scope(value = "request")
public class ViewBackingBean {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    private List<Supplier> suppliers;
    private List<Invoice> invoices;

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    @PostConstruct
    protected void init() {
        System.out.println("ViewBackingBean - init");

        suppliers = supplierRepository.findAll();
        invoices = invoiceRepository.findAll();
    }
}
