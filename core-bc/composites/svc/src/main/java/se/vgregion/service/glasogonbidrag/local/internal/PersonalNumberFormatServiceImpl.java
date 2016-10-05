package se.vgregion.service.glasogonbidrag.local.internal;

import org.springframework.stereotype.Service;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberFormatService;
import se.vgregion.service.glasogonbidrag.local.exception.YearOutOfBoundException;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class PersonalNumberFormatServiceImpl
        implements PersonalNumberFormatService {

    /**
     * {@inheritDoc}
     */
    @Override
    public String from(String number, String currentYear) {
        checkYear(currentYear);

        String numberYear = number.substring(0, 4);

        int age = Integer.parseInt(currentYear) - Integer.parseInt(numberYear);

        String separator = "-";
        if (age >= 100) {
            separator = "+";
        }

        String part1 = number.substring(2, 8);
        String part2 = number.substring(8);

        return part1.concat(separator).concat(part2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String to(String number, String currentYear) {
        checkYear(currentYear);

        int century = Integer.parseInt(currentYear.substring(0, 2));
        int yearDigits = Integer.parseInt(currentYear.substring(2, 4));
        int numberYear = Integer.parseInt(number.substring(0, 2));

        String separator = number.substring(6, 7);

        String year;
        if ("-".equals(separator)) {
            if (0 <= numberYear && numberYear <= yearDigits) {
                year = Integer.toString(century)
                        .concat(Integer.toString(numberYear));
            } else {
                year = Integer.toString(century - 1)
                        .concat(Integer.toString(numberYear));
            }
        } else {
            if (0 <= numberYear && numberYear <= yearDigits) {
                year = Integer.toString(century - 1)
                        .concat(Integer.toString(numberYear));
            } else {
                year = Integer.toString(century - 2)
                        .concat(Integer.toString(numberYear));
            }
        }

        return year.concat(number.substring(2, 6)).concat(number.substring(7));
    }

    private void checkYear(String year) {
        int yearAsNumber = Integer.parseInt(year);

        if (!(1200 <= yearAsNumber && yearAsNumber <= 9000)) {
            throw new YearOutOfBoundException(
                    "This service only works for years between 1200 and 9000");
        }
    }
}
