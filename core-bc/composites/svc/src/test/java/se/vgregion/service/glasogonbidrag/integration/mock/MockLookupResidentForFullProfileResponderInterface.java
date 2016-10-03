package se.vgregion.service.glasogonbidrag.integration.mock;

import se.riv.population.residentmaster.lookupresidentforfullprofile.v1.rivtabp21.LookupResidentForFullProfileResponderInterface;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileResponseType;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileType;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class MockLookupResidentForFullProfileResponderInterface
        implements LookupResidentForFullProfileResponderInterface {

    @Override
    public LookupResidentForFullProfileResponseType
            lookupResidentForFullProfile(
                    String logicalAddress,
                    LookupResidentForFullProfileType parameters) {
        return null;
    }

}
