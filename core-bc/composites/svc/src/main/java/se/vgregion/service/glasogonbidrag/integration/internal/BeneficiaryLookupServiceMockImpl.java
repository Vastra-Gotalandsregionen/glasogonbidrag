package se.vgregion.service.glasogonbidrag.integration.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.SexType;
import se.vgregion.service.glasogonbidrag.integration.api.BeneficiaryLookupService;
import se.vgregion.service.glasogonbidrag.integration.exception.IdentityFormatException;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberService;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryAreaTuple;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryNameTuple;
import se.vgregion.service.glasogonbidrag.types.BeneficiaryTransport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Qualifier("mock")
public class BeneficiaryLookupServiceMockImpl implements BeneficiaryLookupService {

    private static final DateFormat FORMAT =
            new SimpleDateFormat("yyyyMMddhhmmss");
    private static final String PROTECTED_NAME = "XXXXXX XXXXXX";

    private static final Pattern IDENTITY_FORMAT_PATTERN =
            Pattern.compile("[0-9]{12}");

    @Autowired
    private PersonalNumberService personalNumberService;

    private List<String> identities = new ArrayList<>();
    private Map<String, BeneficiaryNameTuple> names = new HashMap<>();
    private Map<String, BeneficiaryAreaTuple> areas = new HashMap<>();


    public BeneficiaryLookupServiceMockImpl() {
        identities.add("200110302387");
        identities.add("200110172392");
        identities.add("199701032394");
        identities.add("199801222390");
        identities.add("200112152384");
        identities.add("196801029288");
        identities.add("199604222399");
        identities.add("199508232387");
        identities.add("199711232398");
        identities.add("199801152381");
        identities.add("199801042392");
        identities.add("196508122857");

        names.put("200110302387", new BeneficiaryNameTuple("Anders Berghagen", SexType.MALE));
        names.put("200110172392", new BeneficiaryNameTuple("Erik Ljungqvist", SexType.MALE));
        names.put("199701032394", new BeneficiaryNameTuple("Ted Asklund", SexType.MALE));
        names.put("199801222390", new BeneficiaryNameTuple("Ron Jeremy", SexType.MALE));
        names.put("200112152384", new BeneficiaryNameTuple("Bob fyrkant", SexType.MALE));
        names.put("196801029288", new BeneficiaryNameTuple("Frida", SexType.FEMALE));
        names.put("199604222399", new BeneficiaryNameTuple("Muhammad bin-Salman", SexType.MALE));
        names.put("199508232387", new BeneficiaryNameTuple("Björn Ryding", SexType.MALE));
        names.put("199711232398", new BeneficiaryNameTuple("Maja Trekan", SexType.FEMALE));
        names.put("199801152381", new BeneficiaryNameTuple("Grace Hopper", SexType.FEMALE));
        names.put("199801042392", new BeneficiaryNameTuple("Ada Lovelace", SexType.FEMALE));
        names.put("196508122857", new BeneficiaryNameTuple("Ulla-bella Sekreterare", SexType.FEMALE));

        areas.put("200110302387", new BeneficiaryAreaTuple("14", "45")); //essunga
        areas.put("200110172392", new BeneficiaryAreaTuple("14", "93")); //mariestad
        areas.put("199701032394", new BeneficiaryAreaTuple("14", "96")); //skövde
        areas.put("199801222390", new BeneficiaryAreaTuple("14", "40")); //ale
        areas.put("200112152384", new BeneficiaryAreaTuple("14", "80")); //göteborg
        areas.put("196801029288", new BeneficiaryAreaTuple("14", "80")); //göteborg
        areas.put("199604222399", new BeneficiaryAreaTuple("14", "81")); //mölndal
        areas.put("199508232387", new BeneficiaryAreaTuple("14", "02")); //partille
        areas.put("199711232398", new BeneficiaryAreaTuple("14", "66")); //herrljunga
        areas.put("199801152381", new BeneficiaryAreaTuple("14", "91")); //ulricehamn
        areas.put("199801042392", new BeneficiaryAreaTuple("14", "27")); //sotenäs
        areas.put("196508122857", new BeneficiaryAreaTuple("14", "92")); //åmål
    }

    @Override
    public BeneficiaryTransport fetchNameAndAddress(String identity, Date date) {
        BeneficiaryNameTuple name = fetchName(identity);
        BeneficiaryAreaTuple area = fetchAddress(identity, date);

        return new BeneficiaryTransport(name, area);
    }

    @Override
    public BeneficiaryNameTuple fetchName(String identity) {
        if (invalidIdentity(identity)) {
            throw new IdentityFormatException(
                    "The format of the identity must be twelve numbers " +
                            "without a hyphen.");
        }

        if (!identities.contains(identity)) {
            throw new IllegalArgumentException(
                    "User not existing in mock service");
        }

        return names.get(identity);
    }

    @Override
    public BeneficiaryAreaTuple fetchAddress(String identity, Date date) {
        if (invalidIdentity(identity)) {
            throw new IdentityFormatException(
                    "The format of the identity must be twelve numbers " +
                            "without a hyphen.");
        }

        if (!identities.contains(identity)) {
            throw new IllegalArgumentException(
                    "User not existing in mock service");
        }

        return areas.get(identity);
    }

    /**
     * Validate that the identity string is in the correct format.
     * Should be twelve numbers without hyphen in the format yyyymmddxxxx.
     *
     * @param identity to validate
     * @return true if the identity is valid otherwise false.
     */
    private boolean invalidIdentity(String identity) {
        Matcher matcher = IDENTITY_FORMAT_PATTERN.matcher(identity);

        return !matcher.matches() || !personalNumberService.validate(identity);
    }
}
