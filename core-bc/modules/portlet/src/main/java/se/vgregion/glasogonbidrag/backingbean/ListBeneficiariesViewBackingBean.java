package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.service.glasogonbidrag.api.data.BeneficiaryRepository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "listBeneficiariesViewBackingBean")
@Scope(value = "request")
public class ListBeneficiariesViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ListBeneficiariesViewBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    private List<Beneficiary> beneficiaries;

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    @PostConstruct
    protected void init() {
        beneficiaries = beneficiaryRepository.findAll();
    }
}
