package se.vgregion.service.glasogonbidrag.local;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localTestContext.xml")
public class PersonalNumberServiceTest {

    @Autowired
    private PersonalNumberService numberService;

    @Test
    public void serviceShouldExists() {
        Assert.assertNotNull(numberService);
    }

    @Test
    public void calculateAges() {
        String number1 = "200101010106";
        String number2 = "191212121212";
        String number3 = "196801029288";
        String number4 = "199404188055";
        String number5 = "199404217789";
        String number6 = "199404229255";

        // Set date to 2016-04-21
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 21);

        Date currentDate = cal.getTime();

        Assert.assertEquals(
                15, numberService.calculateAge(number1, currentDate));
        Assert.assertEquals(
                103, numberService.calculateAge(number2, currentDate));
        Assert.assertEquals(
                48, numberService.calculateAge(number3, currentDate));
        Assert.assertEquals(
                22, numberService.calculateAge(number4, currentDate));
        Assert.assertEquals(
                22, numberService.calculateAge(number5, currentDate));
        Assert.assertEquals(
                21, numberService.calculateAge(number6, currentDate));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bornInTheFuture() {
        String number1 = "200101010101";

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2000);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 12);

        Date currentDate = cal.getTime();

        numberService.calculateAge(number1, currentDate);
    }

    @Test
    public void validatePersonalNumbers() {
        String valid1 = "191212121212";
        String valid2 = "199404188055";
        String valid3 = "199404217789";

        String invalid1 = "200101010108";
        String invalid2 = "199404229252";
        String invalid3 = "196801029289";

        Assert.assertTrue(numberService.validate(valid1));
        Assert.assertTrue(numberService.validate(valid2));
        Assert.assertTrue(numberService.validate(valid3));

        Assert.assertFalse(numberService.validate(invalid1));
        Assert.assertFalse(numberService.validate(invalid2));
        Assert.assertFalse(numberService.validate(invalid3));
    }
}
