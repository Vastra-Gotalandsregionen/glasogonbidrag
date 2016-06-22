package se.vgregion.glasogonbidrag;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;
import se.vgregion.glasogonbidrag.model.ImportDataLibrary;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.parser.ParseOutputData;

import java.io.File;
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
            usage = "The file(s) that should be imported.")
    private List<String> files;

    void run() {
        ImportDataLibrary library = new ImportDataLibrary();

        for (File file : getFiles()) {
            ExcelDataParser parser =
                    new ExcelDataParser(file, password);
            parser.run();

            Map<String, ParseOutputData> data = parser.getData();
            library.catalog(data);
        }

        handleErrors(library.getErrors());
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

    private void handleErrors(List<ImportError> errors) {
        if (errors.isEmpty()) {
            return;
        }

        Collections.sort(errors, new Comparator<ImportError>() {
            @Override
            public int compare(ImportError o1, ImportError o2) {
                int file = o1.getFile().compareTo(o2.getFile());
                int sheet = o1.getSheet().compareTo(o2.getSheet());
                int line = Integer.compare(o1.getLine(), o2.getLine());

                return file * 100 + sheet * 10 + line;
            }
        });

        System.out.println(String.format("\nFound %d errors in one or more files when " +
                "importing documents:", errors.size()));
        for (ImportError error : errors) {
            System.err.println(String.format(
                    "Parsing error in %s on sheet \"%s\" on the line %d: %s",
                    error.getFile() + 1,
                    error.getSheet(),
                    error.getLine(),
                    error.getMessage()));
        }
    }
}
