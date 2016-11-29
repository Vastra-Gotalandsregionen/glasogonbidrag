package se.vgregion.portal.glasogonbidrag.domain.jpa;

import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.value.PrescriptionValueObject;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "diagnose_type")
@Table(name = "diagnose")
public abstract class Diagnose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract DiagnoseType getType();

    @Override
    public String toString() {
        return getType().toString();
    }

    // Value object helper
    public abstract PrescriptionValueObject getValueObject();


    private static final Map<DiagnoseType, String>
            DISCRIMINATOR_MAP = new HashMap<>();
    static {
        DISCRIMINATOR_MAP.put(DiagnoseType.APHAKIA, "a");
        DISCRIMINATOR_MAP.put(DiagnoseType.KERATOCONUS, "k");
        DISCRIMINATOR_MAP.put(DiagnoseType.NONE, "n");
        DISCRIMINATOR_MAP.put(DiagnoseType.SPECIAL, "s");
    }

    public static Map<DiagnoseType, String> getDiscriminatorValueMap() {
        return DISCRIMINATOR_MAP;
    }
}
