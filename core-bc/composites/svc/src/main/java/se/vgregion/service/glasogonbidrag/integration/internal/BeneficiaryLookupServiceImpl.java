package se.vgregion.service.glasogonbidrag.integration.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.riv.population.residentmaster.extended.v1.AdministrativIndelningType;
import se.riv.population.residentmaster.extended.v1.ExtendedResidentType;
import se.riv.population.residentmaster.lookupresidentforextendedprofile.v1.rivtabp21.LookupResidentForExtendedProfileResponderInterface;
import se.riv.population.residentmaster.lookupresidentforextendedprofileresponder.v1.LookupResidentForExtendedProfileResponseType;
import se.riv.population.residentmaster.lookupresidentforextendedprofileresponder.v1.LookupResidentForExtendedProfileType;
import se.riv.population.residentmaster.lookupresidentforfullprofileresponder.v1.LookUpSpecificationType;
import se.riv.population.residentmaster.v1.JaNejTYPE;
import se.riv.population.residentmaster.v1.NamnTYPE;
import se.riv.population.residentmaster.v1.PersonpostTYPE;
import se.riv.population.residentmaster.v1.SvenskAdressTYPE;
import se.vgregion.service.glasogonbidrag.integration.api.BeneficiaryLookupService;
import se.vgregion.service.glasogonbidrag.integration.exception.IdentityFormatException;
import se.vgregion.service.glasogonbidrag.integration.exception.NoBeneficiaryFoundException;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberService;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryAreaTuple;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryNameTuple;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryTransport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class BeneficiaryLookupServiceImpl implements BeneficiaryLookupService {

    private SimpleDateFormat dateFormat;

    private Pattern IDENTITY_FORMAT_PATTERN = Pattern.compile("[0-9]{12}");

    @Autowired
    private PersonalNumberService personalNumberService;

    @Autowired
    private LookupResidentForExtendedProfileResponderInterface profileClient;

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

        LookupResidentForExtendedProfileResponseType response =
                profileClient.lookupResidentForExtendedProfile(
                        "", generateRequest(identity, date));

        return new BeneficiaryTransport(
        extractNameFromResponse(response),
                extractAreaFromRequest(response));
    }

    @Override
    public BeneficiaryNameTuple fetchName(String identity) {
        if (!validateIdentity(identity)) {
            throw new IdentityFormatException(
                    "The format of the identity must be twelve numbers " +
                            "without a hyphen.");
        }

        LookupResidentForExtendedProfileResponseType response =
                profileClient.lookupResidentForExtendedProfile(
                        "", generateRequest(identity));

        return extractNameFromResponse(response);
    }

    @Override
    public BeneficiaryAreaTuple fetchAddress(String identity,
                                                 Date date) {
        if (!validateIdentity(identity)) {
            throw new IdentityFormatException(
                    "The format of the identity must be twelve numbers " +
                            "without a hyphen.");
        }

        LookupResidentForExtendedProfileResponseType response =
                profileClient.lookupResidentForExtendedProfile(
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
        Matcher matcher = IDENTITY_FORMAT_PATTERN.matcher(identity);

        return matcher.matches() && personalNumberService.validate(identity);
    }

    /**
     * Fetches data from beneficiary with specified identity.
     *
     * @param identity to fetch data for.
     * @return Request for the specified identity.
     */
    public LookupResidentForExtendedProfileType generateRequest(
            String identity) {
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
    public LookupResidentForExtendedProfileType generateRequest(
            String identity, Date date) {
        LookupResidentForExtendedProfileType request =
                new LookupResidentForExtendedProfileType();
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
    private BeneficiaryNameTuple extractNameFromResponse(
            LookupResidentForExtendedProfileResponseType response) {
        ExtendedResidentType resident = getResident(response);

        if (resident.getSekretessmarkering() == JaNejTYPE.N) {
            return extractNameFromResponse(resident.getPersonpost());
        } else {
            return createNameForProtected();
        }
    }

    /**
     * Extract name from response.
     *
     * @param person PersonpostTYPE from the response.
     * @return first and last name of a person.
     */
    private BeneficiaryNameTuple extractNameFromResponse(
            PersonpostTYPE person) {
        NamnTYPE name = person.getNamn();

        return new BeneficiaryNameTuple(
                extractName(name.getFornamn(), ""),
                extractName(name.getEfternamn(), ""));
    }

    /**
     * Will return default string if value is null.
     *
     * @param name name to use, if this is null use defaultNameValue
     * @param defaultNameValue default value.
     * @return A string that isn't null
     */
    private String extractName(String name, String defaultNameValue) {
        if(name == null) {
            return defaultNameValue;
        } else {
            return name;
        }
    }

    /**
     * If the beneficiary have protected status, we cannot get a name
     * from the response, create a crossed out name for this user.
     *
     * @return a beneficiary name transport with just crosses.
     */
    private BeneficiaryNameTuple createNameForProtected() {
        return new BeneficiaryNameTuple("XXXXXX", "XXXXXX");
    }

    /**
     * Responses from the full client profile contains the beneficiary's
     * address, this method will extract this data from the response.
     * We are not interested in the full address of a beneficiary just their
     * county and municipality, this is that we extract.
     *
     * @param response A response from the lookupResident service with
     *                 Extended Profile support.
     * @return BeneficiaryAreaTransport which contains county code and
     *         municipality code.
     */
    private BeneficiaryAreaTuple extractAreaFromRequest(
            LookupResidentForExtendedProfileResponseType response) {
        ExtendedResidentType resident = getResident(response);
        AdministrativIndelningType registeredAddress =
                resident.getFolkbokforingsaddressIndelning();


        if (registeredAddress.getDebiteringsgruppKod() == null) {
            return extractAreaFromRequest(resident);
        } else {
            return createAreaFromAccountCode(
                    registeredAddress.getDebiteringsgruppKod());
        }
    }

    /**
     * The registered address contains the county and municipality
     * codes used, this method extract these from the resident object.
     *
     * @param resident resident object from the response.
     * @return Area transport with the beneficiary's county code
     *         and municipality code.
     */
    private BeneficiaryAreaTuple extractAreaFromRequest(
            ExtendedResidentType resident) {
        SvenskAdressTYPE address = resident
                .getPersonpost().getFolkbokforingsadress();

        return new BeneficiaryAreaTuple(
                address.getKommunKod(), address.getLanKod());
    }

    /**
     * If the beneficiary have a account code we should use this instead
     * of the registered address.
     *
     * @return the account code split in two.
     */
    private BeneficiaryAreaTuple createAreaFromAccountCode(
            String accountCode) {
        return new BeneficiaryAreaTuple(
                accountCode.substring(2),
                accountCode.substring(0, 2));
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
     *                 Extended Profile support.
     * @return ExtendedResidentType if there is only one.
     * @throws NoBeneficiaryFoundException
     *         Will be thrown if we have more than one ResidentType.
     */
    private ExtendedResidentType getResident(
            LookupResidentForExtendedProfileResponseType response) {
        List<ExtendedResidentType> residentTypes = response.getResident();
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
