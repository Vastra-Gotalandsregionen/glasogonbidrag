package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletURLFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;
import se.vgregion.glasogonbidrag.flow.action.AddGrantAction;
import se.vgregion.glasogonbidrag.util.GrantUtil;
import se.vgregion.glasogonbidrag.util.TabUtil;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.validator.PersonalNumberValidator;
import se.vgregion.glasogonbidrag.viewobject.GrantTypeOtherVO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.PersonalIdentification;
import se.vgregion.service.glasogonbidrag.api.data.BeneficiaryRepository;
import se.vgregion.service.glasogonbidrag.api.data.GrantRepository;
import se.vgregion.service.glasogonbidrag.api.data.IdentificationRepository;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.api.service.GrantService;
import se.vgregion.service.glasogonbidrag.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.exception.GrantAlreadyExistException;
import se.vgregion.service.glasogonbidrag.exception.NoIdentificationException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "createInvoiceAddGrantBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreateInvoiceAddGrantBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CreateInvoiceViewBackingBean.class);

    private static final String GRANT_TYPE_AGE_0_TO_15 = "0";
    private static final String GRANT_TYPE_AGE_0_TO_19 = "1";
    private static final String GRANT_TYPE_OTHER = "2";

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private GrantRepository grantRepository;

    @Autowired
    private IdentificationRepository identificationRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private GrantService grantService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private GrantUtil grantUtil;

    @Autowired
    private PersonalNumberValidator personalNumberValidator;


    // Helpers

    private TabUtil tabUtil;
    private String portletNamespace;
    private boolean newBeneficiary;
    private Locale locale;

    // Flow

    private CreateInvoiceAddGrantPidFlow flow;

    // Main objects

    private Invoice invoice;
    private Grant grant;
    private Beneficiary beneficiary;

    // Session data

    private String number;
    private String deliveryDate;
    private String prescriptionDate;
    private String grantType;
    private String grantTypeLabel;

    private GrantTypeOtherVO grantTypeOtherVO;

    private String prescriber;
    private String prescriptionComment;

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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
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

    // Getter and Setters for Flow

    public CreateInvoiceAddGrantPidFlow getFlow() {
        return flow;
    }

    public void setFlow(CreateInvoiceAddGrantPidFlow flow) {
        this.flow = flow;
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

    public String getGrantTypeLabel() {
        return grantTypeLabel;
    }

    public void setGrantTypeLabel(String grantTypeLabel) {
        this.grantTypeLabel = grantTypeLabel;
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

    public GrantTypeOtherVO getGrantTypeOtherVO() {
        return grantTypeOtherVO;
    }

    public void setGrantTypeOtherVO(GrantTypeOtherVO grantTypeOtherVO) {
        this.grantTypeOtherVO = grantTypeOtherVO;
    }

    public String getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(String prescriber) {
        this.prescriber = prescriber;
    }

    public String getPrescriptionComment() {
        return prescriptionComment;
    }

    public void setPrescriptionComment(String prescriptionComment) {
        this.prescriptionComment = prescriptionComment;
    }

    // Listeners

    public void personalNumberListener() {
        LOGGER.info("personalNumberListener(): number={}", number);

        FacesContext context = FacesContext.getCurrentInstance();

        // Strip away century digits
        if(number.length() == 13) {
            number = number.substring(2, number.length());
        }

        boolean isNumberValid = personalNumberValidator.validatePersonalNumber(number);
        System.out.println("isNumberValid: " + isNumberValid);
        // Temp
        //isNumberValid = true;

        if(isNumberValid) {
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

            // Set flow
            flow = flow.nextState();
        } else {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Detta är inget giltigt personnummer...", "");

            context.addMessage(portletNamespace + ":addGrantForm:personalNumber", message);
        }

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

        // Set flow
        flow = flow.nextState();
    }

    public void grantTypeListener() {
        LOGGER.info("grantTypeListener()");

        if (GRANT_TYPE_OTHER.equals(grantType)) {
            // TODO: make address check against deliveryDate
            LOGGER.info("grantTypeListener(): addressCheck=");
        }

        // Set flow
        if (GRANT_TYPE_AGE_0_TO_15.equals(grantType)) {
            flow = flow.nextState(AddGrantAction.AGE_0_TO_15);
            grantTypeLabel = "0-15";
        } else if (GRANT_TYPE_AGE_0_TO_19.equals(grantType)) {
            flow = flow.nextState(AddGrantAction.AGE_0_TO_19);
            grantTypeLabel = "0-19";
        } else {
            flow = flow.nextState(AddGrantAction.OTHER);
            grantTypeLabel = "Övriga";
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

//        beneficiary.getPrescription().setDate(date);
        grant.setPrescriptionDate(date);

        if (GRANT_TYPE_AGE_0_TO_15.equals(grantType) ||
                GRANT_TYPE_AGE_0_TO_19.equals(grantType)) {
            // TODO: make address check against prescriptionDate
            LOGGER.info("prescriptionDateListener(): addressCheck=");
        }

        // Set flow
        flow = flow.nextState();
    }

    public void changePrescriptionTypeListener() {
        // Don't do anything.
    }

    public void otherPrescriptionTypeListener() {
        //TODO: Set values in beneficiary
        flow = flow.nextState();
    }

    public void otherPrescriptionDateListener() {
        //TODO: Set values in beneficiary
        flow = flow.nextState();
    }

    public void stepBackListener() {

        LOGGER.info("stepBackListener()");

        String showSection = facesUtil.fetchProperty("showSection");
        LOGGER.info("stepBackListener(): showSection: {}", showSection);

        AddGrantFlowState state = AddGrantFlowState.valueOf(showSection);

        // TODO: When step back to ENTER_PRESCRIPTION_DATE values for prescription date is lost. When step back to SELECT_GRANT_TYPE value for grant type is lost. However, when step back to ENTER_DELIVERY_DATE, value for deliveryDate is still there.

        switch (state) {
            case ENTER_PERSONAL_NUMBER:
                beneficiary = null; // This should be fetched again

                deliveryDate = null;
                prescriptionDate = null;
                grantType = null;
                grantTypeLabel = null;

                grantTypeOtherVO = new GrantTypeOtherVO();

                prescriber = null;
                prescriptionComment = null;

                //amountWithVat = null;
                break;
            case ENTER_DELIVERY_DATE:
                prescriptionDate = null;
                grantType = null;
                grantTypeLabel = null;

                grantTypeOtherVO = new GrantTypeOtherVO();

                prescriber = null;
                prescriptionComment = null;

                //amountWithVat = null;
                break;
            case SELECT_GRANT_TYPE:
                prescriptionDate = null;
                grantType = null;
                grantTypeLabel = null;

                grantTypeOtherVO = new GrantTypeOtherVO();

                prescriber = null;
                prescriptionComment = null;

                //amountWithVat = null;
                break;
            case ENTER_PRESCRIPTION_DATE:
                grantTypeOtherVO = new GrantTypeOtherVO();

                prescriber = null;
                prescriptionComment = null;

                //amountWithVat = null;
                break;
            case ENTER_GRANT_STATE_OTHER_TYPE:
//                grantTypeOtherVO = new GrantTypeOtherVO();
                prescriptionDate = null;

                prescriber = null;
                prescriptionComment = null;

                //amountWithVat = null;
                break;
            case ENTER_GRANT_STATE_OTHER_DATE:
                //amountWithVat = null;
                break;
            case ENTER_AMOUNT_AFTER_AGE:
                break;
            case ENTER_AMOUNT_AFTER_OTHER:
                break;
            case ENTER_ALL_DATA_AFTER_OTHER:
                break;
            case ENTER_ALL_DATA_AFTER_AGE:
                break;
        }

        flow = state.getState();
    }

    // Actions

    public String doDeleteGrant() {
        LOGGER.info("doDeleteGrant");

        Long grantId = grant.getId();

        invoiceService.updateDeleteGrant(invoice, grantId);

        return String.format(
                "add_grant" +
                        "?invoiceId=%d" +
                        "&faces-redirect=true" +
                        "&includeViewParams=true",
                invoice.getId());
    }

    public String doSaveGrantAndAddNew() {
        saveGrant();

        return String.format(
                "add_grant" +
                        "?invoiceId=%d" +
                        "&faces-redirect=true" +
                        "&includeViewParams=true",
                invoice.getId());
    }

    public String doSaveGrantAndShowOverview() {
        return saveGrant();
    }

    public String saveGrant() {

        // Fetch theme display
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();

        long tempInvoiceId = -1;
        Invoice tempInvoice = grant.getInvoice();
        if(tempInvoice != null) {
            tempInvoiceId = tempInvoice.getId();
        }

        LOGGER.info("Invoice id: " + tempInvoiceId);

        // Insert beneficiary first.
        if (newBeneficiary) {
            try {
                beneficiaryService.create(beneficiary);
            } catch (NoIdentificationException e) {
                LOGGER.warn("Beneficiary didn't have a identificaiton. " +
                        "Serious error in this flow.");

                FacesMessage message =
                        new FacesMessage("Generic fatal error...");
                FacesContext.getCurrentInstance().addMessage(null, message);

                return null;
            }
        }

        if (amountWithVat == null || amountWithVat.trim().isEmpty()) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Need to choose a", "");

            LOGGER.info("Return to view. amount Not filled in");

            return "view?faces-redirect=true";
        }

        try {
            BigDecimal amountWithVatDecimal = new BigDecimal(amountWithVat);
            grant.setAmountIncludingVatAsKrona(amountWithVatDecimal);
        } catch (NumberFormatException e) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Need to choose a", "");

            return "view?faces-redirect=true";
        }

        FacesMessage message = null;

        if (grant.getId() == null) {
            message = persistGrant(userId, groupId, companyId, invoice, grant);
        } else {
            message = updateGrant(userId, groupId, companyId, grant);
        }

        if(message != null) {
            FacesContext.getCurrentInstance().addMessage(null, message);
            return null;
        }

        return "view_invoice?faces-redirect=true&invoiceId=" + invoice.getId();
    }

    public FacesMessage persistGrant(long userId, long groupId, long companyId, Invoice invoice, Grant grant) {
        FacesMessage message = null;

        try {
            invoiceService.updateAddGrant(userId, groupId, companyId,
                    invoice, grant);
        } catch (GrantAlreadyExistException e) {
            LOGGER.warn("Cannot add the same grant twice.");

            message =
                    new FacesMessage("Cannot add the same grant twice.");
        }

        return message;
    }

    public FacesMessage updateGrant(long userId, long groupId, long companyId, Grant grant) {
        FacesMessage message = null;

        // TODO: only updated if data has changed
        grantService.update(grant);

        return message;
    }

    // Facelet styling methods

    /**
     * Create list of grant types as a list of select items.
     *
     * @return a select item list with grant types.
     */
    public List<SelectItem> getGrantTypes() {
        List<SelectItem> items = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date changeDate = cal.getTime();

        // Commented out EA 2016-06-15
        //items.add(new SelectItem("-1", ""));

        if (grant != null && grant.getDeliveryDate() != null &&
                grant.getDeliveryDate().before(changeDate)) {
            items.add(new SelectItem("0", "0-15"));
        } else {
            items.add(new SelectItem("1", "0-19"));
        }

        items.add(new SelectItem("2", "Övriga"));

        return items;
    }

    public String editGrant() {
        //String returnView = String.format("add_grant?invoiceId=%d&grantId=%d&faces-redirect=true&includeViewParams=true", invoiceId, grantId);
        //String returnView = String.format("add_grant?invoiceId=%d&faces-redirect=true&includeViewParams=true", invoiceId);

        Long invoiceId = facesUtil.fetchId("invoiceId");
        Long grantId = facesUtil.fetchId("grantId");

        LOGGER.info("editGrant - invoiceId is: " + invoiceId);

        String returnView = String.format(
                "add_grant" +
                "?invoiceId=%d" +
                "&grantId=%d" +
                "&faces-redirect=true" +
                "&includeViewParams=true",
                invoiceId, grantId);


        return returnView;
    }


    // Initializer

    @PostConstruct
    public void init() {
        LOGGER.info("CreateInvoiceAddGrantBackingBean - init()");

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        locale = themeDisplay.getLocale();
        // Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");

        portletNamespace = FacesContext.getCurrentInstance()
                .getExternalContext().encodeNamespace("");


        flow = AddGrantFlowState.ENTER_PERSONAL_NUMBER.getState();
        LOGGER.info("Current state: {}.", flow);

        long invoiceId = facesUtil.fetchId("invoiceId");
        invoice = invoiceRepository.findWithParts(invoiceId);

        newBeneficiary = false;

        //invoice = newInvoice;
        beneficiary = null;

        number = null;
        deliveryDate = null;
        prescriptionDate = null;
        grantType = null;
        grantTypeLabel = null;

        grantTypeOtherVO = new GrantTypeOtherVO();

        prescriber = null;
        prescriptionComment = null;

        // Todo: different types and conditions will affect the default number of amountWithVat
        amountWithVat = "800";

        tabUtil = new TabUtil(Arrays.asList("Personnummer", "LMA-Nummer"), 0);


        Long grantId = facesUtil.fetchId("grantId");
        if(grantId != null) {
            System.out.println("--- Found GrantId -----");

            grant = grantRepository.find(grantId);
            beneficiary = grant.getBeneficiary();
            // Todo: add find with parts to grantrepository -> Then row below will not be needed.
            beneficiary = beneficiaryRepository.findWithParts(beneficiary.getId());
            number = beneficiary.getIdentification().getString();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            deliveryDate = df.format(grant.getDeliveryDate());

            // Todo: code below (grantType) should be in a separate method. Code duplication (similar code elsewhere in backing bean
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2015);
            cal.set(Calendar.MONTH, Calendar.APRIL);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            Date changeDate = cal.getTime();

            if(grant.getDeliveryDate().before((changeDate))) {
                grantType = GRANT_TYPE_AGE_0_TO_15;
                grantTypeLabel = "0-15";
            } else {
                grantType = GRANT_TYPE_AGE_0_TO_19;
                grantTypeLabel = "0-19";
            }

            amountWithVat = grant.getAmountIncludingVatAsKrona().toString();

            //flow = AddGrantFlowState.ENTER_PERSONAL_NUMBER.getState();

            if(GRANT_TYPE_OTHER.equals(grantType)) {
                flow = AddGrantFlowState.ENTER_ALL_DATA_AFTER_OTHER.getState();
            }
            else if(GRANT_TYPE_AGE_0_TO_19.equals(grantType) || GRANT_TYPE_AGE_0_TO_15.equals(grantType)) {
                flow = AddGrantFlowState.ENTER_ALL_DATA_AFTER_AGE.getState();
            }

            //flow = AddGrantFlowState.ENTER_AMOUNT_AFTER_AGE.getState();
            LOGGER.info("Current state: {}.", flow);


        } else {
            grant = new Grant();
        }






    }

}
