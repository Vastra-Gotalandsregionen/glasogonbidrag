package se.vgregion.portal.glasogonbidrag.domain.jpa.identification;

import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@DiscriminatorValue("h")
@Table(name = "vgr_glasogonbidrag_identification_protected")
public class Protected extends Identification {

    @Column(name = "magic_number", unique = true, nullable = false)
    private String magicNumber;

    @Override
    public String getString() {
        return "XXXXXX-XXXX";
    }

    @Override
    public IdentificationType getType() {
        return IdentificationType.PROTECTED;
    }
}
