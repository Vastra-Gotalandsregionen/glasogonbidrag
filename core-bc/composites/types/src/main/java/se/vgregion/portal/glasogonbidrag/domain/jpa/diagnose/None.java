package se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose;

import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.value.DiagnoseValueObject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("n")
@Table(name = "vgr_glasogonbidrag_diagnose_none")
public class None extends Diagnose {

    public None() {

    }

    // Helper method for creating Keratoconus object from value object.
    @SuppressWarnings("unused")
    public None(DiagnoseValueObject ignore) {
        this();
    }

    @Override
    public DiagnoseType getType() {
        return DiagnoseType.NONE;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;

        return obj instanceof None;
    }

    @Override
    public String toString() {
        return "None{}";
    }
}
