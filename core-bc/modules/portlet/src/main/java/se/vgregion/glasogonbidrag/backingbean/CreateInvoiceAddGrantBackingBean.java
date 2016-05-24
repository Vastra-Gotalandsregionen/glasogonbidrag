package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.TabUtil;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.PersonalIdentification;
import se.vgregion.service.glasogonbidrag.api.data.BeneficiaryRepository;
import se.vgregion.service.glasogonbidrag.api.data.IdentificationRepository;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.exception.NoIdentificationException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "createInvoiceAddGrantBackingBean")
@Scope(value = "session")
public class CreateInvoiceAddGrantBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CreateInvoiceViewBackingBean.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private IdentificationRepository identificationRepository;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private FacesUtil facesUtil;

    // Helpers

    private TabUtil tabUtil;
    private String portletNamespace;
    private boolean newBeneficiary;

    // Main objects

    private Invoice invoice;
    private Grant grant;
    private Beneficiary beneficiary;

    // Session data

    private String number;
    private String deliveryDate;
    private String prescriptionDate;
    private String grantType;
    private String amountWithVat;


    // Getter and Setters for Helpers

    public TabUtil getTabUtil() {
        return tabUtil;
    }

    public String getPortletNamespace() {
        return portletNamespace;
    }

    public void setPortletNamespace(String portletNamespace) {
        this.portletNamespace = portletNamespace;
    }


    // Getter and Setters for Main objects

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Grant getGrant() {
        return grant;
    }

    public void setGrant(Grant grant) {
        this.grant = grant;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }


    // Getter and Setters for Session data

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAmountWithVat() {
        return amountWithVat;
    }

    public void setAmountWithVat(String amountWithVat) {
        this.amountWithVat = amountWithVat;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    // Listeners

    public void personalNumberListener() {
        LOGGER.info("personalNumberListener(): number={}", number);

        Identification identification =
                identificationRepository.findByPersonalNumber(number);
        if (identification == null) {
            identification = new PersonalIdentification(number);
        } else {
            beneficiary =
                    beneficiaryRepository.findWithPartsByIdent(identification);
        }

        if (beneficiary == null) {
            // TODO: Integrate with external service.
            beneficiary = new Beneficiary();
            beneficiary.setFirstName("Sven");
            beneficiary.setLastName("Göransson");
            beneficiary.setIdentification(identification);

            newBeneficiary = true;
        }

        LOGGER.info("createInvoiceAddGrantBackingBean - " +
                "personNumberListener(): {}", beneficiary);

        grant.setBeneficiary(beneficiary);
    }

    public void deliveryDateListener() {
        LOGGER.info("deliveryDateListener(): add {} to grant {}",
                deliveryDate, grant);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(deliveryDate);
        } catch (ParseException e) {
            LOGGER.warn("Exception. {}", e.getMessage());

            return;
        }

        grant.setDeliveryDate(date);
    }

    public void grantTypeListener() {
        LOGGER.info("grantTypeListener()");

        if ("2".equals(grantType)) {
            // TODO: make address check against deliveryDate
            LOGGER.info("grantTypeListener(): addressCheck=");
        }
    }

    public void prescriptionDateListener() {
        LOGGER.info("prescriptionDateListener(): add {} to grant {}",
                prescriptionDate, grant);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(prescriptionDate);
        } catch (ParseException e) {
            LOGGER.warn("Exception. {}", e.getMessage());

            return;
        }

        grant.setPrescriptionDate(date);

        if ("0".equals(grantType) || "1".equals(grantType)) {
            // TODO: make address check against prescriptionDate
            LOGGER.info("prescriptionDateListener(): addressCheck=");
        }
    }

    // Actions

    public String saveGrant() {
        // Insert beneficiary first.
        if (newBeneficiary) {
            try {
                beneficiaryService.create(beneficiary);
            } catch (NoIdentificationException e) {
                LOGGER.warn("Beneficiary didn't have a identificaiton. " +
                        "Serious error in this flow.");

                FacesMessage message = new FacesMessage("Generic fatal error...");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return null;
            }
        }

        //TODO: Fix Copy paste code.
        // Copied from CreateInvoiceViewBackingBean - register():141
        String amountWithVat = getAmountWithVat();
        if (amountWithVat == null || amountWithVat.trim().isEmpty()) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Need to choose a", "");

            LOGGER.info("Return to view. amount Not filled in");

            return "view?faces-redirect=true";
        }

        BigDecimal amountWithVatDecimal;
        try {
            amountWithVatDecimal = new BigDecimal(amountWithVat);
        } catch (NumberFormatException e) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Need to choose a", "");

            return "view?faces-redirect=true";
        }

        BigDecimal centWithVatDecimal = amountWithVatDecimal.multiply(new BigDecimal("100"));
        BigDecimal centDecimal = centWithVatDecimal.multiply(new BigDecimal("0.8"));
        BigDecimal vatDecimal = centWithVatDecimal.subtract(centDecimal);

        int amount = centDecimal.intValue();
        int vat = vatDecimal.intValue();

        grant.setAmount(amount);
        grant.setVat(vat);
        invoice.addGrant(grant);

        invoiceService.update(invoice);

        return "view_invoice?faces-redirect=true&invoiceId=" + invoice.getId();
    }

    // Facelet styling methods

    public List<SelectItem> getGrantTypes() {
        List<SelectItem> items = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date changeDate = cal.getTime();

        items.add(new SelectItem("-1", ""));

        if (grant != null && grant.getDeliveryDate() != null &&
                grant.getDeliveryDate().before(changeDate)) {
            items.add(new SelectItem("0", "0-15"));
        } else {
            items.add(new SelectItem("1", "0-19"));
        }

        items.add(new SelectItem("2", "Övriga"));

        return items;
    }

    public String getGrantTypeOutput() {
        if ("0".equals(grantType)) {
            return "0-15";
        } else if ("1".equals(grantType)) {
            return "0-19";
        } else if ("2".equals(grantType)) {
            return "Övriga";
        } else {
            return "";
        }
    }

    // Initializer

    public void init(Invoice newInvoice) {
        LOGGER.info("CreateInvoiceAddGrantBackingBean - init()");

        tabUtil = new TabUtil(Arrays.asList("Personnummer", "LMA-Nummer"), 0);

        portletNamespace = FacesContext.getCurrentInstance()
                .getExternalContext().encodeNamespace("");

        newBeneficiary = false;

        invoice = newInvoice;

        grant = new Grant();
        beneficiary = null;

        number = null;
        deliveryDate = null;
        grantType = null;
        prescriptionDate = null;
        amountWithVat = null;
    }
}
