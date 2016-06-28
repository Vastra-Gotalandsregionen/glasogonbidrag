package se.vgregion.glasogonbidrag.util;

import se.vgregion.glasogonbidrag.parser.ImportAction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class ImportValidationUtil {
    public static boolean verificationNumber(ImportAction action) {
        String[] row = action.getRow();
        return (row != null) && (row[5] == null || row[5].isEmpty());
    }

    public static boolean comment(ImportAction action) {
        return action.getExtraData() != null;
    }

    public static boolean verificationInComment(ImportAction action) {
        String comment = action.getExtraData();

        Pattern verificationPatter = Pattern.compile(".*(?<ver>[0-9]{8}).*");
        Matcher matcher = verificationPatter.matcher(comment);

        return matcher.matches();
    }
}
