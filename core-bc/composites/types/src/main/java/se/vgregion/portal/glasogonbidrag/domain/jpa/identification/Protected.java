package se.vgregion.portal.glasogonbidrag.domain.jpa.identification;

import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.internal.DateCalculationUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("h")
@Table(name = "vgr_glasogonbidrag_identification_protected")
public class Protected extends Identification {

    @Column(name = "pid_number", unique = true, nullable = false)
    private String number;

    @Transient
    private final DateCalculationUtil dateUtil = new DateCalculationUtil();

    /**
     * Default constructor
     */
    public Protected() {
    }

    public Protected(String number) {
        this.number = number;
    }

    @Override
    public Date getBirthDate() {
        String date = dateUtil.extractDateFromPersonalNumbers(number);
        return dateUtil.dateFromString(date);
    }

    @Override
    public String getString() {
        return number;
    }

    @Override
    public IdentificationType getType() {
        return IdentificationType.PROTECTED;
    }
}
