package se.vgregion.service.glasogonbidrag.local.api;

/**
 * This service will transform between the internal format of
 * personal identification numbers and the standard set by "skatteverket"
 * in sweden.
 *
 * This service only works for years between 1200 and 9000.
 *
 * @author Martin Lind - Monator Technologies AB
 */
public interface PersonalNumberFormatService {

    /**
     * Transform number from internal format to standard format.
     *
     * Since a identification number after 100 years has a plus (+) instead
     * of a hyphen-minus (-) one need to supply the current year for this
     * method to work.
     *
     * @param number identification number in the format yyyymmddxxxx.
     * @param currentYear a year in the format yyyy.
     * @return the same identification number in the format yymmdd[+-]xxxx.
     */
    String from(String number, String currentYear);

    /**
     * Transform number to internal format from standard format.
     *
     * Since a identification number after 100 years has a plus (+) instead
     * of a hyphen-minus (-) one need to supply the current year for this
     * method to work.
     *
     * @param number identification number in the format yymmdd[+-]xxxx.
     * @param currentYear a year in the format yyyy.
     * @return the same identification number in the format yyyymmddxxxx.
     */
    String to(String number, String currentYear);
}
