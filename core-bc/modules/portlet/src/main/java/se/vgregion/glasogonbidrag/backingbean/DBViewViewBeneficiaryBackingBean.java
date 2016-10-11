package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.service.glasogonbidrag.domain.api.service.BeneficiaryService;

import javax.annotation.PostConstruct;

@Controller("viewBeneficiaryBean")
@Scope(value = "request")
public class DBViewViewBeneficiaryBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DBViewViewBeneficiaryBackingBean.class);

    @Autowired
    private BeneficiaryService beneficiaryService;

    @Autowired
    private FacesUtil util;

    private Beneficiary beneficiary;

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    @PostConstruct
    protected void init() {
        LOGGER.info("DBViewViewBeneficiaryBackingBean - init()");

        Long id = util.fetchId("beneficiaryId");

        beneficiary = beneficiaryService.findWithParts(id);
    }
}
