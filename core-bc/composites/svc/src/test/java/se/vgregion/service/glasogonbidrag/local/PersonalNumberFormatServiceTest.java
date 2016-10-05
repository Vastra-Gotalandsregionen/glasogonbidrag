package se.vgregion.service.glasogonbidrag.local;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberFormatService;
import se.vgregion.service.glasogonbidrag.local.exception.YearOutOfBoundException;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localTestContext.xml")
public class PersonalNumberFormatServiceTest {

    @Autowired
    private PersonalNumberFormatService formatService;

    @Test
    public void serviceShouldExists() {
        Assert.assertNotNull(formatService);
    }

    @Test
    public void fromLocalNumberFormat() {
        String number1 = "196801029288";
        String number2 = "201212121212";

        String year = "2016";

        Assert.assertEquals("680102-9288", formatService.from(number1, year));
        Assert.assertEquals("121212-1212", formatService.from(number2, year));
    }

    @Test
    public void fromLocalNumberAgeOver100() {
        String number = "191002034823";

        String year = "2016";

        Assert.assertEquals("100203+4823", formatService.from(number, year));
    }

    @Test
    public void toLocalNumberFormat() {
        String number1 = "680102-9288";
        String number2 = "121212-1212";

        String year = "2016";

        Assert.assertEquals("196801029288", formatService.to(number1, year));
        Assert.assertEquals("201212121212", formatService.to(number2, year));
    }


    @Test
    public void toLocalNumberFormatInTheFuture() {
        String number1 = "680102-9288";
        String number2 = "121212-1212";

        String year = "2078";

        Assert.assertEquals("206801029288", formatService.to(number1, year));
        Assert.assertEquals("201212121212", formatService.to(number2, year));
    }

    @Test
    public void toLocalNumberAgeOver100() {
        String number = "100203+4823";

        String year = "2016";

        Assert.assertEquals("191002034823", formatService.to(number, year));
    }

    @Test(expected = YearOutOfBoundException.class)
    public void fromLocalNumberBeforeTheYear1200() {
        formatService.from("680102-9288", "1199");
    }

    @Test(expected = YearOutOfBoundException.class)
    public void fromLocalNumberAfterTheYear9000() {
        formatService.from("680102-9288", "9001");
    }

    @Test(expected = YearOutOfBoundException.class)
    public void toLocalNumberBeforeTheYear1200() {
        formatService.to("680102-9288", "1199");
    }

    @Test(expected = YearOutOfBoundException.class)
    public void toLocalNumberBeforeTheYear9000() {
        formatService.to("680102-9288", "9001");
    }
}
