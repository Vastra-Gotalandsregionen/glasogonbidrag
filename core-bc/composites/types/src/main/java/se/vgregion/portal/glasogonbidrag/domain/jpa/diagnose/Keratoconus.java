package se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("k")
@Table(name = "vgr_glasogonbidrag_diagnose_keratoconus")
public class Keratoconus extends Diagnose {

    private VisualLaterality laterality;
    private float visualAcuityRight;
    private float visualAcuityLeft;
    private boolean noGlasses;

    public VisualLaterality getLaterality() {
        return laterality;
    }

    public void setLaterality(VisualLaterality laterality) {
        this.laterality = laterality;
    }

    public float getVisualAcuityLeft() {
        return visualAcuityLeft;
    }

    public void setVisualAcuityLeft(float visualAcuityLeft) {
        this.visualAcuityLeft = visualAcuityLeft;
    }

    public float getVisualAcuityRight() {
        return visualAcuityRight;
    }

    public void setVisualAcuityRight(float visualAcuityRight) {
        this.visualAcuityRight = visualAcuityRight;
    }

    public boolean isNoGlasses() {
        return noGlasses;
    }

    public void setNoGlasses(boolean noGlasses) {
        this.noGlasses = noGlasses;
    }

    @Override
    public DiagnoseType getDiagnoseType() {
        return DiagnoseType.KERATOCONUS;
    }
}
