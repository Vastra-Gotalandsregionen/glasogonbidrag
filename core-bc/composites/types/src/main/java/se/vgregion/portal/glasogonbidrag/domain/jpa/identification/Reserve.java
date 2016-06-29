package se.vgregion.portal.glasogonbidrag.domain.jpa.identification;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.IdentificationType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("r")
@Table(name = "vgr_glasogonbidrag_identification_reserve")
public class Reserve extends Identification {

    @Column(name = "reserve_number", unique = true, nullable = false)
    private String number;

    /**
     * Default constructor
     */
    public Reserve() {

    }

    public Reserve(String number) {
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

    @Override
    public IdentificationType getType() {
        return IdentificationType.RESERVE;
    }

}
