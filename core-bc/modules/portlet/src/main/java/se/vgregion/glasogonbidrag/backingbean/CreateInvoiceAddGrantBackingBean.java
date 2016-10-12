package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;
import se.vgregion.glasogonbidrag.flow.action.AddGrantAction;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.GrantUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.glasogonbidrag.util.TabUtil;
import se.vgregion.glasogonbidrag.validator.PersonalNumberValidator;
import se.vgregion.glasogonbidrag.viewobject.BeneficiaryVO;
import se.vgregion.glasogonbidrag.viewobject.PrescriptionVO;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.*;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Keratoconus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Special;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Lma;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;
import se.vgregion.service.glasogonbidrag.domain.api.service.*;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantAlreadyExistException;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;
import se.vgregion.service.glasogonbidrag.integration.api.BeneficiaryLookupService;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberFormatService;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryTransport;

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
    private IdentificationService identificationService;

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private DiagnoseService diagnoseService;

    @Autowired
    private GrantService grantService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private BeneficiaryLookupService beneficiaryLookupService;

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private GrantUtil grantUtil;

    @Autowired
    private PersonalNumberValidator personalNumberValidator;

    @Autowired
    private PersonalNumberFormatService personalNumberFormatService;

    @Autowired
    private LiferayUtil liferayUtil;

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

    private PrescriptionVO prescriptionVO;

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

    public String getAmountWithVat() {
        return amountWithVat;
    }

    public void setAmountWithVat(String amountWithVat) {
        this.amountWithVat = amountWithVat;
    }

    public PrescriptionVO getPrescriptionVO() {
        return prescriptionVO;
    }

    public void setPrescriptionVO(PrescriptionVO prescriptionVO) {
        this.prescriptionVO = prescriptionVO;
    }

    // Listeners

    public void lmaNumberListener() {
        // TODO: can validations be made?
        // TODO: Handle none LMA

        String identificationNumber = beneficiaryVO.getIdentificationNumber();

        Identification identification = identificationService.findByLMANumber(identificationNumber);

        if (identification == null) {
            // TODO: birthyear should not be hardcoded

            identification = new Lma(identificationNumber);
        } else {
            beneficiary = beneficiaryService.findWithPartsByIdent(identification);
        }

        if (beneficiary == null) {
            beneficiary = new Beneficiary();
            beneficiary.setIdentification(identification);

            if("".equals(beneficiaryVO.getFirstName())) {
                beneficiary.setFirstName("-");
            } else {
                beneficiary.setFirstName(beneficiaryVO.getFirstName());
            }

            if("".equals(beneficiaryVO.getSurName())) {
                beneficiary.setLastName("-");
            } else {
                beneficiary.setLastName(beneficiaryVO.getSurName());
            }

            newBeneficiary = true;
        }

        grant.setBeneficiary(beneficiary);

        // Set grantFlow
        grantFlow = grantFlow.nextState();
    }

    public void personalNumberListener() {

        String identificationNumber = beneficiaryVO.getIdentificationNumber();

        // TODO: This is temp fix
        String localFormat = personalNumberFormatService.to(identificationNumber, "2016");
        LOGGER.info("personalNumberListener(): localFormat={}", localFormat);

        FacesContext context = FacesContext.getCurrentInstance();

        // Strip away century digits
        if(identificationNumber.length() == 13) {
            identificationNumber = identificationNumber.substring(2, identificationNumber.length());
        }

        boolean isNumberValid = personalNumberValidator.validatePersonalNumber(identificationNumber);
        System.out.println("isNumberValid: " + isNumberValid);
        // Temp
        //isNumberValid = true;

        if(isNumberValid) {
            Identification identification =
                    identificationService.findByPersonalNumber(identificationNumber);
            if (identification == null) {
                identification = new Personal(identificationNumber);
            } else {
                beneficiary =
                        beneficiaryService.findWithPartsByIdent(identification);
            }

            if (beneficiary == null) {
                // TODO: Handle the integration better.
                beneficiary = new Beneficiary();
                beneficiary.setIdentification(identification);

                // TODO: Temp code
                Calendar cal = new GregorianCalendar();
                Date currentDate = cal.getTime();

                BeneficiaryTransport transport =
                        beneficiaryLookupService
                                .fetchNameAndAddress(localFormat, currentDate);
                beneficiary.setFirstName(transport.getName().getFirstName());
                beneficiary.setLastName(transport.getName().getLastName());

                grant.setCounty(transport.getArea().getCounty());
                grant.setMunicipality(transport.getArea().getMunicipality());

                newBeneficiary = true;
            }

            LOGGER.info("createInvoiceAddGrantBackingBean - " +
                    "personNumberListener(): {}", beneficiary);

            grant.setBeneficiary(beneficiary);

            // Set grantFlow
            grantFlow = grantFlow.nextState();
        } else {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "reg-grant-error-not-a-valid-personal-number", "");

            context.addMessage(liferayUtil.getPortletNamespace() + ":addGrantForm:personalNumber", message);
        }

    }

    public void deliveryDateListener() {
        LOGGER.info("deliveryDateListener(): add {} to grant {}",
                deliveryDate, grant);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = null;
//        try {
//            date = sdf.parse(deliveryDate);
//        } catch (ParseException e) {
//            LOGGER.warn("Exception. {}", e.getMessage());
//
//            return;
//        }

        grant.setDeliveryDate(deliveryDate);

        // Set grantFlow
        grantFlow = grantFlow.nextState();
    }

    public void grantTypeListener() {
        LOGGER.info("grantTypeListener()");

        if (GRANT_TYPE_OTHER.equals(grantType)) {
            // TODO: make address check against deliveryDate
            LOGGER.info("grantTypeListener(): addressCheck=");
        }

        // Set grantFlow
        if (GRANT_TYPE_AGE_0_TO_15.equals(grantType)) {
            grantFlow = grantFlow.nextState(AddGrantAction.AGE_0_TO_15);
            grantTypeLabel = "grant-type-0-15";
        } else if (GRANT_TYPE_AGE_0_TO_19.equals(grantType)) {
            grantFlow = grantFlow.nextState(AddGrantAction.AGE_0_TO_19);
            grantTypeLabel = "grant-type-0-19";
        } else {
            grantFlow = grantFlow.nextState(AddGrantAction.OTHER);
            grantTypeLabel = "grant-type-other";
        }
    }

    public void prescriptionDateListener() {
        LOGGER.info("prescriptionDateListener(): add {} to grant {}",
                prescriptionVO.getDate(), grant);

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

                prescriptionVO = new PrescriptionVO();

                //amountWithVat = null;
                break;
            case ENTER_DELIVERY_DATE:
                grantType = null;
                grantTypeLabel = null;

                prescriptionVO = new PrescriptionVO();

                //amountWithVat = null;
                break;
            case SELECT_GRANT_TYPE:
                grantType = null;
                grantTypeLabel = null;

                prescriptionVO = new PrescriptionVO();

                //amountWithVat = null;
                break;
            case ENTER_PRESCRIPTION_DATE:

                Date prescriptionDate = prescriptionVO.getDate();

                prescriptionVO = new PrescriptionVO();

                prescriptionVO.setDate(prescriptionDate);

                //amountWithVat = null;
                break;
            case ENTER_GRANT_STATE_OTHER_TYPE:

                prescriptionVO.setDate(null);
                prescriptionVO.setComment(null);
                prescriptionVO.setPrescriber(null);

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
        try {
            saveGrant();
        } catch(Exception e) {
            LOGGER.error(e.getMessage(), e);

            // TODO: notify user that there was an error.
        }


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

        LOGGER.info("CreateInvoiceAddGrantBackingBean - saveGrant - Invoice id: " + tempInvoiceId);

        // Insert beneficiary first.
        if (newBeneficiary) {
            try {
                beneficiaryService.create(beneficiary);
            } catch (NoIdentificationException e) {
                LOGGER.warn("Beneficiary didn't have a identificaiton. " +
                        "Serious error in this grantFlow.");

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

        // Connect Grant with prescriptionVO

        Prescription prescription = new Prescription();
        prescription.setDate(prescriptionVO.getDate());

        Diagnose diagnose;

        if(prescriptionVO.getType() != DiagnoseType.NONE) {
            LOGGER.info("saveGrant - this is DiagnoseType OTHER. Should save Comment, Prescriber and Diagnose.");

            // TODO: remove comment for line below when problem with comment is solved
            prescription.setComment(prescriptionVO.getComment());
            prescription.setPrescriber(prescriptionVO.getPrescriber());

            if (prescriptionVO.getType() == DiagnoseType.APHAKIA) {
                Aphakia aphakia = new Aphakia();
                aphakia.setLaterality(prescriptionVO.getLaterality());

                diagnose = aphakia;
            } else if (prescriptionVO.getType() == DiagnoseType.KERATOCONUS) {
                Keratoconus keratoconus = new Keratoconus();
                keratoconus.setLaterality(prescriptionVO.getLaterality());
                keratoconus.setVisualAcuityLeft(prescriptionVO.getVisualAcuityLeft());
                keratoconus.setVisualAcuityRight(prescriptionVO.getVisualAcuityRight());
                keratoconus.setNoGlasses(prescriptionVO.isNoGlasses());

                diagnose = keratoconus;
            } else if (prescriptionVO.getType() == DiagnoseType.SPECIAL) {
                Special special = new Special();
                special.setLaterality(prescriptionVO.getLaterality());
                special.setWeakEyeSight(prescriptionVO.isWeakEyeSight());

                diagnose = special;
            } else {
                throw new RuntimeException("Diagnose cannot be null.");
            }

        } else {
            diagnose = new None();
        }

        diagnoseService.create(diagnose);

        prescription.setDiagnose(diagnose);


        beneficiaryService.updateAddPrescription(userId, groupId, companyId, beneficiary, prescription);
        grant.setPrescription(prescription);

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

        grantFlow = AddGrantFlowState.ENTER_IDENTIFICATION.getState();

        LOGGER.info("Current state: {}.", grantFlow);

        long invoiceId = facesUtil.fetchId("invoiceId");
        invoice = invoiceService.findWithParts(invoiceId);

        newBeneficiary = false;

        //invoice = newInvoice;
        beneficiary = null;

        deliveryDate = null;
        grantType = null;
        grantTypeLabel = null;

        beneficiaryVO = new BeneficiaryVO();

        prescriptionVO = new PrescriptionVO();

        // TODO: if existing (already persited) grant - load from DB (if not already loaded)
        // TODO: If new grant and beneficiary has a type other diagnose registered before, then load this diagnose
        prescriptionVO.setType(DiagnoseType.NONE);
        prescriptionVO.setLaterality(VisualLaterality.NONE);

        // Todo: different types and conditions will affect the default number of amountWithVat
        amountWithVat = "800";

        tabUtil = new TabUtil(Arrays.asList("personal-number", "lma-number"), 0);

        Long grantId = facesUtil.fetchId("grantId");
        if(grantId != null) {
            System.out.println("--- Found GrantId -----");

            //TODO: add find with parts to grantService, so that call to beneficiaryService.findWithParts will not be necessary.
            grant = grantService.findWithParts(grantId);
            //grant = grantService.find(grantId);
            beneficiary = beneficiaryService.findWithParts(grant.getBeneficiary().getId());


            //number = beneficiary.getIdentification().getString();
            beneficiaryVO.setIdentificationNumber(
                    beneficiary.getIdentification().getString());

            deliveryDate = grant.getDeliveryDate();

            Prescription prescription = grant.getPrescription();

            // Temporary code - start
            if(prescription == null) {
                System.out.println("--- prescription IS null -----");
            } else {
                System.out.println("--- prescription is NOT null -----");
            }
            // Temporary code - end

            prescriptionVO.setDate(prescription.getDate());

            Diagnose diagnose = prescription.getDiagnose();
            DiagnoseType diagnoseType = diagnose.getType();

            if(diagnoseType != DiagnoseType.NONE) {

                // TODO: activate code below when comments work for prescription again
                prescriptionVO.setComment(prescription.getComment());
                prescriptionVO.setPrescriber(prescription.getPrescriber());
                prescriptionVO.setType(prescription.getDiagnose().getType());

                if(diagnoseType == DiagnoseType.APHAKIA) {
                    Aphakia aphakia = (Aphakia)diagnose;

                    System.out.println("--- DiagnoseType is Aphakia -----");


                    prescriptionVO.setLaterality(aphakia.getLaterality());
                } else if(diagnoseType == DiagnoseType.KERATOCONUS) {
                    Keratoconus keratoconus = (Keratoconus)diagnose;

                    System.out.println("--- DiagnoseType is Keratoconus -----");

                    prescriptionVO.setLaterality(keratoconus.getLaterality());
                    prescriptionVO.setNoGlasses(keratoconus.isNoGlasses());
                    prescriptionVO.setVisualAcuityLeft(keratoconus.getVisualAcuityLeft());
                    prescriptionVO.setVisualAcuityRight(keratoconus.getVisualAcuityRight());
                } else if(diagnoseType == DiagnoseType.SPECIAL) {
                    Special special = (Special)diagnose;

                    System.out.println("--- DiagnoseType is Special -----");

                    prescriptionVO.setLaterality(special.getLaterality());
                    prescriptionVO.setWeakEyeSight(special.isWeakEyeSight());
                } else {
                    // TODO: throw exception
                }

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

            //grantFlow = AddGrantFlowState.ENTER_IDENTIFICATION.getState();

            if(GRANT_TYPE_OTHER.equals(grantType)) {
                grantFlow = AddGrantFlowState.ENTER_ALL_DATA_AFTER_OTHER.getState();
            }
            else if(GRANT_TYPE_AGE_0_TO_19.equals(grantType) || GRANT_TYPE_AGE_0_TO_15.equals(grantType)) {
                grantFlow = AddGrantFlowState.ENTER_ALL_DATA_AFTER_AGE.getState();
            }

            //grantFlow = AddGrantFlowState.ENTER_AMOUNT_AFTER_AGE.getState();
            LOGGER.info("Current state: {}.", grantFlow);


        } else {
            grant = new Grant();
        }
    }

}
