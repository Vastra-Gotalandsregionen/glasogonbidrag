package se.vgregion.portal.glasogonbidrag.domain.jpa.identification;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("p")
@Table(name = "vgr_glasogonbidrag_identification_personal")
public class Personal extends Identification {

    @Column(name = "pid_number", unique = true, nullable = false)
    private String number;

    // Default constructor
    public Personal() {

    }

    public Personal(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getString() {
        return number;
    }

    @Override
    public IdentificationType getType() {
        return IdentificationType.PERSONAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Personal that = (Personal) o;

        return number != null ? number.equals(that.number) : that.number == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }

}
