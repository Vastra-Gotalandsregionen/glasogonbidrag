package se.vgregion.glasogonbidrag.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class GrantUtil {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GrantUtil.class);


    public boolean validatePersonalNumber(String number) {
        boolean isValid = false;

        String tenCharNumber = number;

        int length = tenCharNumber.length();
        if (number.contains("-")) {
            length = length - 1;
        }

        if (length == 12) {
            tenCharNumber = tenCharNumber.substring(2);
        }

        tenCharNumber = tenCharNumber.substring(0,6)
                .concat(tenCharNumber.substring(7));

        try {
            int[] numberArray = stringToNumberArray(tenCharNumber);
            int[] validationPattern = new int[] {2,1,2,1,2,1,2,1,2,1};

            int sum = 0;
            for (int i = 0; i < 10; i++) {
                int product = numberArray[i] * validationPattern[i];
                sum = sum + (int)Math.floor(product/10.0) + (product % 10);
            }

            isValid = (sum % 10 == 0);
        } catch(NumberFormatException nfe) {
            isValid = false;
        }


        return isValid;
    }

    public int[] stringToNumberArray(String number) {
        int[] numberArray = new int[10];

        char[] numbers = number.toCharArray();
        for (int i = 0; i < 10; i++) {
            numberArray[i] = Integer.parseInt(Character.toString(numbers[i]));
        }

        return numberArray;
    }

}
