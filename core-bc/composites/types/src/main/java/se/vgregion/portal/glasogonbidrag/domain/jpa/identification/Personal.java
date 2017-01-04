package se.vgregion.portal.glasogonbidrag.domain.jpa.identification;

import se.vgregion.portal.glasogonbidrag.domain.internal.DateCalculationUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@DiscriminatorValue("p")
@Table(name = "identification_personal")
public class Personal extends Identification {

    @Transient
    private final DateCalculationUtil dateUtil = new DateCalculationUtil();

    /**
     * Default constructor
     */
    public Personal() {
    }

    public Personal(String number) {
        super(number);
    }

    @Override
    public Date getBirthDate() {
        String date = dateUtil.extractDateFromPersonalNumbers(getNumber());
        return dateUtil.dateFromString(date);
    }

    // FIXME: Might be code duplication / unused code section.
    public boolean isValid() {
        boolean isValid = false;

        String tenCharNumber = getNumber();

        int length = tenCharNumber.length();
        if (tenCharNumber.contains("-")) {
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

    @Override
    public IdentificationType getType() {
        return IdentificationType.PERSONAL;
    }

    private int[] stringToNumberArray(String number) {
        int[] numberArray = new int[10];

        char[] numbers = number.toCharArray();
        for (int i = 0; i < 10; i++) {
            numberArray[i] = Integer.parseInt(Character.toString(numbers[i]));
        }

        return numberArray;
    }
}
