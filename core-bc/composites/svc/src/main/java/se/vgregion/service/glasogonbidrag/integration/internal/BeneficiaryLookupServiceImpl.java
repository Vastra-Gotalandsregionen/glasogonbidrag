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
import se.vgregion.portal.glasogonbidrag.domain.SexType;
import se.vgregion.service.glasogonbidrag.integration.api.BeneficiaryLookupService;
import se.vgregion.service.glasogonbidrag.integration.exception.IdentityFormatException;
import se.vgregion.service.glasogonbidrag.integration.exception.NoBeneficiaryFoundException;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberService;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryAreaTuple;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryNameTuple;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryTransport;

import java.text.DateFormat;
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

    private static final DateFormat FORMAT =
            new SimpleDateFormat("yyyyMMddhhmmss");
    private static final String PROTECTED_NAME = "XXXXXX XXXXXX";

    private static final Pattern IDENTITY_FORMAT_PATTERN =
            Pattern.compile("[0-9]{12}");

    @Autowired
    private PersonalNumberService personalNumberService;

    @Autowired
    private LookupResidentForExtendedProfileResponderInterface profileClient;

    public BeneficiaryLookupServiceImpl() {
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
                        "", createRequest(identity, date));

        return new BeneficiaryTransport(
                extractDataFromResponse(response),
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
                        "", createRequest(identity, null));

        return extractDataFromResponse(response);
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
                        "", createRequest(identity, date));

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
     * Fetches data from beneficiary with specified identity, at a historical
     * time.
     *
     * @param identity to fetch data for.
     * @param date request data from this historic time, must be in the past.
     *             to request current information send null.
     * @return Request for the specified identity at specified date.
     */
    public LookupResidentForExtendedProfileType createRequest(
            String identity, Date date) {
        LookupResidentForExtendedProfileType request =
                new LookupResidentForExtendedProfileType();
        request.getPersonId().add(identity);

        if (date != null) {
            LookUpSpecificationType spec = new LookUpSpecificationType();

            spec.setHistoriskTidpunkt(FORMAT.format(date));

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
    private BeneficiaryNameTuple extractDataFromResponse(
            LookupResidentForExtendedProfileResponseType response) {
        List<ExtendedResidentType> residentTypes = response.getResident();

        if (residentTypes.size() < 1) {
            throw new NoBeneficiaryFoundException(
                    "No resident with supplied id.");
        } else if (residentTypes.size() > 1) {
            throw new NoBeneficiaryFoundException(
                    "Found more than one resident.");
        }

        ExtendedResidentType resident = residentTypes.get(0);

        if (resident.getSekretessmarkering() == JaNejTYPE.J) {
            // A Beneficiary have only a name of Xs
            return new BeneficiaryNameTuple(PROTECTED_NAME);
        }

        PersonpostTYPE person = resident.getPersonpost();

        NamnTYPE name = person.getNamn();

        String nameString = "";
        if (name != null) {
            String firstName = name.getFornamn();
            String lastName = name.getEfternamn();
            if (firstName == null) {
                firstName = "";
            }

            if (lastName == null) {
                lastName = "";
            }

            nameString = String.format("%s %s", firstName, lastName).trim();
        }

        SexType sex;
        if (person.getKon() == null) {
            sex = SexType.UNKNOWN;
        } else switch (person.getKon()) {
            case M:
                sex = SexType.MALE;
                break;
            case K:
                sex = SexType.FEMALE;
                break;
            default:
                sex = SexType.UNKNOWN;
                break;
        }

        return new BeneficiaryNameTuple(nameString, sex);
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
        List<ExtendedResidentType> residentTypes = response.getResident();

        if (residentTypes.size() < 1) {
            throw new NoBeneficiaryFoundException(
                    "No resident with supplied id.");
        } else if (residentTypes.size() > 1) {
            throw new NoBeneficiaryFoundException(
                    "Found more than one resident.");
        }

        ExtendedResidentType resident = residentTypes.get(0);

        String municipalityCode;
        String countyCode;
        if (resident.getFolkbokforingsaddressIndelning() != null &&
                resident.getFolkbokforingsaddressIndelning()
                        .getDebiteringsgruppKod() != null) {
            String accountCode = resident.getFolkbokforingsaddressIndelning()
                    .getDebiteringsgruppKod();

            municipalityCode = accountCode.substring(2);
            countyCode = accountCode.substring(0, 2);
        } else {
            SvenskAdressTYPE address = resident
                    .getPersonpost().getFolkbokforingsadress();

            municipalityCode = address.getLanKod();
            countyCode = address.getKommunKod();
        }

        return new BeneficiaryAreaTuple(municipalityCode, countyCode);
    }
}
