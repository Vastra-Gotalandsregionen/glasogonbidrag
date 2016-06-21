package se.vgregion.glasogonbidrag.util;

import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportGrant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public final class DocumentUtil {
    private DocumentUtil() {

    }

    public static ImportDocument document(String id, String[] row) {
        return document(id, row[0]);
    }

    private static ImportDocument document(String id, String name) {
        return new ImportDocument(id, name);
    }

    public static ImportGrant grant(String[] row) {
        return grant(row[1], row[2], row[3], row[4], row[5]);
    }

    public static String extractVerification(String comment) {
        Pattern verificationPatter = Pattern.compile(".*(?<ver>[0-9]{8}).*");
        Matcher matcher = verificationPatter.matcher(comment);

        if (matcher.matches()) {
            return matcher.group("ver");
        }

        return "";
    }

    private static ImportGrant grant(String prescriptionDate,
                                     String deliveryDate,
                                     String amount,
                                     String invoiceNumber,
                                     String verificationNumber) {
        return new ImportGrant(
                prescriptionDate, deliveryDate,
                amount, invoiceNumber, verificationNumber);
    }

    public static boolean emptyRow(String[] row) {
        boolean empty = true;
        for (int i = 1; i < row.length; i++) {
            empty = empty & row[i].isEmpty();
        }
        return empty;
    }
}
