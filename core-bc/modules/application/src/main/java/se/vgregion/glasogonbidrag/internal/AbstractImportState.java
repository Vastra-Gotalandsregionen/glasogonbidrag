package se.vgregion.glasogonbidrag.internal;

public abstract class AbstractImportState implements ImportState {
    @Override
    public boolean isFinal() {
        return false;
    }
}
