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

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    /**
     * Default constructor
     */
    public Lma() {
    }

    public Lma(String number) {
        super(number);
    }

    public Lma(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public IdentificationType getType() {
        return IdentificationType.LMA;
    }

}
