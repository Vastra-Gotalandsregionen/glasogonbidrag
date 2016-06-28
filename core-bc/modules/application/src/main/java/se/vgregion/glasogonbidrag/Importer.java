package se.vgregion.glasogonbidrag;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;
import se.vgregion.glasogonbidrag.library.StorableRepositoryImpl;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.parser.ParseOutputData;
import se.vgregion.glasogonbidrag.writers.DatabaseWriter;
import se.vgregion.glasogonbidrag.writers.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class Importer {
    @Option(name = "-password", usage = "The file is password protected.")
    private String password;
    @Option(name = "-file", handler = StringArrayOptionHandler.class,
            usage = "The file(s) that should be imported.",
            required = true)
    private List<String> files;
    @Option(name = "-silent",
            usage = "Set this if you want to suppress all output to " +
                    "stderr and stdout.")
    private boolean silent;

    void run() {
        StorableRepositoryImpl repo = new StorableRepositoryImpl();

        if (!silent) {
            System.out.println("Starting");
        }

        for (File file : getFiles()) {
            ExcelDataParser parser =
                    new ExcelDataParser(file, password);
            parser.parse();

            Map<String, ParseOutputData> data = parser.getData();
            repo.catalog(data);
        }

        DatabaseWriter db = new DatabaseWriter();
        db.run(repo);

        FileWriter writer = new FileWriter("import.log");
        try {
            writer.logErrors(repo);
        } catch (IOException e) {
            if (!silent) {
                System.err.println("Couldn't write log. " + e.getMessage());
            }
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private List<File> getFiles() {
        List<File> fileList = new ArrayList<>();

        for (String file : files) {
            File f = new File(file);

            if (f.exists()) {
                fileList.add(f);
            }
        }

        return fileList;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }
}
