package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.domain.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.domain.api.data.SupplierRepository;
import se.vgregion.service.glasogonbidrag.domain.api.service.SupplierService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private SupplierRepository supplierRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private SupplierService supplierService;

    private Supplier selectedSupplier;

    private List<Invoice> selectedSupplierInvoices;

    private List<Supplier> suppliers;

    private boolean viewEditSupplier;

    private Supplier newSupplier;

    public Locale locale;

    public Supplier getSelectedSupplier() {
        return selectedSupplier;
    }

    public List<Invoice> getSelectedSupplierInvoices() {
        return selectedSupplierInvoices;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public boolean getViewEditSupplier() {
        return viewEditSupplier;
    }

    public Supplier getNewSupplier() {
        return newSupplier;
    }

    public Locale getLocale() {
        return locale;
    }

    public void onRowSelect(SelectEvent event) {
        selectedSupplier = (Supplier) event.getObject();
        selectedSupplierInvoices = invoiceRepository.findAllBySupplier(selectedSupplier);
    }

    public void onRowUnselect(UnselectEvent event) {
        selectedSupplier = null;
        selectedSupplierInvoices = new <Invoice>ArrayList();
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
        supplierService.update(selectedSupplier);
        viewEditSupplier = false;
    }


    public String getUserNameById(long userId) {
        return liferayUtil.getUserNameById(userId);
    }

    @PostConstruct
    protected void init() {
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();

        suppliers = supplierRepository.findAllWithInvoices();
        selectedSupplier = null;
        selectedSupplierInvoices = new <Invoice>ArrayList();
        newSupplier = new Supplier();
        viewEditSupplier = facesUtil.fetchBooleanProperty("viewEditSupplier");

        locale = themeDisplay.getLocale();
        // Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");
    }
}
