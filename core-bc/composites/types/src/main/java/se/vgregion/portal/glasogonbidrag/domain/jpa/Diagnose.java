package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "diagnose_type")
@Table(name = "vgr_glasogonbidrag_diagnose")
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

    public abstract Type getDiagnoseType();

    @Override
    public String toString() {
        return getDiagnoseType().toString();
    }

    public enum Type {
        APHAKIA,
        KERATOCONUS,
        SPECIAL,
        NONE
    }
}
