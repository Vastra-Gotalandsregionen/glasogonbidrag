package se.vgregion.glasogonbidrag.internal;

import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportGrant;
import se.vgregion.glasogonbidrag.util.DocumentUtil;

import static se.vgregion.glasogonbidrag.internal.ImportActionEvent.*;

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
                ImportDocument doc = DocumentUtil.document(id, action.getRow());
                ImportGrant grant = DocumentUtil.grant(action.getRow());

                getData().addDocument(doc);
                getData().addGrant(grant);

                return new NameAndGrantState(getData());
            }

            if (action.is(EMPTY)) {
                return this;
            }

            if (action.is(ID)) {
                throw new IllegalImportStateException(action.getLine(),
                        "expected name found id.");
            } else if (action.is(EOF)) {
                throw new IllegalImportStateException(action.getLine(),
                        "expected name reached end of file.");
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
