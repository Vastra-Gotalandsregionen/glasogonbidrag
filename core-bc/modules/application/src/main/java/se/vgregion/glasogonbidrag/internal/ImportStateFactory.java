package se.vgregion.glasogonbidrag.internal;

import se.vgregion.glasogonbidrag.model.ImportId;

import static se.vgregion.glasogonbidrag.internal.ImportActionEvent.*;

public class ImportStateFactory {
    public ImportState newImportState() {
        return new StartOfFileState();
    }

    public class StartOfFileState extends AbstractImportState {
        @Override
        public ImportState activate(ImportAction action) {
            if (action.is(ID)) {
                return new FirstIdState(action.getContent());
            }

            if (action.is(EOF)) {
                return new EOFState(null);
            }

            return this;
        }
    }

    public class FirstIdState extends AbstractImportState {
        ImportId current;

        public FirstIdState(String content) {
            current = new ImportId();
            current.setId(content);
        }

        @Override
        public ImportState activate(ImportAction action) {
            if (action.is(NAME)) {
                return new NameAndValueState(current, action.getContent());
            }

            if (action.is(EMPTY)) {
                return this;
            }

            throw new IllegalImportStateException(action.getLine(), "");
        }
    }

    public class NameAndValueState extends AbstractImportState {
        private ImportId current;

        public NameAndValueState(ImportId current, String name) {
            this.current = current;
            this.current.setName(name);
        }

        @Override
        public ImportState activate(ImportAction action) {
            if (action.is(ID)) {
                return new IdState(this.current, action.getContent());
            }

            if (action.is(EOF)) {
                return new EOFState(this.current);
            }

            return null;
        }
    }

    public class IdState extends AbstractImportState {
        private ImportId current;
        private ImportId finished;

        public IdState(ImportId finished, String id) {
            current = new ImportId();
            current.setId(id);

            this.finished = finished;
        }

        @Override
        public ImportState activate(ImportAction action) {
            return null;
        }

        public ImportId getFinished() {
            return finished;
        }
    }

    public class EOFState extends AbstractImportState {
        private ImportId finished;

        public EOFState(ImportId finished) {
            this.finished = finished;
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

        public ImportId getFinished() {
            return finished;
        }
    }
}
