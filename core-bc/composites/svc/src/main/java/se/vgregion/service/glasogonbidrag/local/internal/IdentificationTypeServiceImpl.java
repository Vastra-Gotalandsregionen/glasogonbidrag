package se.vgregion.service.glasogonbidrag.local.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.service.glasogonbidrag.local.api.IdentificationTypeService;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberFormatService;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberService;

import java.util.regex.Pattern;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class IdentificationTypeServiceImpl implements IdentificationTypeService {
    private static final Pattern personalNumberRegex;
    private static final Pattern reserveNumberRegex;
    private static final Pattern lmaNumberRegex;
    static {
        personalNumberRegex = Pattern.compile(
                "(?<year>[0-9]{2})?" +
                        "(?<number>[0-9]{6})[\\-]?(?<control>[0-9]{4})");
        reserveNumberRegex = Pattern.compile(
                "(?<year>[0-9]{2})?(?<number>[0-9]{6}[\\-]?[A-Z0-9]{4})");
        lmaNumberRegex = Pattern.compile("[0-9]{2}[\\-]?[0-9]{6}");
    }

    @Autowired
    private PersonalNumberService numberService;

    @Autowired
    private PersonalNumberFormatService formatService;

    @Override
    public IdentificationType detect(String number, String currentYear) {
        if (personalNumberRegex.matcher(number).matches()) {
            String internalFormat = formatService.to(number, currentYear);
            if (numberService.validate(internalFormat)) {
                return IdentificationType.PERSONAL;
            } else {
                return IdentificationType.RESERVE;
            }
        }

        if (reserveNumberRegex.matcher(number).matches()) {
            return IdentificationType.RESERVE;
        }

        if (lmaNumberRegex.matcher(number).matches()) {
            return IdentificationType.LMA;
        }

        return IdentificationType.OTHER;
    }
}
