package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.constants.GbConstants;
import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;
import se.vgregion.glasogonbidrag.flow.action.AddGrantAction;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.GrantUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.glasogonbidrag.util.TabUtil;
import se.vgregion.glasogonbidrag.validator.PersonalNumberValidator;
import se.vgregion.glasogonbidrag.viewobject.BeneficiaryVO;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.*;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Lma;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Other;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Reserve;
import se.vgregion.portal.glasogonbidrag.value.PrescriptionValueObject;
import se.vgregion.service.glasogonbidrag.domain.api.service.*;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantAlreadyExistException;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantMissingAreaException;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;
import se.vgregion.service.glasogonbidrag.integration.api.BeneficiaryLookupService;
import se.vgregion.service.glasogonbidrag.local.api.GrantRuleValidationService;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberFormatService;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryTransport;
import se.vgregion.service.glasogonbidrag.types.GrantRuleResult;
import se.vgregion.service.glasogonbidrag.types.InvoiceBeneficiaryIdentificationTuple;
import se.vgregion.service.glasogonbidrag.types.InvoiceBeneficiaryTuple;
import se.vgregion.service.glasogonbidrag.types.InvoiceGrantTuple;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "createInvoiceAddGrantBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CreateInvoiceAddGrantBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CreateInvoiceViewBackingBean.class);

    private static final Date NEW_RULESET_CHANGE_DATE;
    static {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        NEW_RULESET_CHANGE_DATE = cal.getTime();
    }

    private static final String GRANT_TYPE_AGE_0_TO_15 = "0";
    private static final String GRANT_TYPE_AGE_0_TO_19 = "1";
    private static final String GRANT_TYPE_OTHER = "2";

    @Autowired
    private BeneficiaryLookupService beneficiaryLookupService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private DiagnoseService diagnoseService;

    @Autowired
    private GrantService grantService;

    @Autowired
    private GrantRuleValidationService grantRuleValidationService;

    @Autowired
    private IdentificationService identificationService;

    @Autowired
    private InvoiceService invoiceService;


    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private GrantUtil grantUtil;

    @Autowired
    private LiferayUtil liferayUtil;


    @Autowired
    private PersonalNumberValidator personalNumberValidator;

    @Autowired
    private PersonalNumberFormatService personalNumberFormatService;

    @Autowired
    private MessageSource messageSource;


    // Helpers
    private TabUtil tabUtil;
    private boolean newBeneficiary;

    // Flow

    private CreateInvoiceAddGrantPidFlow grantFlow;

    // Main objects
    private Invoice invoice;
    private Grant grant;
    private Beneficiary beneficiary;

    // Session data
    private BeneficiaryVO beneficiaryVO;
    private Date deliveryDate;
    private String grantType;
    private String grantTypeLabel;
    private Prescription latestBeneficiaryPrescription;

    private PrescriptionValueObject prescriptionValueObject;

    private String amountWithVat;


    // Getter and Setters for Helpers

    public TabUtil getTabUtil() {
        return tabUtil;
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

    public BeneficiaryVO getBeneficiaryVO() {
        return beneficiaryVO;
    }

    public void setBeneficiaryVO(BeneficiaryVO beneficiaryVO) {
        this.beneficiaryVO = beneficiaryVO;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    // Getter and Setters for Flow

    public CreateInvoiceAddGrantPidFlow getGrantFlow() {
        return grantFlow;
    }

    public void setGrantFlow(CreateInvoiceAddGrantPidFlow grantFlow) {
        this.grantFlow = grantFlow;
    }

    // Getter and Setters for Session data

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
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

    public Prescription getLatestBeneficiaryPrescription() {
        return latestBeneficiaryPrescription;
    }

    public void setLatestBeneficiaryPrescription(Prescription latestBeneficiaryPrescription) {
        this.latestBeneficiaryPrescription = latestBeneficiaryPrescription;
    }

    public String getAmountWithVat() {
        return amountWithVat;
    }

    public void setAmountWithVat(String amountWithVat) {
        this.amountWithVat = amountWithVat;
    }

    public PrescriptionValueObject getPrescriptionValueObject() {
        return prescriptionValueObject;
    }

    public void setPrescriptionValueObject(PrescriptionValueObject prescriptionValueObject) {
        this.prescriptionValueObject = prescriptionValueObject;
    }

    // Listeners

    public void identificationLMAListener() {
        // TODO: can validations be made?
        // TODO: Handle none LMA

        String identificationNumber = beneficiaryVO.getIdentificationNumber();

        Identification identification = identificationService.findByLMANumber(identificationNumber);

        if (identification == null) {
            identification = new Lma(identificationNumber, beneficiaryVO.getDateOfOBirth());
        } else {
            beneficiary = beneficiaryService.findWithPartsByIdent(identification);
        }

        if (beneficiary == null) {
            beneficiary = new Beneficiary();
            beneficiary.setIdentification(identification);

            if("".equals(beneficiaryVO.getFullName())) {
                beneficiary.setFullName("-");
            } else {
                beneficiary.setFullName(beneficiaryVO.getFullName());
            }

            newBeneficiary = true;
        }

        // TODO: Should be reviewed
        grant.setCounty(GbConstants.NON_IDENTIFIED_DEFAULT_COUNTY);
        grant.setMunicipality(GbConstants.NON_IDENTIFIED_DEFAULT_MUNICIPALITY);

        grant.setBeneficiary(beneficiary);

        // Set grantFlow
        grantFlow = grantFlow.nextState();
    }

    public void identificationPIDListener() {

        // TODO: Upgrade the PersonaNumberFormatService so that it can sanitize user input.
        String identificationNumber = beneficiaryVO.getIdentificationNumber();

        // TODO: This is temp fixes. Should be moved to a service. First we remove century digits, then add them again =).
        // Strip away century digits
        if(identificationNumber.length() == 13) {
            identificationNumber = identificationNumber.substring(2, identificationNumber.length());
        }


        String localFormat = personalNumberFormatService.to(identificationNumber, "2016");
        LOGGER.info("identificationPIDListener(): localFormat={}", localFormat);

        FacesContext context = FacesContext.getCurrentInstance();

        boolean isNumberValid = personalNumberValidator.validatePersonalNumber(identificationNumber);
        System.out.println("isNumberValid: " + isNumberValid);
        // Temp
        //isNumberValid = true;

        if(isNumberValid) {
            Identification identification =
                    identificationService.findByPersonalNumber(localFormat);
            if (identification == null) {
                identification = new Personal(localFormat);
            } else {
                beneficiary = beneficiaryService
                        .findWithPartsByIdent(identification);

                latestBeneficiaryPrescription = prescriptionService
                        .findLatest(beneficiary);
            }

            // TODO: Temp code
            Calendar cal = new GregorianCalendar();
            Date currentDate = cal.getTime();

            BeneficiaryTransport transport =
                    beneficiaryLookupService
                            .fetchNameAndAddress(localFormat, currentDate);

            if (beneficiary == null) {
                // TODO: Handle the integration better.
                beneficiary = new Beneficiary();
                beneficiary.setIdentification(identification);

                newBeneficiary = true;
            }

            beneficiary.setFullName(transport.getName().getFullName());
            beneficiary.setSex(transport.getName().getSex());

            grant.setCounty(transport.getArea().getCounty());
            grant.setMunicipality(transport.getArea().getMunicipality());

            LOGGER.info("createInvoiceAddGrantBackingBean - " +
                    "personNumberListener(): {}", beneficiary);

            grant.setBeneficiary(beneficiary);

            if (grant.getCounty() == null
                    || grant.getMunicipality() == null
                    || grant.getCounty().trim().isEmpty()
                    || grant.getMunicipality().trim().isEmpty()) {

                FacesMessage message = new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "reg-grant-error-could-not-fetch-area", "");

                context.addMessage(liferayUtil.getPortletNamespace() + ":addGrantForm:personalNumber", message);
            } else {
                // Set grantFlow
                grantFlow = grantFlow.nextState();
            }
        } else {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "reg-grant-error-not-a-valid-personal-number", "");

            context.addMessage(liferayUtil.getPortletNamespace() + ":addGrantForm:personalNumber", message);
        }
    }

    public void identificationOtherListener() {
        String identificationNumber = beneficiaryVO.getIdentificationNumber();

        Identification identification = new Other(identificationNumber, beneficiaryVO.getDateOfOBirth());

        createBeneficiaryWithoutPersonalNumber(identification, beneficiaryVO);

        // Set grantFlow
        grantFlow = grantFlow.nextState();
    }


    public void identificationReserveListener() {
        String identificationNumber = beneficiaryVO.getIdentificationNumber();

        Identification identification = new Reserve(identificationNumber, beneficiaryVO.getDateOfOBirth());

        createBeneficiaryWithoutPersonalNumber(identification, beneficiaryVO);

        // Set grantFlow
        grantFlow = grantFlow.nextState();
    }

    public void deliveryDateListener() {
        LOGGER.info("deliveryDateListener(): add {} to grant {}",
                deliveryDate, grant);

        grant.setDeliveryDate(deliveryDate);

        // Set grantFlow
        grantFlow = grantFlow.nextState();

        // Check if latestBeneficiaryPrescription exists and diagnosis is A, K or S. Then move to next state. Also, pass a parameter that grant type should
        // Be automatically chosen. Also pass info about diagnosis to populate

        if(latestBeneficiaryPrescription != null) {
            Diagnose diagnose = latestBeneficiaryPrescription.getDiagnose();
            DiagnoseType diagnoseType = diagnose.getType();

            // Foobar
            if(diagnoseType != DiagnoseType.NONE) {
                grantFlow = grantFlow.nextState(AddGrantAction.OTHER);

                prescriptionValueObject =
                        latestBeneficiaryPrescription.getValueObject();

                grantType = GRANT_TYPE_OTHER;
                grantTypeLabel = "grant-type-other";

                grantFlow = grantFlow.nextState();
            }
        }

    }

    public void grantTypeListener() {
        LOGGER.info("grantTypeListener()");

        if (GRANT_TYPE_OTHER.equals(grantType)) {
            // TODO: make address check against deliveryDate
            LOGGER.info("grantTypeListener(): addressCheck=");
        }

        // Set grantFlow
        switch (grantType) {
            case GRANT_TYPE_AGE_0_TO_15:
                grantFlow = grantFlow.nextState(AddGrantAction.AGE_0_TO_15);
                grantTypeLabel = "grant-type-0-15";
                break;
            case GRANT_TYPE_AGE_0_TO_19:
                grantFlow = grantFlow.nextState(AddGrantAction.AGE_0_TO_19);
                grantTypeLabel = "grant-type-0-19";
                break;
            default:
                grantFlow = grantFlow.nextState(AddGrantAction.OTHER);
                grantTypeLabel = "grant-type-other";
                break;
        }
    }

    public void prescriptionDateListener() {
        LOGGER.info("prescriptionDateListener(): add {} to grant {}",
                prescriptionValueObject.getDate(), grant);

        if (GRANT_TYPE_AGE_0_TO_15.equals(grantType) ||
                GRANT_TYPE_AGE_0_TO_19.equals(grantType)) {
            // TODO: make address check against prescriptionDate
            LOGGER.info("prescriptionDateListener(): addressCheck=");
        }

        // Set grantFlow
        grantFlow = grantFlow.nextState();
    }

    public void changePrescriptionTypeListener() {
        // Don't do anything.
    }

    public void changeVisualLateralityListener() {
        // Don't do anything.
    }

    public void otherPrescriptionTypeListener() {
        //TODO: Set values in beneficiary
        grantFlow = grantFlow.nextState();
    }

    public void otherPrescriptionDateListener() {
        //TODO: Set values in beneficiary
        grantFlow = grantFlow.nextState();
    }

    public void stepBackListener() {

        LOGGER.info("stepBackListener()");

        String showSection = facesUtil.fetchProperty("showSection");
        LOGGER.info("stepBackListener(): showSection: {}", showSection);

        AddGrantFlowState state = AddGrantFlowState.valueOf(showSection);

        //TODO: When step back to ENTER_PRESCRIPTION_DATE values for prescription date is lost. When step back to SELECT_GRANT_TYPE value for grant type is lost. However, when step back to ENTER_DELIVERY_DATE, value for deliveryDate is still there.
        switch (state) {
            case ENTER_IDENTIFICATION:
                beneficiary = null; // This should be fetched again

                deliveryDate = null;
                grantType = null;
                grantTypeLabel = null;

                prescriptionValueObject = new PrescriptionValueObject();

                //amountWithVat = null;
                break;
            case ENTER_DELIVERY_DATE:
                grantType = null;
                grantTypeLabel = null;

                prescriptionValueObject = new PrescriptionValueObject();

                //amountWithVat = null;
                break;
            case SELECT_GRANT_TYPE:
                grantType = null;
                grantTypeLabel = null;

                prescriptionValueObject = new PrescriptionValueObject();

                //amountWithVat = null;
                break;
            case ENTER_PRESCRIPTION_DATE:
                Date prescriptionDate = prescriptionValueObject.getDate();

                prescriptionValueObject = new PrescriptionValueObject();

                prescriptionValueObject.setDate(prescriptionDate);

                //amountWithVat = null;
                break;
            case ENTER_GRANT_STATE_OTHER_TYPE:

                prescriptionValueObject.setDate(null);
                prescriptionValueObject.setComment(null);
                prescriptionValueObject.setPrescriber(null);

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

        grantFlow = state.getState();
    }

    // Actions

    public String doDeleteGrant() {
        LOGGER.info("doDeleteGrant");

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        String caseWorker = themeDisplay.getUser().getScreenName();

        Long grantId = grant.getId();

        InvoiceBeneficiaryTuple tuple = invoiceService.updateDeleteGrant(
                caseWorker, invoice, grantId);

        invoice = tuple.getInvoice();
        beneficiary = tuple.getBeneficiary();

        return String.format(
                "add_grant" +
                        "?invoiceId=%d" +
                        "&faces-redirect=true" +
                        "&includeViewParams=true",
                invoice.getId());
    }

    public String doSave() {
        String responseView = String.format(
                "add_grant" +
                        "?faces-redirect=true" +
                        "&includeViewParams=true" +
                        "&invoiceId=%d", invoice.getId());
        return saveObjects(responseView);
    }

    public String doSaveAndReturn() {
        String responseView = String.format(
                "view_invoice" +
                        "?faces-redirect=true" +
                        "&invoiceId=%d", invoice.getId());
        return saveObjects(responseView);
    }

    private void createBeneficiaryWithoutPersonalNumber(Identification identification, BeneficiaryVO beneficiaryVO) {
        beneficiary = new Beneficiary();
        beneficiary.setIdentification(identification);

        if("".equals(beneficiaryVO.getFullName())) {
            beneficiary.setFullName("-");
        } else {
            beneficiary.setFullName(beneficiaryVO.getFullName());
        }

        newBeneficiary = true;

        // TODO: Should be reviewed
        grant.setCounty(GbConstants.NON_IDENTIFIED_DEFAULT_COUNTY);
        grant.setMunicipality(GbConstants.NON_IDENTIFIED_DEFAULT_MUNICIPALITY);

        grant.setBeneficiary(beneficiary);
    }

    private String saveObjects(String responseView) {
        // This should do the thing
        List<FacesMessage> messages = handleObjects();

        // If everything went well:
        // return and overview view
        if (!messages.isEmpty()) {
            addMessagesToContext(messages);
            return null;
        }

        // otherwise return the view.
        return responseView;
    }

    private void addMessagesToContext(List<FacesMessage> messages) {
        FacesContext context = FacesContext.getCurrentInstance();

        for (FacesMessage message : messages) {
            context.addMessage(null, message);
        }
    }

    private List<FacesMessage> handleObjects() {
        FacesMessage message = null;

        // Store a list of messages, this is all errors we've received.
        List<FacesMessage> messages = new ArrayList<>();

        // Populate domain objects from View objects.
        message = assignAmount();
        if (message != null) {
            messages.add(message);

            return messages;
        }

        // Validate the data
        List<FacesMessage> violationMessages = setupAndValidateObjects();
        if (violationMessages != null) {
            messages.addAll(violationMessages);

            return messages;
        }

        // TODO: I think this parts below should be run in an transaction.

        // Handle beneficiary
        message = handleBeneficiaryObject();
        if (message != null) {
            messages.add(message);

            return messages;
        }

        // Handle grant
        message = handleGrantObject();
        if (message != null) {
            messages.add(message);

            return messages;
        }

        return messages;
    }

    private FacesMessage assignAmount() {
        Locale locale = facesUtil.getLocale();

        if (amountWithVat == null || amountWithVat.trim().isEmpty()) {
            String localizedMessage = messageSource
                    .getMessage("reg-grant-need-to-enter-amount",
                            new Object[0], locale);

            return new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    localizedMessage,
                    "");
        }

        try {
            BigDecimal amountWithVatDecimal = new BigDecimal(amountWithVat);
            grant.setAmountIncludingVatAsKrona(amountWithVatDecimal);
        } catch (NumberFormatException e) {
            String localizedMessage = messageSource
                    .getMessage("reg-grant-amount-is-not-a-number",
                            new Object[0], locale);

            return new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    localizedMessage,
                    "");
        }

        return null;
    }

    // TODO: Verify that this method actually setup all relations
    private List<FacesMessage> setupAndValidateObjects() {

        if (grant.getPrescription() == null) {
            Prescription prescription =
                    prescriptionValueObject.getPrescription();
            grant.setPrescription(prescription);
        } else {
            prescriptionValueObject.patchPrescription(grant.getPrescription());
        }

        // TODO: Should we move code that set relation from grant to prescription here?

        beneficiary.getGrants().add(grant);

        GrantRuleResult result = grantRuleValidationService.test(
                grant, beneficiary.getGrants());

        if(result.hasViolations()) {
            Locale locale = facesUtil.getLocale();

            List<FacesMessage> messages = new ArrayList<>();

            for (String violation : result.getViolationStrings()) {
                String localizedMessage = messageSource
                        .getMessage(violation,
                                new Object[0], locale);

                messages.add(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, localizedMessage, ""));
            }

            return messages;
        } else {
            return null;
        }
    }

    private FacesMessage handleBeneficiaryObject() {
        Locale locale = facesUtil.getLocale();

        if (beneficiary.getId() == null) {
            try {
                beneficiaryService.create(beneficiary);
            } catch (NoIdentificationException e) {
                String localizedMessage = messageSource.getMessage(
                        "reg-grant-beneficiary-do-not-have-identification",
                        new Object[0], locale);

                return new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        localizedMessage,
                        "");
            }
        }

        return null;
    }

    private FacesMessage handleGrantObject() {
        Locale locale = facesUtil.getLocale();

        try {
            storeOrUpdateGrant();
        } catch (GrantAlreadyExistException e) {
            LOGGER.warn("Cannot add the same grant twice.");

            String localizedMessage = messageSource
                    .getMessage("reg-grant-error-same-grant-twice",
                            new Object[0], locale);

            return new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    localizedMessage,
                    "");
        } catch (GrantMissingAreaException e) {
            LOGGER.warn("Couldn't fetch County or Municipality from integration!");

            String localizedMessage = messageSource
                    .getMessage("reg-grant-error-could-not-fetch-area",
                            new Object[0], locale);

            return new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    localizedMessage,
                    "");
        } catch (NoIdentificationException e) {
            String localizedMessage = messageSource.getMessage(
                    "reg-grant-beneficiary-do-not-have-identification",
                    new Object[0], locale);

            return new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    localizedMessage,
                    "");
        }

        return null;
    }

    private void storeOrUpdateGrant()
            throws GrantAlreadyExistException, GrantMissingAreaException,
                   NoIdentificationException {
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();
        String caseWorker = themeDisplay.getUser().getScreenName();

        if (grant.getId() == null) {
            InvoiceBeneficiaryIdentificationTuple tuple = invoiceService
                    .updateAddGrant(
                            userId, groupId, companyId,
                            caseWorker, invoice, grant);

            invoice = tuple.getInvoice();
            beneficiary = tuple.getBeneficiary();
        } else {
            // TODO: Add more code here.
            InvoiceGrantTuple result =
                    invoiceService.updateGrant(caseWorker, invoice, grant);

            invoice = result.getInvoice();
            grant = result.getGrant();
        }
    }

    // Facelet styling methods

    /**
     * Create list of grant types as a list of select items.
     *
     * @return a select item list with grant types.
     */
    public List<SelectItem> getGrantTypes() {
        List<SelectItem> items = new ArrayList<>();

        // Commented out EA 2016-06-15
        //items.add(new SelectItem("-1", ""));

        if (grant != null && grant.getDeliveryDate() != null &&
                grant.getDeliveryDate().before(NEW_RULESET_CHANGE_DATE)) {
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

        // Initialize with start states.
        setupDefaults();

        LOGGER.info("Current state: {}.", grantFlow);

        // Fetch properties from user navigation.
        long invoiceId = facesUtil.fetchId("invoiceId");
        long grantId = facesUtil.fetchId("grantId");

        invoice = invoiceService.findWithParts(invoiceId);
        grant = grantService.findWithParts(grantId);

        // TODO: if existing (already persited) grant - load from DB (if not already loaded)
        if (grant == null) {
            grant = new Grant();
            return;
        }

        beneficiary = grant.getBeneficiary();
        latestBeneficiaryPrescription =
                prescriptionService.findLatest(beneficiary);

        // Populate beneficiaryVO (not moved yet)
        beneficiaryVO.setIdentificationNumber(
                beneficiary.getIdentification().getNumber());
        deliveryDate = grant.getDeliveryDate();

        Prescription prescription = grant.getPrescription();
        prescriptionValueObject = prescription.getValueObject();

        if (prescriptionValueObject.getType() != DiagnoseType.NONE) {
            grantType = GRANT_TYPE_OTHER;
            grantTypeLabel = "grant-type-other";
        } else {
            //TODO: code below (grantType) should be in a separate method. Code duplication (similar code elsewhere in this backing bean)
            if(grant.getDeliveryDate().before((NEW_RULESET_CHANGE_DATE))) {
                grantType = GRANT_TYPE_AGE_0_TO_15;
                grantTypeLabel = "grant-type-0-15";
            } else {
                grantType = GRANT_TYPE_AGE_0_TO_19;
                grantTypeLabel = "grant-type-0-19";
            }
        }

        amountWithVat = grant.getAmountIncludingVatAsKrona().toString();

        if(GRANT_TYPE_OTHER.equals(grantType)) {
            grantFlow = AddGrantFlowState.ENTER_ALL_DATA_AFTER_OTHER.getState();
        }
        else if(GRANT_TYPE_AGE_0_TO_19.equals(grantType) || GRANT_TYPE_AGE_0_TO_15.equals(grantType)) {
            grantFlow = AddGrantFlowState.ENTER_ALL_DATA_AFTER_AGE.getState();
        }
    }

    private void setupDefaults() {
        grantFlow = AddGrantFlowState.ENTER_IDENTIFICATION.getState();

        latestBeneficiaryPrescription = null;

        newBeneficiary = false;

        beneficiary = null;

        deliveryDate = null;
        grantType = null;
        grantTypeLabel = null;

        beneficiaryVO = new BeneficiaryVO();
        beneficiaryVO.setIdentificationType(IdentificationType.PERSONAL);

        prescriptionValueObject = new PrescriptionValueObject();

        amountWithVat = "800";

        tabUtil = new TabUtil(Arrays.asList(
                IdentificationType.PERSONAL.getLanguageKey(),
                IdentificationType.LMA.getLanguageKey(),
                IdentificationType.RESERVE.getLanguageKey(),
                IdentificationType.OTHER.getLanguageKey()
        ), 0);

    }
}
