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
@Table(name = "identification_protected")
public class Protected extends Identification {

    @Transient
    private final DateCalculationUtil dateUtil = new DateCalculationUtil();

    /**
     * Default constructor
     */
    public Protected() {
    }

    public Protected(String number) {
        super(number);
    }

    @Override
    public Date getBirthDate() {
        String date = dateUtil.extractDateFromPersonalNumbers(getNumber());
        return dateUtil.dateFromString(date);
    }

    @Override
    public IdentificationType getType() {
        return IdentificationType.PROTECTED;
    }
}
