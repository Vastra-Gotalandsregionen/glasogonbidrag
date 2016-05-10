package se.vgregion.glasogonbidrag.backingbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.service.glasogonbidrag.data.BeneficiaryRepository;

import javax.annotation.PostConstruct;

@Controller("viewBeneficiaryBean")
@Scope(value = "request")
public class ViewBeneficiaryBackingBean {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private FacesUtil util;

    private Beneficiary beneficiary;

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    @PostConstruct
    protected void init() {
        System.out.println("ViewBeneficiaryBackingBean - init()");

        Long id = util.fetchId("beneficiaryId");

        beneficiary = beneficiaryRepository.findWithParts(id);
    }
}
