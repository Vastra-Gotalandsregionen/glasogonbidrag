package se.vgregion.glasogonbidrag;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class Program {
    public static void main(String[] args) throws Exception {
        Importer settings = new Importer();
        CmdLineParser parser = new CmdLineParser(settings);
        try {
            parser.parseArgument(args);
            settings.run();
        } catch (CmdLineException e) {
            System.err.println("Exception: " + e.getMessage());
            parser.printUsage(System.err);
        }
    }
}
