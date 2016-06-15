package se.vgregion.glasogonbidrag.internal;

public class ImportAction {
    private int line;
    private ImportActionEvent action;
    private String content;

    public ImportAction(int line, ImportActionEvent action, String content) {
        this.line = line;
        this.action = action;
        this.content = content;
    }

    public int getLine() {
        return line;
    }

    public ImportActionEvent getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }

    public boolean is(ImportActionEvent action) {
        return action == this.action;
    }
}
