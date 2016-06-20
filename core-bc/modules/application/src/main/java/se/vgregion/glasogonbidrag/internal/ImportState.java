package se.vgregion.glasogonbidrag.internal;

public interface ImportState {
    ImportState activate(ImportAction action);
    boolean isFinal();
    ParseOutputData getData();
}
