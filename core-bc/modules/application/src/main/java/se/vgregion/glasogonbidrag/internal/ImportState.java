package se.vgregion.glasogonbidrag.internal;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface ImportState {
    ImportState activate(ImportAction action);
    boolean isFinal();
    ParseOutputData getData();
}
