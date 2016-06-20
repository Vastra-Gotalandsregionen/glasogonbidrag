package se.vgregion.glasogonbidrag.internal;

public abstract class AbstractImportState implements ImportState {
    private ParseOutputData data;

    public AbstractImportState(ParseOutputData data) {
        this.data = data;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public ParseOutputData getData() {
        return data;
    }
}
