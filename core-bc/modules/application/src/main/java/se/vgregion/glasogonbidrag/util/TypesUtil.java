package se.vgregion.glasogonbidrag.util;

import se.vgregion.glasogonbidrag.model.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.LMAIdentification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Migration;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.PersonalIdentification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.ReserveIdentification;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by martlin on 2016/06/27.
 */
public final class TypesUtil {
    private TypesUtil() {

    }

    public static Migration migration(String date) {
        Calendar cal = Calendar.getInstance();

        Migration m = new Migration();
        m.setDate(cal.getTime());

        return m;
    }

    public static Invoice invoice(String verNr, String invNr) {
        Invoice invoice = new Invoice();
        invoice.setVerificationNumber(verNr);
        invoice.setInvoiceNumber(invNr);

        return invoice;
    }

    public static Identification identification(String number) {
        IdentificationType type = IdentificationUtil.detect(number);

        Identification id;
        if (type == IdentificationType.VALID) {
            id = new PersonalIdentification(number);
        } else {
            id = new ReserveIdentification(number);
        }

        return id;
    }

    public static Beneficiary beneficiary(Identification id,
                                          String firstName,
                                          String lastName) {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setIdentification(id);
        beneficiary.setFirstName(firstName);
        beneficiary.setLastName(lastName);

        return beneficiary;
    }

    public static Grant grant(Date delivery,
                              Date prescription,
                              String amount) {
        Grant g = new Grant();
        g.setDeliveryDate(delivery);
        try {
            BigDecimal a = new BigDecimal(amount);
            g.setAmountIncludingVatAsKrona(a);
        } catch (NumberFormatException ignored) {
            System.out.println("TypesUtil.grant: " + amount.length());
        }
        return g;
    }
}
