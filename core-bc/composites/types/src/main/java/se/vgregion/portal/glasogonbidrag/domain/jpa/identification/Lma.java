package se.vgregion.portal.glasogonbidrag.domain.jpa.identification;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@DiscriminatorValue("l")
@Table(name = "vgr_glasogonbidrag_identification_lma")
public class Lma extends Identification {

    @Column(unique = true, nullable = false)
    private String number;

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    /**
     * Default constructor
     */
    public Lma() {
    }

    public Lma(String number, Date birthDate) {
        this.number = number;
        this.birthDate = birthDate;
    }

    public Lma(String number) {
        this.number = number;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String getString() {
        return number;
    }

    @Override
    public IdentificationType getType() {
        return IdentificationType.LMA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Lma that = (Lma) o;

        return number != null ? number.equals(that.number) : that.number == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }

}
