package se.vgregion.service.glasogonbidrag.local.internal;

import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.service.glasogonbidrag.local.api.GrantAmountLookupService;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class GrantAmountLookupServiceImpl implements GrantAmountLookupService {

    private static final Date PRE_20160301;
    private static final Date PRE_20160620;
    static {
        Calendar cal20160301 = new GregorianCalendar(2016, Calendar.MARCH, 1);
        Calendar cal20160620 = new GregorianCalendar(2016, Calendar.JUNE, 20);
        PRE_20160301 = cal20160301.getTime();
        PRE_20160620 = cal20160620.getTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal grantableAmount(Diagnose diagnose,
                                      Date deliveryDate,
                                      Date birthDate) {
        BigDecimal amount = new BigDecimal("0");

        switch (diagnose.getType()) {
            case APHAKIA:
            case SPECIAL:
                amount = aphakiaAndSpecialAmount(deliveryDate);
                break;
            case KERATOCONUS:
                amount = keratoconusAmount(deliveryDate);
                break;
            case NONE:
                amount = childrenAmount(deliveryDate, birthDate);
                break;
        }

        return amount;
    }

    private BigDecimal aphakiaAndSpecialAmount(Date deliveryDate) {
        return deliveryDate.before(PRE_20160620) ?
                new BigDecimal("2000"): // Amount after 2016-06-20
                new BigDecimal("2400"); // Amount after 2016-06-20
    }

    private BigDecimal keratoconusAmount(Date deliveryDate) {
        return deliveryDate.before(PRE_20160620) ?
                new BigDecimal("2400"): // Amount after 2016-06-20
                new BigDecimal("3000"); // Amount after 2016-06-20
    }

    private BigDecimal childrenAmount(Date deliveryDate, Date birthday) {
        Calendar cal = new GregorianCalendar();
        if (deliveryDate.before(PRE_20160301)) {
            cal.setTime(birthday);
            cal.add(Calendar.YEAR, 16);
            cal.add(Calendar.MONTH, 6);

            Date birthday16 = cal.getTime();

            if (!deliveryDate.before(birthday)
                    && !deliveryDate.after(birthday16)) {
                return new BigDecimal("1000");
            }
        } else if (deliveryDate.before(PRE_20160620)) {
            cal.setTime(birthday);
            cal.add(Calendar.YEAR, 16);
            cal.add(Calendar.MONTH, 6);

            Date birthday16 = cal.getTime();

            cal.setTime(birthday);
            cal.add(Calendar.YEAR, 20);
            cal.add(Calendar.MONTH, 6);

            Date birthday20 = cal.getTime();

            if (!deliveryDate.before(birthday)
                    && !deliveryDate.after(birthday16)) {
                return new BigDecimal("1000");
            } else if (deliveryDate.after(birthday16)
                    && !deliveryDate.after(birthday20)) {
                return new BigDecimal("800");
            }
        } else {
            cal.setTime(birthday);
            cal.add(Calendar.YEAR, 20);
            cal.add(Calendar.MONTH, 6);

            Date birthday20 = cal.getTime();

            if (!deliveryDate.before(birthday)
                    && !deliveryDate.after(birthday20)) {
                return new BigDecimal("1600");
            }
        }

        return new BigDecimal("0");
    }
}
