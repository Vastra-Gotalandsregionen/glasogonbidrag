package se.vgregion.portal.glasogonbidrag.domain.jpa;

import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;

import javax.persistence.Column;
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
import java.util.Date;

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

    @Column(name = "ident_number", unique = true, nullable = false)
    private String number;

    public Identification() {

    }

    public Identification(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public abstract IdentificationType getType();

    public abstract Date getBirthDate();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identification that = (Identification) o;

        if (!number.equals(that.number)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public String toString() {
        return this.number;
    }
}
