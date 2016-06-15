package se.vgregion.glasogonbidrag.internal;

import org.omg.CORBA.IDLType;
import se.vgregion.glasogonbidrag.model.IdType;

/**
 * Created by martlin on 2016/06/15.
 */
public class IdentificationUtil {
    public static IdType detect(String id) {
        IdType type = IdType.NONE;

        if (detectPersonalNumber(id)) {
            if (validatePersonalNumber(id)) {
                type = IdType.VALID;
            } else {
                type = IdType.INVALID;
            }
        } else if (detectReservedNumber(id)) {
            type = IdType.RESERVE;
        }

        return type;
    }

    public static boolean detectPersonalNumber(String number) {
        int length = number.length() - 1;

        if (length != 10 && length != 12) {
            return false;
        }

        String reversedNumber =
                new StringBuilder(number).reverse().toString();

        String[] split = number.split("-", 2);

        return reversedNumber.charAt(4) == '-'
                && split.length == 2
                && !split[0].isEmpty() && !split[1].isEmpty()
                && split[0].matches("[0-9]+")
                && split[1].matches("[0-9]+")
                && (split[0].length() == 6 || split[0].length() == 8)
                && split[1].length() == 4;
    }

    public static boolean detectReservedNumber(String number) {
        int length = number.length() - 1;

        if (length != 10 && length != 12) {
            return false;
        }

        String reversedNumber =
                new StringBuilder(number).reverse().toString();
        String[] split = number.split("-", 2);

        return reversedNumber.charAt(4) == '-'
                && split.length == 2
                && !split[0].isEmpty() && !split[1].isEmpty()
                && split[0].matches("[0-9]+")
                && (split[0].length() == 6 || split[0].length() == 8)
                && split[1].length() == 4;
    }

    public static boolean validatePersonalNumber(String id) {
        String num = removeHyphen(removeYear(id));

        int[] numbers = string2array(num);

        return ibmMod10(numbers);
    }

    // Transform identificaiton numbers

    public static String removeYear(String id) {
        String numbers = removeHyphen(id);

        if (numbers.length() == 12) {
            return id.substring(2);
        } else {
            return id;
        }
    }

    public static String addYear(String id, int year) {
        return null;
    }

    public static String removeHyphen(String id) {
        if (!id.contains("-")) {
            return id;
        }

        int index = id.indexOf('-');
        return id.substring(0, index).concat(id.substring(index + 1));
    }

    public static String addHyphen(String id) {
        return null;
    }

    // Helper methods

    private static boolean ibmMod10(int[] numbers) {
        int[] pattern = new int[] {2,1,2,1,2,1,2,1,2,1};

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            int product = numbers[i] * pattern[i];
            sum = sum + (int)Math.floor(product/10.0) + (product % 10);
        }

        return sum % 10 == 0;
    }

    private static int[] string2array(String id) {
        int[] is = new int[10];

        char[] numbers = removeHyphen(id).toCharArray();
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
