package se.vgregion.glasogonbidrag.internal;

/**
 * @author Martin Lind - Monator Technologies AB
 */
abstract class AbstractImportState implements ImportState {
    private ParseOutputData data;

    AbstractImportState(ParseOutputData data) {
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
