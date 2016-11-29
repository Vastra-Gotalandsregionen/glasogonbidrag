package se.vgregion.portal.glasogonbidrag.domain.jpa.identification;

import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("o")
@Table(name = "vgr_glasogonbidrag_identification_other")
public class Other extends Identification {

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    /**
     * Default constructor
     */
    public Other() {
    }

    public Other(String number, Date birthDate) {
        super(number);
        this.birthDate = birthDate;
    }

    public Other(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public IdentificationType getType() {
        return IdentificationType.OTHER;
    }

}
