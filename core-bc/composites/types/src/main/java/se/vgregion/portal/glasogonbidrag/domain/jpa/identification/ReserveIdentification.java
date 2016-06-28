package se.vgregion.portal.glasogonbidrag.domain.jpa.identification;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("r")
@Table(name = "vgr_glasogonbidrag_reserve_identification")
public class ReserveIdentification extends Identification {

    @Column(name = "reserve_number", unique = true, nullable = false)
    private String number;

    /**
     * Default constructor
     */
    public ReserveIdentification() {

    }

    public ReserveIdentification(String number) {
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
        return getNumber();
    }

}
