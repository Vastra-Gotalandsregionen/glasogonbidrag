package se.vgregion.portal.glasogonbidrag.domain.jpa;

import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "identity_type")
@Table(name = "vgr_glasogonbidrag_identification")
@NamedQueries({
        @NamedQuery(
                name = "glasogonbidrag.identification.findByPersonalNumber",
                query = "SELECT i FROM Personal i " +
                        "WHERE i.number = :number"),
        @NamedQuery(
                name = "glasogonbidrag.identification.findByReserveNumber",
                query = "SELECT i FROM Reserve i " +
                        "WHERE i.number = :number"),
        @NamedQuery(
                name = "glasogonbidrag.identification.findByLMANumber",
                query = "SELECT i FROM Lma i " +
                        "WHERE i.number = :number")
})
public abstract class Identification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract String getString();

    public abstract IdentificationType getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identification that = (Identification) o;

        return getString() != null ?
                getString().equals(that.getString()) :
                that.getString() == null;
    }

    @Override
    public int hashCode() {
        return getString() != null ? getString().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getString();
    }
}
