package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.viewobject.PrescriptionVO;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "dbViewTestFieldBackingBean")
@Scope(value = "session")
public class DBViewTestFieldBackingBean {

    private final Logger LOGGER =
            LoggerFactory.getLogger(DBViewTestFieldBackingBean.class);

    private PrescriptionVO prescriptionVO;

    public PrescriptionVO getPrescriptionVO() {
        return prescriptionVO;
    }

    public void setPrescriptionVO(PrescriptionVO prescriptionVO) {
        this.prescriptionVO = prescriptionVO;
    }

    public void diagnoseTypeListener() {
        LOGGER.info("Changed diagnose type");
    }

    public void doAction() {
        LOGGER.info("We've got the value: {}",
                prescriptionVO.getType().toString());
    }

    public SelectItem[] getDiagnoseTypeItems() {
        SelectItem[] items = new SelectItem[3];

        items[0] = new SelectItem(DiagnoseType.APHAKIA, "afaki");
        items[1] = new SelectItem(DiagnoseType.KERATOCONUS, "keratokonus");
        items[2] = new SelectItem(DiagnoseType.SPECIAL, "spec./synsvag");

        return items;
    }

    @PostConstruct
    protected void init() {
        prescriptionVO = new PrescriptionVO();

        // Load from database
        prescriptionVO.setType(DiagnoseType.NONE);
        prescriptionVO.setLaterality(VisualLaterality.NONE);
    }
}
