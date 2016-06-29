package se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.VisualLaterality;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("s")
@Table(name = "vgr_glasogonbidrag_diagnose_special")
public class Special extends Diagnose {

    private VisualLaterality laterality;
    private boolean weakEyeSight;

    public VisualLaterality getLaterality() {
        return laterality;
    }

    public void setLaterality(VisualLaterality laterality) {
        this.laterality = laterality;
    }

    public boolean isWeakEyeSight() {
        return weakEyeSight;
    }

    public void setWeakEyeSight(boolean weakEyeSight) {
        this.weakEyeSight = weakEyeSight;
    }

    @Override
    public Diagnose.Type getDiagnoseType() {
        return Type.SPECIAL;
    }
}
