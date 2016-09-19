package se.vgregion.glasogonbidrag.backingbean;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.service.glasogonbidrag.domain.api.data.BeneficiaryRepository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "manageBeneficiariesViewBackingBean")
@Scope(value = "request")
public class ManageBeneficiariesViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ManageBeneficiariesViewBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    private List<Beneficiary> beneficiaries;

    private Beneficiary selectedBeneficiary;

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public Beneficiary getSelectedBeneficiary() {
        return selectedBeneficiary;
    }

    public void onRowSelect(SelectEvent event) {
        selectedBeneficiary = (Beneficiary) event.getObject();
    }

    public void onRowUnselect(UnselectEvent event) {
        selectedBeneficiary = null;
    }

    @PostConstruct
    protected void init() {
        beneficiaries = beneficiaryRepository.findAllWithParts();
        selectedBeneficiary = null;
    }
}
