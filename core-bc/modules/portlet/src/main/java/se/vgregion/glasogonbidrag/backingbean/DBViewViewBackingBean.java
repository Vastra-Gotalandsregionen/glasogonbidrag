package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.GrantAdjustment;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Lma;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.api.data.BeneficiaryRepository;
import se.vgregion.service.glasogonbidrag.api.data.IdentificationRepository;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.api.data.SupplierRepository;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.api.service.SupplierService;
import se.vgregion.service.glasogonbidrag.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.exception.GrantAdjustmentAlreadySetException;
import se.vgregion.service.glasogonbidrag.exception.GrantAlreadyExistException;
import se.vgregion.service.glasogonbidrag.exception.NoIdentificationException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component(value = "dbViewViewBackingBean")
@Scope(value = "request")
public class DBViewViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DBViewViewBackingBean.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private IdentificationRepository identificationRepository;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private FacesUtil facesUtil;

    private List<Supplier> suppliers;
    private List<Invoice> invoices;
    private List<Beneficiary> beneficiaries;

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public String insertSupplier() {
        LOGGER.info("DBViewViewBackingBean - insertSupplier()");

        Supplier supplier = new Supplier();
        supplier.setName("Specsavers");

        supplierService.create(supplier);

        return "view?faces-redirect=true";
    }

    public String insertBeneficiary1() {
        LOGGER.info("DBViewViewBackingBean - insertBeneficiary1()");

        Personal i1 = new Personal();
        i1.setNumber("11294377-1834");

        Beneficiary b1 = new Beneficiary();
        b1.setFirstName("Ethel");
        b1.setLastName("Gonzalez");
        b1.setIdentification(i1);

        insert(b1);

        return "view?faces-redirect=true";
    }

    public String insertBeneficiary2() {
        LOGGER.info("DBViewViewBackingBean - insertBeneficiary2()");

        Personal i2 = new Personal();
        i2.setNumber("67652979-0773");

        Beneficiary b2 = new Beneficiary();
        b2.setFirstName("Colin");
        b2.setLastName("Little");
        b2.setIdentification(i2);

        insert(b2);

        return "view?faces-redirect=true";
    }

    public String insertBeneficiary3() {
        LOGGER.info("DBViewViewBackingBean - insertBeneficiary3()");

        Personal i3 = new Personal();
        i3.setNumber("36386944-2631");

        Beneficiary b3 = new Beneficiary();
        b3.setFirstName("Larry");
        b3.setLastName("Douglas");
        b3.setIdentification(i3);

        insert(b3);

        return "view?faces-redirect=true";
    }

    public String insertBeneficiary4() {
        LOGGER.info("DBViewViewBackingBean - insertBeneficiary4()");

        Lma i4 = new Lma();
        i4.setNumber("50-008920/4");

        Beneficiary b4 = new Beneficiary();
        b4.setFirstName("Muhammed");
        b4.setLastName("Ali");
        b4.setIdentification(i4);

        insert(b4);

        return "view?faces-redirect=true";
    }

    private void insert(Beneficiary beneficiary) {
        FacesMessage message = null;

        try {
            beneficiaryService.create(beneficiary);
        } catch (NoIdentificationException e) {
            message = new FacesMessage("No identification added to user.");
            LOGGER.warn("No identiciation set for the user");
        } catch (PersistenceException e) {
            message = new FacesMessage("Exception persisting beneficiary. " +
                    "Got exception: " + e.getMessage());
            LOGGER.warn("Exception persisting beneficiary: {}.",
                    e.getMessage());
        }

        if (message != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, message);
        }
    }

    public String insertInvoice() {
        LOGGER.info("DBViewViewBackingBean - insertInvoice()");

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();


        List<Supplier> suppliers = supplierRepository.findAllByName("Specsavers");
        Supplier s = suppliers.get(0);

        Identification id1 = identificationRepository.findByPersonalNumber("11294377-1834");
        Beneficiary b1 = beneficiaryRepository.findWithPartsByIdent(id1);

        Identification id2 = identificationRepository.findByLMANumber("50-008920/4");
        Beneficiary b2 = beneficiaryRepository.findWithPartsByIdent(id2);


        Calendar cal = Calendar.getInstance();

        Grant g1 = new Grant();
        g1.setAmount(10000);
        g1.setVat(2500);
        g1.setBeneficiary(b1);
        g1.setDeliveryDate(cal.getTime());
        g1.setPrescriptionDate(cal.getTime()); //TODO: Make this work

//        b1.getPrescription().setDate(cal.getTime());

        Grant g2 = new Grant();
        g2.setAmount(20000);
        g2.setVat(5000);
        g2.setBeneficiary(b2);
        g2.setDeliveryDate(cal.getTime());
        g2.setPrescriptionDate(cal.getTime()); //TODO: Make this work

//        b2.getPrescription().setDate(cal.getTime());

        List<Grant> grants = new ArrayList<>();
        grants.add(g1);
        grants.add(g2);

        Invoice invoice = new Invoice();
        invoice.setSupplier(s);
        invoice.setGrants(grants);
        invoice.setAmount(30000);
        invoice.setVat(7500);
        invoice.setInvoiceNumber("10002");
        invoice.setVerificationNumber("E510396");
        invoice.setStatus(InvoiceStatus.IN_PROGRESS);

        try {
            beneficiaryService.update(b1);
        } catch (NoIdentificationException e) {
            FacesMessage message = new FacesMessage(
                    "Identification not set on beneficiary");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        try {
            beneficiaryService.update(b2);
        } catch (NoIdentificationException e) {
            FacesMessage message = new FacesMessage(
                    "Identification not set on beneficiary");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        invoiceService.create(userId, groupId, companyId, invoice);

        return "view?faces-redirect=true";
    }

    public String addGrant1() {
        LOGGER.info("DBViewViewBackingBean - addGrant1()");

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();

        Identification id1 = identificationRepository.findByPersonalNumber("67652979-0773");
        Beneficiary b1 = beneficiaryRepository.findWithPartsByIdent(id1);

        Invoice inv = invoiceRepository.findByVerificationNumber("E510396");
        long amount = inv.getAmount();
        long vat = inv.getVat();

        Calendar cal = Calendar.getInstance();

        Grant grant = new Grant();
        grant.setAmount(30000);
        grant.setVat(7500);
        grant.setBeneficiary(b1);
        grant.setDeliveryDate(cal.getTime());
        grant.setPrescriptionDate(cal.getTime()); //TODO: Make this work

//        b1.getPrescription().setDate(cal.getTime());

        try {
            beneficiaryService.update(b1);
        } catch (NoIdentificationException e) {
            FacesMessage message = new FacesMessage(
                    "Identification not set on beneficiary");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        try {
            invoiceService.updateAddGrant(userId, groupId, companyId, inv, grant);
        } catch (GrantAlreadyExistException e) {
            FacesMessage message = new FacesMessage(
                    "May not add the same grant twice.");
            FacesContext.getCurrentInstance().addMessage(null, message);

            LOGGER.info("Grant already exists on this invoice!");
        }

        return "view?faces-redirect=true";
    }

    public String addGrant2() {
        LOGGER.info("DBViewViewBackingBean - addGrant2()");

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();

        Identification id1 = identificationRepository.findByPersonalNumber("36386944-2631");
        Beneficiary b1 = beneficiaryRepository.findWithPartsByIdent(id1);

        Invoice inv = invoiceRepository.findByVerificationNumber("E510396");
        long amount = inv.getAmount();
        long vat = inv.getVat();

        Calendar cal = Calendar.getInstance();

        Grant grant = new Grant();
        grant.setAmount(60000);
        grant.setVat(15000);
        grant.setBeneficiary(b1);
        grant.setDeliveryDate(cal.getTime());
        grant.setPrescriptionDate(cal.getTime()); //TODO: Make this work

//        b1.getPrescription().setDate(cal.getTime());

        try {
            beneficiaryService.update(b1);
        } catch (NoIdentificationException e) {
            FacesMessage message = new FacesMessage(
                    "Identification not set on beneficiary");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        try {
            invoiceService.updateAddGrant(userId, groupId, companyId, inv, grant);
        } catch (GrantAlreadyExistException e) {
            LOGGER.warn("Already inserted.");
        }

        return "view?faces-redirect=true";
    }

    public String addGrantAdjustment() {
        LOGGER.info("DBViewViewBackingBean - addGrantAdjustment()");

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();

        Invoice inv = invoiceRepository.findByVerificationNumber("E510396");

        GrantAdjustment adjustment = new GrantAdjustment();
        adjustment.setAmount(60000);

        try {
            invoiceService.updateAddGrantAdjustment(
                    userId, groupId, companyId,
                    inv, adjustment);
        } catch (GrantAdjustmentAlreadySetException e) {
            LOGGER.warn("Already present.");
        }
        return "view?faces-redirect=true";
    }

    @PostConstruct
    protected void init() {
        LOGGER.info("DBViewViewBackingBean - init()");

        fetchSuppliers();
        fetchInvoices();
        fetchBeneficiaries();
    }

    private void fetchSuppliers() {
        suppliers = supplierRepository.findAll();
    }

    private void fetchInvoices() {
        invoices = invoiceRepository.findAll();
    }

    private void fetchBeneficiaries() {
        beneficiaries = beneficiaryRepository.findAll();
    }
}
