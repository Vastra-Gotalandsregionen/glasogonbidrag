package se.vgregion.service.glasogonbidrag.local.internal;

import org.springframework.stereotype.Service;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class PersonalNumberServiceImpl implements PersonalNumberService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Date parseBirthYear(String number) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date birthDate;
        try {
            birthDate = sdf.parse(number.substring(0, 8));
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "Number is in the wrong format.", e);
        }

        return birthDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int calculateAge(String number, Date currentDate) {
        Date birthDate = parseBirthYear(number);

        Calendar now = new GregorianCalendar();
        Calendar dob = new GregorianCalendar();

        now.setTime(currentDate);
        dob.setTime(birthDate);

        if (dob.after(now)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        int year1 = now.get(Calendar.YEAR);
        int year2 = dob.get(Calendar.YEAR);

        int age = year1 - year2;

        int month1 = now.get(Calendar.MONTH);
        int month2 = dob.get(Calendar.MONTH);

        if (month2 > month1) {
            age = age - 1;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = dob.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age = age - 1;
            }
        }

        return age;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(String number) {
        int[] numberArray = string2array(number.substring(2));

        return ibmMod10ver(numberArray);
    }



    /**
     * Verification with the Luhn-algorithm (also known as IBM MOD10)
     *
     * @param numbers an array of 10 numbers which should be verified.
     * @return true if the control digit (least significant) is
     *         correct or not.
     */
    private static boolean ibmMod10ver(int[] numbers) {
        int[] pattern = new int[] { 2, 1, 2, 1, 2, 1, 2, 1, 2, 1 };

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            int product = numbers[i] * pattern[i];
            sum = sum + (int)Math.floor(product / 10.0) + (product % 10);
        }

        return sum % 10 == 0;
    }

    /**
     * Turn a string of numbers into a array of integers.
     * This method works for numbers in the format yymmddxxxx.
     *
     * @param number identification number to turn into a string.
     * @return array of 10 integers.
     */
    private static int[] string2array(String number) {
        int[] is = new int[10];

        char[] numbers = number.toCharArray();
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
