package se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.value.PrescriptionValueObject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("s")
@Table(name = "diagnose_special")
public class Special extends Diagnose {

    @Enumerated(EnumType.STRING)
    private VisualLaterality laterality;
    private boolean weakEyeSight;

    public Special() {
        this(VisualLaterality.NONE, false);
    }

    public Special(VisualLaterality laterality, boolean weakEyeSight) {
        this.laterality = laterality;
        this.weakEyeSight = weakEyeSight;
    }

    // Helper method for creating Keratoconus object from value object.
    public Special(PrescriptionValueObject value) {
        this(value.getLaterality(), value.isWeakEyeSight());
    }

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
    public DiagnoseType getType() {
        return DiagnoseType.SPECIAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Special special = (Special) o;

        if (weakEyeSight != special.weakEyeSight) return false;
        if (laterality != special.laterality) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = laterality.hashCode();
        result = 31 * result + (weakEyeSight ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Special{" +
                "laterality=" + laterality +
                ", weakEyeSight=" + weakEyeSight +
                '}';
    }

    @Override
    public PrescriptionValueObject getValueObject() {
        PrescriptionValueObject vo = new PrescriptionValueObject();
        vo.setType(DiagnoseType.SPECIAL);
        vo.setLaterality(laterality);
        vo.setWeakEyeSight(weakEyeSight);

        return vo;
    }
}
