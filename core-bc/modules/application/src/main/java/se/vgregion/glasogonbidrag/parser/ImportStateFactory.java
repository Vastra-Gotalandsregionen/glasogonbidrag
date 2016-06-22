package se.vgregion.glasogonbidrag.parser;

import se.vgregion.glasogonbidrag.util.DocumentUtil;

import static se.vgregion.glasogonbidrag.parser.ImportActionEvent.*;
import static se.vgregion.glasogonbidrag.model.ImportErrorType.*;

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
    private static ImportHandler newImportHandler(ParseOutputData data) {
        return new ImportHandler(data);
    }

    public static ImportState newImportState() {
        return new ImportStateFactory.StartOfFileState(
                new ParseOutputData());
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
                newImportHandler(getData()).validate(action);
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
                newImportHandler(getData()).document(id, action);
                newImportHandler(getData()).grant(action);

                return new NameAndGrantState(getData());
            }

            if (action.is(EMPTY)) {
                return this;
            }

            if (action.is(ID)) {
                newImportHandler(getData()).error(
                        action, UNEXPECTED_ID,
                        "expected name found id.");
                return new IdState(getData(), action.getRow()[0]);
            } else if (action.is(EOF)) {
                newImportHandler(getData()).error(
                        action, UNEXPECTED_EOF,
                        "expected name reached end of file.");
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
                newImportHandler(getData()).validate(action);
                return new IdState(getData(), action.getRow()[0]);
            }

            if (action.is(EOF)) {
                return new EOFState(getData());
            }

            if (!DocumentUtil.emptyRow(action.getRow())) {
                newImportHandler(getData()).grant(action);
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
