package se.vgregion.service.glasogonbidrag.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.riv.population.residentmaster.lookupresidentforextendedprofile.v1.rivtabp21.LookupResidentForExtendedProfileResponderInterface;
import se.riv.population.residentmaster.lookupresidentforextendedprofileresponder.v1.LookupResidentForExtendedProfileResponseType;
import se.riv.population.residentmaster.lookupresidentforextendedprofileresponder.v1.LookupResidentForExtendedProfileType;
import se.riv.population.residentmaster.lookupresidentforfullprofile.v1.rivtabp21.LookupResidentForFullProfileResponderInterface;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileResponseType;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileType;

import static org.junit.Assert.assertNotNull;

/**
 * @author Patrik Björk
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:webservice-context-it.xml")
public class LookupResidentForProfileIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupResidentForProfileIT.class);

    @Autowired
    private LookupResidentForExtendedProfileResponderInterface extendedProfileClient;

    @Autowired
    private LookupResidentForFullProfileResponderInterface fullProfileClient;

    @Test
    public void testContext() throws Exception {
        assertNotNull(extendedProfileClient);
    }

    @Test
    public void testExtendedProfileRequest() {
        LookupResidentForExtendedProfileType request = new LookupResidentForExtendedProfileType();

        request.getPersonId().add("201010101010");

        LookupResidentForExtendedProfileResponseType response = extendedProfileClient
                .lookupResidentForExtendedProfile("", request);

        LOGGER.info(response.getResident().toString());
    }

    @Test
    public void testFullProfileRequest() {
        LookupResidentForFullProfileType request = new LookupResidentForFullProfileType();

        request.getPersonId().add("201010101010");

        LookupResidentForFullProfileResponseType response = fullProfileClient
                .lookupResidentForFullProfile("", request);

        LOGGER.info(response.getResident().toString());
    }
}
