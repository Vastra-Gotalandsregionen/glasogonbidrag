package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.datamodel.InvoiceLazyDataModel;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "listInvoicesViewBackingBean")
@Scope(value = "view")
public class ListInvoicesViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ListInvoicesViewBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private LiferayUtil liferayUtil;

    @Autowired
    private InvoiceService invoiceService;

    private List<String> filterDataStatuses;

    private InvoiceLazyDataModel lazyDataModel;
    private List<Invoice> invoices;

    public List<String> getFilterDataStatuses() {
        return filterDataStatuses;
    }

    public void setFilterDataStatuses(List<String> filterDataStatuses) {
        this.filterDataStatuses = filterDataStatuses;
    }

    public InvoiceLazyDataModel getLazyDataModel() {
        return lazyDataModel;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public String getUserNameById(long userId) {
        return liferayUtil.getUserNameById(userId);
    }

    @PostConstruct
    protected void init() {
        fetchInvoices();
        initFilterData();
    }

    private void initFilterData() {
        // TODO: refactor this nicer when more work with filters has been done
        filterDataStatuses = new ArrayList<String>();
        filterDataStatuses.add("invoice-status-in-progress");
        filterDataStatuses.add("invoice-status-completed");
        filterDataStatuses.add("invoice-status-canceled");
        filterDataStatuses.add("invoice-status-replaced");

    }

    private void fetchInvoices() {
        // TODO: Do we use the invoices field? Have the lazy data model replaced this everywhere?
        invoices = invoiceService.findAllWithParts();
        lazyDataModel = new InvoiceLazyDataModel(invoiceService);
    }

}
