package se.vgregion.glasogonbidrag.parser;

import se.vgregion.glasogonbidrag.model.ImportDataLibrary;
import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.model.ImportErrorType;
import se.vgregion.glasogonbidrag.model.ImportGrant;
import se.vgregion.glasogonbidrag.util.DocumentUtil;
import se.vgregion.glasogonbidrag.util.ImportValidationUtil;

import static se.vgregion.glasogonbidrag.parser.ImportActionEvent.*;

/**
 * A finite state machine for parsing excel file of the quasi-structured
 * data format in repeating pattern
 *
 * <pre>
 * {@code
 * +------+
 * | id   |
 * +------+--------------+----------+--------+---------+--------------+
 * |      | prescription | delivery |        | invoice | verification |
 * | name |    date      |   date   | amount |   nr    |      nr      |
 * +------+--------------+----------+--------+---------+--------------+
 *                                     .
 *                                     .
 *                                     .
 *        +--------------+----------+--------+---------+--------------+
 *        | prescription | delivery |        | invoice | verification |
 *        |    date      |   date   | amount |   nr    |      nr      |
 *        +--------------+----------+--------+---------+--------------+
 * }
 * </pre>
 *
 * implemented as a pushdown automaton.
 *
 * @author Martin Lind - Monator Technologies AB
 */
public class ImportStateFactory {
    public static ImportState newImportState() {
        return new ImportStateFactory.StartOfFileState(new ParseOutputData());
    }

    /**
     *
     */
    private static class StartOfFileState extends AbstractImportState {

        StartOfFileState(ParseOutputData data) {
            super(data);
        }

        @Override
        public ImportState activate(ImportAction action) {
            if (action.is(ID)) {
                return new IdState(getData(), action.getRow()[0]);
            }

            if (action.is(EOF)) {
                return new EOFState(getData());
            }

            return this;
        }
    }

    /**
     *
     */
    private static class IdState extends AbstractImportState {

        private String id;

        IdState(ParseOutputData data, String id) {
            super(data);
            this.id = id;
        }

        @Override
        public ImportState activate(ImportAction action) {
            if (action.is(TEXT)) {
                ImportDocument doc = DocumentUtil.document(
                        id, action.getRow());
                ImportGrant grant = DocumentUtil.grant(action.getRow());

                getData().addDocument(doc);
                if(!ImportValidationUtil.verificationNumber(action)) {
                    String verificationNumber =
                            ImportDataLibrary.dummyVerificationNumber();

                    if (ImportValidationUtil.comment(action) &&
                            ImportValidationUtil
                                    .verificationInComment(action)) {
                        verificationNumber = DocumentUtil
                                .extractVerification(action.getExtraData());
                    }

                    grant = new ImportGrant(
                            grant.getPrescriptionDate(),
                            grant.getDeliveryDate(),
                            grant.getAmount(),
                            grant.getInvoiceNumber(),
                            verificationNumber);
                }
                getData().addGrant(grant);

                return new NameAndGrantState(getData());
            }

            if (action.is(EMPTY)) {
                return this;
            }

            if (action.is(ID)) {
                ImportError error = new ImportError(
                        action.getFile(),
                        action.getSheet(),
                        action.getLine(),
                        ImportErrorType.UNEXPECTED_ID,
                        "expected name found id.");
                getData().addError(error);

                return new IdState(getData(), action.getRow()[0]);
            } else if (action.is(EOF)) {
                ImportError error = new ImportError(
                        action.getFile(),
                        action.getSheet(),
                        action.getLine(),
                        ImportErrorType.UNEXPECTED_EOF,
                        "expected name reached end of file.");
                getData().addError(error);

                return new EOFState(getData());
            }

            throw new IllegalImportStateException(action.getLine(),
                    "unknown error");
        }

    }

    /**
     *
     */
    private static class NameAndGrantState extends AbstractImportState {

        NameAndGrantState(ParseOutputData data) {
            super(data);
        }

        @Override
        public ImportState activate(ImportAction action) {
            if (action.is(ID)) {
                return new IdState(getData(), action.getRow()[0]);
            }

            if (action.is(EOF)) {
                return new EOFState(getData());
            }

            if (!DocumentUtil.emptyRow(action.getRow())) {
                ImportGrant grant = DocumentUtil.grant(action.getRow());
                getData().addGrant(grant);


            }


            return this;
        }
    }

    /**
     *
     */
    private static class EOFState extends AbstractImportState {

        EOFState(ParseOutputData data) {
            super(data);
        }

        @Override
        public ImportState activate(ImportAction action) {
            throw new IllegalImportStateException(
                    0,
                    "EOF state already reached");
        }

        @Override
        public boolean isFinal() {
            return true;
        }
    }
}
