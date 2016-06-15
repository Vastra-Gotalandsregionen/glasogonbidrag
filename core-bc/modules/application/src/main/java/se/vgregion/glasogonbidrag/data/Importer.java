package se.vgregion.glasogonbidrag.data;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;
import se.vgregion.glasogonbidrag.ExcelImporter;

import java.io.File;

/**
 * Created by martlin on 2016/06/15.
 */
public class Importer {
    @Option(name = "-password", usage = "The file is password protected.")
    private String password;
    private File file;

    @Option(name = "-file", usage = "The file that should be imported.")
    public void setFile(File f) {
        if (f.exists()) file = f;
    }

    public ExcelImporter run() {
        if (file == null) {
            throw new RuntimeException("File need to be sett.");
        }

        return new ExcelImporter(password, file);
    }
}
