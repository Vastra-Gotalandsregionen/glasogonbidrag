package se.vgregion.service.glasogonbidrag.integration.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.riv.population.residentmaster.lookupresidentforfullprofile.v1.rivtabp21.LookupResidentForFullProfileResponderInterface;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookUpSpecificationType;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileResponseType;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookupResidentForFullProfileType;
import se.riv.population.residentmaster.v1.NamnTYPE;
import se.riv.population.residentmaster.v1.ResidentType;
import se.riv.population.residentmaster.v1.SvenskAdressTYPE;
import se.vgregion.service.glasogonbidrag.integration.api.BeneficiaryLookupService;
import se.vgregion.service.glasogonbidrag.integration.exception.IdentityFormatException;
import se.vgregion.service.glasogonbidrag.integration.exception.NoBeneficiaryFoundException;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryAreaTransport;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryNameTransport;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryTransport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class BeneficiaryLookupServiceImpl implements BeneficiaryLookupService {

    private SimpleDateFormat dateFormat;

    @Autowired
    private LookupResidentForFullProfileResponderInterface fullProfileClient;

    public BeneficiaryLookupServiceImpl() {
        dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    }

    @Override
    public BeneficiaryTransport fetchNameAndAddress(String identity,
                                                    Date date) {
        if (!validateIdentity(identity)) {
            throw new IdentityFormatException(
                    "The format of the identity must be twelve numbers " +
                            "without a hyphen.");
        }

        LookupResidentForFullProfileResponseType response =
                fullProfileClient.lookupResidentForFullProfile(
                        "", generateRequest(identity, date));

        return new BeneficiaryTransport(
                extractNameFromResponse(response),
                extractAreaFromRequest(response));
    }

    @Override
    public BeneficiaryNameTransport fetchName(String identity) {
        if (!validateIdentity(identity)) {
            throw new IdentityFormatException(
                    "The format of the identity must be twelve numbers " +
                            "without a hyphen.");
        }

        LookupResidentForFullProfileResponseType response =
                fullProfileClient.lookupResidentForFullProfile(
                        "", generateRequest(identity));

        return extractNameFromResponse(response);
    }

    @Override
    public BeneficiaryAreaTransport fetchAddress(String identity,
                                                 Date date) {
        if (!validateIdentity(identity)) {
            throw new IdentityFormatException(
                    "The format of the identity must be twelve numbers " +
                            "without a hyphen.");
        }

        LookupResidentForFullProfileResponseType response =
                fullProfileClient.lookupResidentForFullProfile(
                        "", generateRequest(identity, date));

        return extractAreaFromRequest(response);
    }

    // Helper methods

    /**
     * Validate that the identity string is in the correct format.
     * Should be twelve numbers without hyphen in the format yyyymmddxxxx.
     *
     * @param identity to validate
     * @return true if the identity is valid otherwise false.
     */
    public boolean validateIdentity(String identity) {
        return false;
    }

    /**
     * Fetches data from beneficiary with specified identity.
     *
     * @param identity to fetch data for.
     * @return Request for the specified identity.
     */
    public LookupResidentForFullProfileType generateRequest(String identity) {
        return generateRequest(identity, null);
    }

    /**
     * Fetches data from beneficiary with specified identity, at a historical
     * time.
     *
     * @param identity to fetch data for.
     * @param date request data from this historic time, must be in the past.
     * @return Request for the specified identity at specified date.
     */
    public LookupResidentForFullProfileType generateRequest(String identity,
                                                            Date date) {
        LookupResidentForFullProfileType request =
                new LookupResidentForFullProfileType();
        request.getPersonId().add(identity);

        if (date != null) {
            LookUpSpecificationType spec = new LookUpSpecificationType();

            spec.setHistoriskTidpunkt(dateFormat.format(date));

            request.setLookUpSpecification(spec);
        }

        return request;
    }

    /**
     * Responses from the full client profile contains the beneficiary's name
     * this method will extract this data from the response.
     *
     * @param response A response from the lookupResident service with
     *                 Full Profile support.
     * @return BeneficiaryNameTransport which contains first name and
     *         surname.
     */
    private BeneficiaryNameTransport extractNameFromResponse(
            LookupResidentForFullProfileResponseType response) {
        ResidentType resident = getResident(response);
        NamnTYPE name = resident.getPersonpost().getNamn();

        return new BeneficiaryNameTransport(
                name.getFornamn(), name.getEfternamn());
    }

    /**
     * Responses from the full client profile contains the beneficiary's
     * address, this method will extract this data from the response.
     * We are not interested in the full address of a beneficiary just their
     * county and municipality, this is that we extract.
     *
     * @param response A response from the lookupResident service with
     *                 Full Profile support.
     * @return BeneficiaryAreaTransport which contains county code and
     *         municipality code.
     */
    private BeneficiaryAreaTransport extractAreaFromRequest(
            LookupResidentForFullProfileResponseType response) {
        ResidentType resident = getResident(response);
        SvenskAdressTYPE address = resident
                .getPersonpost().getFolkbokforingsadress();

        return new BeneficiaryAreaTransport(
                address.getKommunKod(), address.getLanKod());
    }

    /**
     * Make sure we only have one resident from the response.
     * One can ask for more than one resident via the LookupResidentFor*
     * service, in our service we just want to ask for single residents.
     *
     * To simplify the interfaces this methods will make sure that we only
     * use this service in this way.
     *
     * @param response A response from the lookupResident service with
     *                 Full Profile support.
     * @return ResidentType if there is only one.
     * @throws NoBeneficiaryFoundException
     *         Will be thrown if we have more than one ResidentType.
     */
    private ResidentType getResident(
            LookupResidentForFullProfileResponseType response) {
        List<ResidentType> residentTypes = response.getResident();
        if (residentTypes.size() < 1) {
            throw new NoBeneficiaryFoundException(
                    "No resident with supplied id.");
        } else if (residentTypes.size() > 1) {
            throw new NoBeneficiaryFoundException(
                    "Found more than one resident.");
        }

        return residentTypes.get(0);
    }
}
