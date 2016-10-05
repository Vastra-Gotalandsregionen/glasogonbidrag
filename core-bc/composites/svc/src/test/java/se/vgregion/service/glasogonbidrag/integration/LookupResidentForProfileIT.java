package se.vgregion.service.glasogonbidrag.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.riv.population.residentmaster.extended.v1.ExtendedResidentType;
import se.riv.population.residentmaster.lookupresidentforextendedprofile.v1.rivtabp21.LookupResidentForExtendedProfileResponderInterface;
import se.riv.population.residentmaster.lookupresidentforextendedprofileresponder.v1.LookupResidentForExtendedProfileResponseType;
import se.riv.population.residentmaster.lookupresidentforextendedprofileresponder.v1.LookupResidentForExtendedProfileType;
import se.riv.population.residentmaster.lookupresidentforfullprofile.v1.rivtabp21.LookupResidentForFullProfileResponderInterface;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookUpSpecificationType;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileResponseType;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileType;
import se.riv.population.residentmaster.v1.NamnTYPE;
import se.riv.population.residentmaster.v1.ResidentType;
import se.riv.population.residentmaster.v1.SvenskAdressTYPE;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertNotNull(fullProfileClient);
    }

    @Test
    public void testExtendedProfileRequest() {
        LookupResidentForExtendedProfileType request = new LookupResidentForExtendedProfileType();

        request.getPersonId().add("196801029288");

        LookupResidentForExtendedProfileResponseType response = extendedProfileClient
                .lookupResidentForExtendedProfile("", request);

        List<Object> objs = response.getAny();
        List<ExtendedResidentType> resi = response.getResident();
        ExtendedResidentType ext = resi.get(0);

        assertTrue(true);
    }

    @Test
    public void testFullProfileRequest() {
        LookupResidentForFullProfileType request = new LookupResidentForFullProfileType();

        request.getPersonId().add("196801029288");
        request.setLookUpSpecification(new LookUpSpecificationType());

        LookupResidentForFullProfileResponseType response = fullProfileClient
                .lookupResidentForFullProfile("", request);

        List<Object> objs = response.getAny();
        List<ResidentType> resis = response.getResident();
        ResidentType resi = resis.get(0);

        NamnTYPE namn = resi.getPersonpost().getNamn();
        SvenskAdressTYPE addr = resi.getPersonpost().getFolkbokforingsadress();


        assertTrue(true);
    }
}
