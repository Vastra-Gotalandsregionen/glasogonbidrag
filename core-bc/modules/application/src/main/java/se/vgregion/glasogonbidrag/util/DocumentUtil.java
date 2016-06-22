package se.vgregion.glasogonbidrag.util;

import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportGrant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for helping of building Import Document and Import Grant.
 * Also helpers for related functionality.
 * 
 * @author Martin Lind - Monator Technologies AB
 */
public final class DocumentUtil {

    /**
     * Prevent initialization of this class.
     */
    private DocumentUtil() {}

    /**
     * Build a local grant object form an row, the fields in the array
     * have the following mapping:
     *
     * row[0] contains the name of the person (document)
     *
     * The id is present on a previous row.
     *
     * @param id the id of the document.
     * @param row data for an import document.
     * @return an import grant that should be converted to
     *         domain objects and inserted in the database.
     */
    public static ImportDocument document(String id, String[] row) {
        return document(id, row[0]);
    }

    /**
     * Build a local grant object form an array of strings, the fields in
     * the array have the following mapping:
     *
     * row[1] contains date to set as prescription date.
     * row[2] contains date to set as delivery date.
     * row[3] contains amount including vat to set as amount for date.
     * row[4] contains the invoice number, this may be blank.
     * row[5] contains the verification number.
     *
     * @param row data for an import grant.
     * @return an import grant that should be converted
     */
    public static ImportGrant grant(String[] row) {
        return grant(row[1], row[2], row[3], row[4], row[5]);
    }

    /**
     * Build a local grant object form a strings.
     *
     * @param prescriptionDate date to set as prescription date.
     * @param deliveryDate date to set as delivery date.
     * @param amount amount including vat to set as amount for date
     * @param invoiceNumber the invoice number, this may be blank
     * @param verificationNumber the verification number
     * @return an import grant that should be converted to
     *         domain objects and inserted in the database.
     */
    private static ImportGrant grant(String prescriptionDate,
                                     String deliveryDate,
                                     String amount,
                                     String invoiceNumber,
                                     String verificationNumber) {
        return new ImportGrant(
                prescriptionDate, deliveryDate,
                amount, invoiceNumber, verificationNumber);
    }

    /**
     * Build a local document object from strings.
     *
     * @param id the id of this document
     * @param name the name of this document
     * @return an import document that should be converted to
     *         domain objects and inserted in the database.
     */
    private static ImportDocument document(String id, String name) {
        return new ImportDocument(id, name);
    }

    /**
     * The comment (column 7) in an row may contain a text with the
     * verification of the replacement invoice, this method will extract
     * this verification number.
     *
     * A verification number should contain 8 numbers and no characters.
     *
     * @param comment the comment that contains the verification number.
     * @return verification number as a string.
     */
    public static String extractVerification(String comment) {
        Pattern verificationPatter = Pattern.compile(".*(?<ver>[0-9]{8}).*");
        Matcher matcher = verificationPatter.matcher(comment);

        if (matcher.matches()) {
            return matcher.group("ver");
        }

        return "";
    }

    /**
     * Check if all strings in the array are empty.
     * This method will not check if a string only contains whitespace.
     *
     * @param row should be a array with all fields initialized.
     * @return true if all strings are empty
     * @throws NullPointerException if any of the of the strings are null.
     */
    public static boolean emptyRow(String[] row) {
        boolean empty = true;
        for (int i = 1; i < row.length; i++) {
            empty = empty & row[i].isEmpty();
        }
        return empty;
    }

    /**
     * Check if all strings in the array are empty.
     * This method will treat whitespace as an empty string.
     *
     * @param row should be a array with all fields initialized.
     * @return true if all strings are empty
     * @throws NullPointerException if any of the of the strings are null.
     */
    public static boolean emptyOrWhitespace(String[] row) {
        boolean empty = true;
        for (int i = 1; i < row.length; i++) {
            empty = empty & row[i].trim().isEmpty();
        }
        return empty;
    }
}
