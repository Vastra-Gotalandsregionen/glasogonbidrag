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

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    /**
     * Default constructor
     */
    public Reserve() {
    }

    public Reserve(String number, Date birthDate) {
        super(number);
        this.birthDate = birthDate;
    }

    public Reserve(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public IdentificationType getType() {
        return IdentificationType.RESERVE;
    }

}
