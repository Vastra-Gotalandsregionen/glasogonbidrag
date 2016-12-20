package se.vgregion.service.glasogonbidrag.local;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.portal.glasogonbidrag.domain.dto.ImportDTO;
import se.vgregion.service.glasogonbidrag.local.api.GrantImportService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:localTestContext.xml")
public class GrantImportServiceTest {
    @Autowired
    private GrantImportService service;

    @Test
    public void serviceShouldExist() {
        Assert.assertNotNull(service);
    }

    @Test
    public void importExcelFile() throws IOException {
        InputStream input = getClass()
                .getResourceAsStream("/import/document.xlsx");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int r = input.read(buffer);
            if (r == -1) break;
            stream.write(buffer, 0, r);
        }

        byte[] data = stream.toByteArray();

        stream.close();
        input.close();

        ImportDTO dto = service.importData(
                data, "2016", Locale.forLanguageTag("sv-se"));

        Assert.assertEquals(3, dto.getBeneficiaries().size());
        Assert.assertEquals(10, dto.getPrescriptions().size());
    }
}
