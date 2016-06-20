package se.vgregion.glasogonbidrag.internal;

import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportGrant;
import se.vgregion.glasogonbidrag.util.DocumentUtil;

import static se.vgregion.glasogonbidrag.internal.ImportActionEvent.*;

public class ImportStateFactory {
    public static ImportState newImportState() {
        return new ImportStateFactory.StartOfFileState(new ParseOutputData());
    }

    /**
     *
     */
    public static class StartOfFileState extends AbstractImportState {

        public StartOfFileState(ParseOutputData data) {
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
    public static class IdState extends AbstractImportState {

        private String id;

        public IdState(ParseOutputData data, String id) {
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
    public static class NameAndGrantState extends AbstractImportState {

        public NameAndGrantState(ParseOutputData data) {
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
    public static class EOFState extends AbstractImportState {

        public EOFState(ParseOutputData data) {
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
