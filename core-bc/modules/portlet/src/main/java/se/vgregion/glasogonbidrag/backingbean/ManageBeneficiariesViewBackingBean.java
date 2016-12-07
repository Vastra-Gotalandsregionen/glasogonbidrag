package se.vgregion.glasogonbidrag.backingbean;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.datamodel.BeneficiaryLazyDataModel;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.service.glasogonbidrag.domain.api.service.BeneficiaryService;
import se.vgregion.portal.glasogonbidrag.domain.dto.BeneficiaryDTO;

import javax.annotation.PostConstruct;

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
    private BeneficiaryService beneficiaryService;

    private LazyDataModel<BeneficiaryDTO> lazyDataModel;
    private BeneficiaryDTO selectedBeneficiary;

    public LazyDataModel<BeneficiaryDTO> getLazyDataModel() {
        return lazyDataModel;
    }

    public Beneficiary getSelectedBeneficiary() {
        if (selectedBeneficiary == null) {
            return null;
        }
        return selectedBeneficiary.getBeneficiary();
    }

    public void onRowSelect(SelectEvent event) {
        selectedBeneficiary = (BeneficiaryDTO) event.getObject();
    }

    public void onRowDeselect(UnselectEvent event) {
        selectedBeneficiary = null;
    }

    @PostConstruct
    protected void init() {
        lazyDataModel = new BeneficiaryLazyDataModel(beneficiaryService);
        selectedBeneficiary = null;
    }
}
