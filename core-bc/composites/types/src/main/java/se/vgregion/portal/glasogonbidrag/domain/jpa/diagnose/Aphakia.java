package se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.value.PrescriptionValueObject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("a")
@Table(name = "vgr_glasogonbidrag_diagnose_aphakia")
public class Aphakia extends Diagnose {

    private VisualLaterality laterality;

    public Aphakia() {
        this(VisualLaterality.NONE);
    }

    public Aphakia(VisualLaterality laterality) {
        this.laterality = laterality;
    }

    // Helper method for creating Keratoconus object from value object.
    public Aphakia(PrescriptionValueObject value) {
        this(value.getLaterality());
    }

    public VisualLaterality getLaterality() {
        return laterality;
    }

    public void setLaterality(VisualLaterality laterality) {
        this.laterality = laterality;
    }

    @Override
    public DiagnoseType getType() {
        return DiagnoseType.APHAKIA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aphakia aphakia = (Aphakia) o;

        if (laterality != aphakia.laterality) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return laterality.hashCode();
    }

    @Override
    public String toString() {
        return "Aphakia{" +
                "laterality=" + laterality +
                '}';
    }

    @Override
    public PrescriptionValueObject getValueObject() {
        PrescriptionValueObject vo = new PrescriptionValueObject();
        vo.setType(DiagnoseType.APHAKIA);
        vo.setLaterality(laterality);

        return vo;
    }
}
