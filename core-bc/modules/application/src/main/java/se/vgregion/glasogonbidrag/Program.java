package se.vgregion.glasogonbidrag;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import se.vgregion.glasogonbidrag.data.Importer;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class Program {
    public static void main(String[] args) throws Exception {
        ExcelImporter importer = null;

        Importer settings = new Importer();
        CmdLineParser parser = new CmdLineParser(settings);
        try {
            parser.parseArgument(args);
            importer = settings.run();
        } catch (CmdLineException e) {
            System.err.println("Exception: " + e.getMessage());
            parser.printUsage(System.err);
        }

        if (importer == null) {
            return;
        }

        importer.benficiaryRun();
    }
}
