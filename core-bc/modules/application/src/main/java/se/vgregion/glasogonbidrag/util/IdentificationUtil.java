package se.vgregion.glasogonbidrag.util;

import se.vgregion.glasogonbidrag.model.IdentificationType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public final class IdentificationUtil {

    private static final Pattern personalNumberRegex;
    private static final Pattern reserveNumberRegex;
    static {
        personalNumberRegex = Pattern.compile(
                "(?<year>[0-9]{2})?" +
                        "(?<number>[0-9]{6})\\-(?<control>[0-9]{4})");
        reserveNumberRegex = Pattern.compile(
                "(?<year>[0-9]{2})?" +
                        "(?<number>[0-9]{6}\\-[A-Z0-9]{4})");
    }

    /**
     * Prevent initialization of this class.
     */
    private IdentificationUtil() {}

    /**
     * Detect the type the identification number is.
     * The number can be a Valid, Invalid or Reserved number if it is in
     * the correct format. It can also be None if it's neither a
     * personal identification number or a reserve number.
     *
     * @param number the string to test if it's a identification number.
     * @return the type the identification number is of.
     */
    public static IdentificationType detect(String number) {
        IdentificationType type = IdentificationType.NONE;

        if (detectPersonalNumber(number)) {
            if (validatePersonalNumber(number)) {
                type = IdentificationType.VALID;
            } else {
                type = IdentificationType.INVALID;
            }
        } else if (detectReservedNumber(number)) {
            type = IdentificationType.RESERVE;
        }

        return type;
    }

    /**
     * Detect if the string is an personal identification number.
     * It may or may not be a valid identification number but it should be
     * in the correct format.
     *
     * @param number the number to test.
     * @return true if the number follows the correct format.
     */
    public static boolean detectPersonalNumber(String number) {
        Matcher matcher = personalNumberRegex.matcher(number);
        return matcher.matches();
    }

    /**
     * Detect if the string is an reserve number.
     * A personal identification number is also a correct reserve number.
     *
     * @param number the number to test.
     * @return true if the number follows the correct format.
     */
    public static boolean detectReservedNumber(String number) {
        Matcher matcher = reserveNumberRegex.matcher(number);
        return matcher.matches();
    }

    /**
     * Validate a personal number. Checks if the control digit is
     * correctly entered.
     *
     * @param number the number to validate.
     * @return true if the identification number is correct.
     */
    public static boolean validatePersonalNumber(String number) {
        String n = removeYear(number);
        int[] ns = string2array(n);
        return ibmMod10ver(ns);
    }

    // Transform identification numbers

    /**
     * Remove leading two digits if the number contains 12 digits.
     *
     * @param number the number to remove the year from.
     * @return return a identification number with only the two least
     *         significant digits from the year.
     */
    public static String removeYear(String number) {
        int length = number.length() - 1;

        if (length == 12 || length == 10) {
            return number.substring(2);
        } else {
            return number;
        }
    }

    /**
     * Removes the hyphen from an identitification number.
     * Will return the original string if the string don't contains an
     * hyphen.
     *
     * @param number to remove the hyphen from.
     * @return a string without hyphen
     */
    public static String removeHyphen(String number) {
        int index = number.indexOf('-');

        if (index == -1) {
            return number;
        } else {
            return number.substring(0, index)
                    .concat(number.substring(index + 1));
        }
    }

    // Helper methods

    /**
     * Verification of the Luhn-algorithm (also known as IBM MOD10)
     *
     * @param numbers an array of 10 numbers which should be verified.
     * @return true if the control digit (least significant) is
     *         correct or not.
     */
    private static boolean ibmMod10ver(int[] numbers) {
        int[] pattern = new int[] {2,1,2,1,2,1,2,1,2,1};

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            int product = numbers[i] * pattern[i];
            sum = sum + (int)Math.floor(product/10.0) + (product % 10);
        }

        return sum % 10 == 0;
    }

    /**
     * Turn a string of numbers into a array of integers.
     * This method only works when there the number are in the format
     * YYMMDD-XXXX or YYMMDDXXXX, the hyphen will be removed by the code.
     *
     * @param number identification number to turn into a string.
     * @return array of 10 integers.
     */
    private static int[] string2array(String number) {
        int[] is = new int[10];

        char[] numbers = removeHyphen(number).toCharArray();
        for (int i = 0; i < 10; i++) {
            is[i] = char2int(numbers[i]);
        }

        return is;
    }

    /**
     * Convert a number represented as a character to it's integer
     * value.
     *
     * @param num to convert.
     * @return an integer (between 0 - 9)
     */
    private static int char2int(char num) {
        return Integer.parseInt(Character.toString(num));
    }
}
