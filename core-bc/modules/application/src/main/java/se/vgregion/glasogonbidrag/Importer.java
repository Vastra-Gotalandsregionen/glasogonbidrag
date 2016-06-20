package se.vgregion.glasogonbidrag;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
//TODO: implement this: http://stackoverflow.com/questions/23800070/multiple-args-with-arg4j
public class Importer {
    @Option(name = "-password", usage = "The file is password protected.")
    private String password;
    @Option(name = "-file", handler = StringArrayOptionHandler.class,
            usage = "The file(s) that should be imported.")
    private List<String> files;

    public void run() {
        ExcelDataParser parser = new ExcelDataParser(password, false);

        for (File file : getFiles()) {
            parser.run(file);
        }
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
}
