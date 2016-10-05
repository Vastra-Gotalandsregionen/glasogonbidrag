package se.vgregion.service.glasogonbidrag.local.api;

import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface PersonalNumberService {

    /**
     * A personal identification number contains the year a person was born.
     * We can use this information with the current year to calculate the
     * age of a the person with this identification number.
     *
     * Will throw IllegalArgumentException if the identification number is
     * in the future (compared to currentDate input).
     * Will throw IllegalArgumentException with a encapsulated ParseException
     * if number is in the wrong format.
     *
     * @param number identification number that should have
     *               it's age calculated, the format should be in the
     *               local storage format yyyymmddxxxx.
     * @param currentDate date of today.
     * @return the age of the person with the identification number.
     */
    int calculateAge(String number, Date currentDate);

    /**
     * Validate a swedish personal identification number via the
     * Luhn-algorithm (also known as IBM MOD10)
     *
     * @param number personal number that should be validated.
     *               the format should be in the local storage
     *               format yyyymmddxxxx.
     * @return true if the number is valid.
     */
    boolean validate(String number);
}
