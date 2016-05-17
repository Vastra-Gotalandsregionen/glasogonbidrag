package se.vgregion.glasogonbidrag.backingbean;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.LMAIdentification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.PersonalIdentification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.api.data.BeneficiaryRepository;
import se.vgregion.service.glasogonbidrag.api.data.IdentificationRepository;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.api.data.SupplierRepository;
import se.vgregion.service.glasogonbidrag.api.service.IdentificationService;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.api.service.SupplierService;
import se.vgregion.service.glasogonbidrag.api.service.BeneficiaryService;
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

@Component(value = "viewBean")
@Scope(value = "request")
public class ViewBackingBean {

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
        System.out.println("ViewBackingBean - insertSupplier");

        Supplier supplier = new Supplier();
        supplier.setName("Specsavers");

        supplierService.create(supplier);

        return "view?faces-redirect=true";
    }

    public String insertBeneficiary1() {
        System.out.println("ViewBackingBean - insertBeneficiary1");

        PersonalIdentification i1 = new PersonalIdentification();
        i1.setNumber("11294377-1834");

        Beneficiary b1 = new Beneficiary();
        b1.setFirstName("Ethel");
        b1.setLastName("Gonzalez");
        b1.setIdentification(i1);

        insert(b1);

        return "view?faces-redirect=true";
    }

    public String insertBeneficiary2() {
        System.out.println("ViewBackingBean - insertBeneficiary2");

        PersonalIdentification i2 = new PersonalIdentification();
        i2.setNumber("67652979-0773");

        Beneficiary b2 = new Beneficiary();
        b2.setFirstName("Colin");
        b2.setLastName("Little");
        b2.setIdentification(i2);

        insert(b2);

        return "view?faces-redirect=true";
    }

    public String insertBeneficiary3() {
        System.out.println("ViewBackingBean - insertBeneficiary2");

        PersonalIdentification i3 = new PersonalIdentification();
        i3.setNumber("36386944-2631");

        Beneficiary b3 = new Beneficiary();
        b3.setFirstName("Larry");
        b3.setLastName("Douglas");
        b3.setIdentification(i3);

        insert(b3);

        return "view?faces-redirect=true";
    }

    public String insertBeneficiary4() {
        System.out.println("ViewBackingBean - insertBeneficiary2");

        LMAIdentification i4 = new LMAIdentification();
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
        } catch (PersistenceException e) {
            message = new FacesMessage("Exception persisting beneficiary. " +
                    "Got exception: " + e.getMessage());
        }

        if (message != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, message);
        }
    }

    public String insertInvoice() {
        System.out.println("ViewBackingBean - insertInvoice");

        Supplier supplier = supplierRepository.find("Specsavers");

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
        g1.setPrescriptionDate(cal.getTime());

        Grant g2 = new Grant();
        g2.setAmount(20000);
        g2.setVat(5000);
        g2.setBeneficiary(b2);
        g2.setDeliveryDate(cal.getTime());
        g2.setPrescriptionDate(cal.getTime());
        List<Grant> grants = new ArrayList<>();
        grants.add(g1);
        grants.add(g2);

        Invoice invoice = new Invoice();
        invoice.setSupplier(supplier);
        invoice.setGrants(grants);
        invoice.setAmount(30000);
        invoice.setVat(7500);
        invoice.setInvoiceDate(cal.getTime());
        invoice.setInvoiceNumber("10002");
        invoice.setVerificationNumber("E510396");
        invoice.setUserId(20159);
        invoice.setCompanyId(20155);
        invoice.setGroupId(20195);

        invoiceService.create(invoice);

        return "view?faces-redirect=true";
    }

    public String addGrant1() {
        Identification id1 = identificationRepository.findByPersonalNumber("36386944-2631");
        Beneficiary b1 = beneficiaryRepository.findWithPartsByIdent(id1);

        Invoice inv = invoiceRepository.findByVerificationNumber("E510396");

        Calendar cal = Calendar.getInstance();

        Grant g1 = new Grant();
        g1.setAmount(30000);
        g1.setVat(7500);
        g1.setBeneficiary(b1);
        g1.setDeliveryDate(cal.getTime());
        g1.setPrescriptionDate(cal.getTime());

        inv.addGrant(g1);
        invoiceService.update(inv);

        return "view?faces-redirect=true";
    }

    public String addGrant2() {
        Identification id1 = identificationRepository.findByPersonalNumber("36386944-2631");
        Beneficiary b1 = beneficiaryRepository.findWithPartsByIdent(id1);

        Invoice inv = invoiceRepository.findByVerificationNumber("E510396");

        Calendar cal = Calendar.getInstance();

        Grant g1 = new Grant();
        g1.setAmount(60000);
        g1.setVat(15000);
        g1.setBeneficiary(b1);
        g1.setDeliveryDate(cal.getTime());
        g1.setPrescriptionDate(cal.getTime());

        inv.addGrant(g1);
        invoiceService.update(inv);

        return "view?faces-redirect=true";
    }

    @PostConstruct
    protected void init() {
        System.out.println("ViewBackingBean - init");

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
