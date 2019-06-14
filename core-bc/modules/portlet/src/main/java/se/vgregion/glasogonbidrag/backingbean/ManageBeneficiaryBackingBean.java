package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.SexType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Keratoconus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Special;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;
import se.vgregion.service.glasogonbidrag.domain.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.domain.api.service.PrescriptionService;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;
import se.vgregion.service.glasogonbidrag.integration.api.BeneficiaryLookupService;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryTransport;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.Locale;

@Component(value = "manageBeneficiaryBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ManageBeneficiaryBackingBean {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ManageBeneficiaryBackingBean.class);

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private BeneficiaryLookupService beneficiaryLookupService;

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private MessageSource messageSource;

    private String diagnoseName;
    private boolean newPrescription;
    private boolean fetchedData;

    private String fullName;
    private SexType sex;
    private Date birthYear;
    private Date prescriptionDate;
    private String county;
    private String municipality;
    private String prescriber;
    private String notes;

    private String personalNumber;

    private Personal personalIdentification;

    private Aphakia diagnoseAphakia;
    private Keratoconus diagnoseKeratoconus;
    private Special diagnoseSpecial;
    private None diagnoseNone;

    private DiagnoseType diagnoseType;

    private Beneficiary beneficiary;
    private Prescription prescription;

    // Getter only fields

    public String getDiagnoseName() {
        return diagnoseName;
    }

    public String getFullName() {
        return fullName;
    }

    public SexType getSex() {
        return sex;
    }

    public Date getBirthYear() {
        return birthYear;
    }

    public String getCounty() {
        return county;
    }

    public String getMunicipality() {
        return municipality;
    }

    public boolean isNewPrescription() {
        return newPrescription;
    }

    public boolean isFetchedData() {
        return fetchedData;
    }

    // Getters and Setters


    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public DiagnoseType getDiagnoseType() {
        return diagnoseType;
    }

    public void setDiagnoseType(DiagnoseType diagnoseType) {
        this.diagnoseType = diagnoseType;

        this.diagnoseName = diagnoseType.toString();
    }

    public Aphakia getDiagnoseAphakia() {
        return diagnoseAphakia;
    }

    public Keratoconus getDiagnoseKeratoconus() {
        return diagnoseKeratoconus;
    }

    public Special getDiagnoseSpecial() {
        return diagnoseSpecial;
    }

    public None getDiagnoseNone() {
        return diagnoseNone;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(String prescriber) {
        this.prescriber = prescriber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Listeners

    public void lookupListener() {
        Locale locale = facesUtil.getLocale();

        BeneficiaryTransport result;

        try {
            result = this.beneficiaryLookupService
                    .fetchNameAndAddress(personalNumber, new Date());
        } catch (Exception e) {
            String localizedMessage = messageSource
                    .getMessage("connection-to-population-service-failed",
                            new Object[0], locale);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            localizedMessage, localizedMessage);
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);
            return;
        }

        if (result == null) {
            String localizedMessage = messageSource
                    .getMessage("no-result-from-population-service",
                            new Object[0], locale);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            localizedMessage, localizedMessage);
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);
            return;
        }

        this.personalIdentification = new Personal(personalNumber);

        this.fullName = result.getName().getFullName();
        this.sex = result.getName().getSex();
        this.birthYear = personalIdentification.getBirthDate();

        this.county = result.getArea().getCounty();
        this.municipality = result.getArea().getMunicipality();

        this.fetchedData = true;
    }

    public void diagnoseListener() {
        // Don't do anything.
    }

    public void changeVisualLateralityListener() {
        // Don't do anything.
    }

    // Actions

    public String saveBeneficiary() {
        Locale locale = facesUtil.getLocale();

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();

        long userId = themeDisplay.getUserId();
        long groupId = themeDisplay.getScopeGroupId();
        long companyId = themeDisplay.getCompanyId();

        Date currentDate = new Date();

        beneficiary = new Beneficiary();
        beneficiary.setIdentification(personalIdentification);

        beneficiary.setFullName(fullName);
        beneficiary.setSex(sex);

        Prescription newPrescription = new Prescription();
        newPrescription.setDate(prescriptionDate);
        newPrescription.setPrescriber(prescriber);
        newPrescription.setComment(notes);
        newPrescription.setBeneficiary(beneficiary);

        switch (diagnoseType) {
            case APHAKIA:
                newPrescription.setDiagnose(diagnoseAphakia);
                break;
            case KERATOCONUS:
                newPrescription.setDiagnose(diagnoseKeratoconus);
                break;
            case SPECIAL:
                newPrescription.setDiagnose(diagnoseSpecial);
                break;
            case NONE:
                String localizedMessage = messageSource.getMessage(
                        "new-beneficiary-glasses-diagnose-not-supported",
                        new Object[0], locale);

                FacesMessage message =
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                localizedMessage, localizedMessage);
                FacesContext.getCurrentInstance()
                        .addMessage(null, message);

                break;
        }

        try {
            if (this.newPrescription) {
                beneficiaryService.create(
                        beneficiary,
                        newPrescription,
                        userId, groupId, companyId,
                        currentDate);
            } else {
                beneficiaryService.update(beneficiary);
            }
        } catch (NoIdentificationException e) {
            String localizedMessage = messageSource.getMessage(
                    "new-beneficiary-no-identification-set",
                    new Object[0], locale);

            FacesMessage message =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            localizedMessage, localizedMessage);
            FacesContext.getCurrentInstance()
                    .addMessage(null, message);
        }

        return "view?faces-redirect=true";
    }

    // Initializer

    @PostConstruct
    public void init() {
        //FUTURE: Support editing.
        long beneficiaryId = facesUtil.fetchId("beneficiaryId");

        if (beneficiaryId > 0) {
            beneficiary = beneficiaryService.find(beneficiaryId);
            prescription =
                    prescriptionService.findLatest(beneficiary);
            newPrescription = false;
        } else {
            newPrescription = true;
        }

        fetchedData = beneficiary != null;

        if (fetchedData) {
            fullName = beneficiary.getFullName();
            sex = beneficiary.getSex();
            //birthYear = beneficiary.getBirthYear();
            county = prescription.getGrant().getCounty();
            municipality = prescription.getGrant().getMunicipality();
        } else {
            fullName = "";
            sex = SexType.UNKNOWN;
            birthYear = new Date();
            county = "";
            municipality = "";
        }

        this.diagnoseAphakia = new Aphakia();
        this.diagnoseKeratoconus = new Keratoconus();
        this.diagnoseSpecial = new Special();
        this.diagnoseNone = new None();
    }

}
