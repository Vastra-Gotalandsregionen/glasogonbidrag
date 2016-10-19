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

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("r")
@Table(name = "vgr_glasogonbidrag_identification_reserve")
public class Reserve extends Identification {

    @Column(name = "reserve_number", unique = true, nullable = false)
    private String number;

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    /**
     * Default constructor
     */
    public Reserve() {
    }

    public Reserve(String number, Date birthDate) {
        this.number = number;
        this.birthDate = birthDate;
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

    @Override
    public String getString() {
        return getNumber();
    }

    @Override
    public IdentificationType getType() {
        return IdentificationType.RESERVE;
    }

}
