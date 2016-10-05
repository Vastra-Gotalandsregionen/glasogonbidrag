package se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose;

import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;

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
    @Override
    public DiagnoseType getType() {
        return DiagnoseType.NONE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;

        return obj instanceof None;
    }
}
