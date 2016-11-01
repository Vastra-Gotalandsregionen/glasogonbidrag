package se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.value.DiagnoseValueObject;

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

    public Keratoconus() {
        this(VisualLaterality.NONE, 0.0f, 0.0f, false);
    }

    public Keratoconus(VisualLaterality laterality,
                       float visualAcuityRight,
                       float visualAcuityLeft,
                       boolean noGlasses) {
        this.laterality = laterality;
        this.visualAcuityRight = visualAcuityRight;
        this.visualAcuityLeft = visualAcuityLeft;
        this.noGlasses = noGlasses;
    }

    // Helper method for creating Keratoconus object from value object.
    public Keratoconus(DiagnoseValueObject value) {
        this(value.getLaterality(),
                value.getVisualAcuityRight(),
                value.getVisualAcuityLeft(),
                value.isNoGlasses());
    }

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
    public DiagnoseType getType() {
        return DiagnoseType.KERATOCONUS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Keratoconus that = (Keratoconus) o;

        if (noGlasses != that.noGlasses) return false;
        if (Float.compare(that.visualAcuityLeft, visualAcuityLeft) != 0)
            return false;
        if (Float.compare(that.visualAcuityRight, visualAcuityRight) != 0)
            return false;
        if (laterality != that.laterality) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = laterality.hashCode();
        result = 31 * result + (visualAcuityRight != +0.0f ? Float.floatToIntBits(visualAcuityRight) : 0);
        result = 31 * result + (visualAcuityLeft != +0.0f ? Float.floatToIntBits(visualAcuityLeft) : 0);
        result = 31 * result + (noGlasses ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Keratoconus{" +
                "laterality=" + laterality +
                ", visualAcuityRight=" + visualAcuityRight +
                ", visualAcuityLeft=" + visualAcuityLeft +
                ", noGlasses=" + noGlasses +
                '}';
    }
}
