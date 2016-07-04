package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Lma;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Reserve;
import se.vgregion.service.glasogonbidrag.api.data.BeneficiaryRepository;
import se.vgregion.service.glasogonbidrag.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.exception.NoIdentificationException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "dbViewBeneficiaryBackingBean")
@Scope(value = "session")
public class DBViewBeneficiaryBackingBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Logger LOGGER =
            LoggerFactory.getLogger(DBViewBeneficiaryBackingBean.class);

    @Autowired
    private BeneficiaryRepository repository;

    @Autowired
    private BeneficiaryService service;

    @Autowired
    private FacesUtil util;

    // Instance variables

    private boolean editBeneficiary;

    private List<Beneficiary> beneficiaries;
    private Beneficiary beneficiary;

    private IdentificationType type;
    private Identification identification;

    private Prescription prescription;

    // Getter and setters


    public boolean isEditBeneficiary() {
        return editBeneficiary;
    }

    public void setEditBeneficiary(boolean editBeneficiary) {
        this.editBeneficiary = editBeneficiary;
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public IdentificationType getType() {
        return type;
    }

    public void setType(IdentificationType type) {
        this.type = type;
    }

    public Identification getIdentification() {
        return identification;
    }

    public Personal getPersonalIdentification() {
        if (type == IdentificationType.PERSONAL) {
            return (Personal) identification;
        } else {
            return null;
        }
    }

    public Reserve getReserveIdentification() {
        if (type == IdentificationType.RESERVE) {
            return (Reserve) identification;
        } else {
            return null;
        }
    }

    public Lma getLmaIdentification() {
        if (type == IdentificationType.LMA) {
            return (Lma) identification;
        } else {
            return null;
        }
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    // Actions

    public void doRefresh() {
        LOGGER.info("Refresh list.");

        loadBeneficiaryList();
    }

    public String doCreate() {
        LOGGER.info("Load empty beneficiary.");

        loadBeneficiary();

        return "add_beneficiary?faces-redirect=true";
    }

    public String doSave() {
        beneficiary.setIdentification(identification);

        try {
            service.create(beneficiary);
        } catch (NoIdentificationException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null,
                    new FacesMessage(
                            "You need to set an identification number."));

            return null;
        }

        loadBeneficiaryList();

        return "list_beneficiaries?faces-redirect=true";
    }

    public void doDelete() {
        Long id = util.fetchId("beneficiaryId");
        if (id != null) {
            service.delete(id);
        }

        loadBeneficiaryList();
    }

    public String doCancel() {
        return "list_beneficiaries?faces-redirect=true";
    }

    // Actions for Prescription

    public String doSavePrescription() {
        ThemeDisplay display = util.getThemeDisplay();
        long userId = display.getUserId();
        long groupId = display.getScopeGroupId();
        long companyId = display.getCompanyId();

        service.updateAddPrescription(
                userId, groupId, companyId,
                beneficiary, prescription);

        return "view_beneficiary?faces-redirect=true" +
                "&beneficiaryId=" + beneficiary.getId();
    }

    // Listeners

    public void identificationTypeListener() {
        LOGGER.info("Changed identification");
        switch (type) {
            case PERSONAL: identification = new Personal(); break;
            case RESERVE: identification = new Reserve(); break;
            case LMA: identification = new Lma(); break;
            case OTHER: identification = null; break;
        }
    }

    // Value converters

    // Select Item converters

    public SelectItem[] getIdentificationTypes() {
        IdentificationType[] types = IdentificationType.values();
        SelectItem[] items = new SelectItem[types.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(types[i], types[i].toString());
        }

        return items;
    }

    // Initializer

    @PostConstruct
    protected void init() {
        loadBeneficiaryList();
        loadBeneficiary();
    }

    private void loadBeneficiaryList() {
        beneficiaries = repository.findAll();
    }

    private void loadBeneficiary() {
        type = IdentificationType.NONE;
        beneficiary = new Beneficiary();
        editBeneficiary = false;

        try {
            Long id = util.fetchId("beneficiaryId");
            if (id != null) {
                beneficiary = repository.find(id);
                editBeneficiary = true;
                prescription = new Prescription();
            }
        } catch (Exception e) {
            LOGGER.warn("Got exception.");
        }
    }
}
