package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.datamodel.SupplierInvoiceLazyDataModel;
import se.vgregion.glasogonbidrag.datamodel.SupplierLazyDataModel;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.portal.glasogonbidrag.domain.dto.SupplierDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.domain.api.service.LowLevelDatabaseQueryService;
import se.vgregion.service.glasogonbidrag.domain.api.service.SupplierService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "manageSuppliersViewBackingBean")
@Scope(value = "view")
public class ManageSuppliersViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ManageSuppliersViewBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private LiferayUtil liferayUtil;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private LowLevelDatabaseQueryService queryService;

    private SupplierLazyDataModel lazyDataModel;
    private SupplierInvoiceLazyDataModel invoiceLazyDataModel;

    private List<String> filterActiveStatues;
    private List<String> filterDataStatuses;

    private Supplier selectedSupplier;

    private List<Invoice> selectedSupplierInvoices;

    private boolean viewEditSupplier;

    private Supplier newSupplier;

    public ManageSuppliersViewBackingBean() {
        filterActiveStatues = new ArrayList<>();
        filterActiveStatues.add("yes");
        filterActiveStatues.add("no");

        filterDataStatuses = new ArrayList<String>();
        filterDataStatuses.add("invoice-status-in-progress");
        filterDataStatuses.add("invoice-status-completed");
        filterDataStatuses.add("invoice-status-canceled");
        filterDataStatuses.add("invoice-status-replaced");
    }

    public Supplier getSelectedSupplier() {
        return selectedSupplier;
    }

    public List<Invoice> getSelectedSupplierInvoices() {
        return selectedSupplierInvoices;
    }

    public LazyDataModel<SupplierDTO> getSuppliers() {
        return lazyDataModel;
    }

    public SupplierInvoiceLazyDataModel getInvoiceLazyDataModel() {
        return invoiceLazyDataModel;
    }

    public boolean getViewEditSupplier() {
        return viewEditSupplier;
    }

    public Supplier getNewSupplier() {
        return newSupplier;
    }

    public void onRowSelect(SelectEvent event) {
        selectedSupplier = ((SupplierDTO) event.getObject()).getSupplier();
        invoiceLazyDataModel = new SupplierInvoiceLazyDataModel(
                selectedSupplier.getId(), invoiceService, queryService);

//        selectedSupplierInvoices = invoiceService.findAllBySupplier(selectedSupplier);
    }

    public void onRowDeselect(UnselectEvent event) {
        selectedSupplier = null;
        selectedSupplierInvoices = new ArrayList<>();

        invoiceLazyDataModel = null;
    }

    public void addNewSupplierListener() {
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long userId = themeDisplay.getUserId();

        newSupplier.setUserId(themeDisplay.getUserId());
        newSupplier.setCompanyId(themeDisplay.getCompanyId());
        newSupplier.setCreateDate(new Date());

        supplierService.create(newSupplier);

        newSupplier = new Supplier();
    }


    public void changeViewEditSupplierListener(boolean viewEditSupplier) {
        System.out.println("changeViewEditSupplierListener viewEditSupplier is: " + viewEditSupplier);
        this.viewEditSupplier = viewEditSupplier;
    }

    public void updateSupplierListener() {
        selectedSupplier = supplierService.update(selectedSupplier);
        viewEditSupplier = false;
    }


    public String getUserNameById(long userId) {
        return liferayUtil.getUserNameById(userId);
    }

    public List<String> getFilterActiveStatues() {
        return filterActiveStatues;
    }

    public List<String> getFilterDataStatuses() {
        return filterDataStatuses;
    }

    @PostConstruct
    protected void init() {
        selectedSupplier = null;
        selectedSupplierInvoices = new ArrayList<>();
        newSupplier = new Supplier();
        viewEditSupplier = facesUtil.fetchBooleanProperty("viewEditSupplier");

        lazyDataModel = new SupplierLazyDataModel(
                supplierService, queryService);
        invoiceLazyDataModel = null;
    }
}
