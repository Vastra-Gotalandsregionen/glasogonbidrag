package se.vgregion.glasogonbidrag.writers;

import se.vgregion.glasogonbidrag.library.StorableRepository;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.model.ValidationError;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class FileWriter {

    private File log;

    public FileWriter(String file) {
        log = new File(file);
    }

    public void logErrors(StorableRepository repo) throws IOException {
        if (!log.exists()) {
            if (!log.createNewFile()) {
                System.out.println("Couldn't create file.");
                return;
            }
        }

        PrintWriter writer = new PrintWriter(log, "UTF-8");

        validationErrors(writer, repo.getValidationErrors());
        importErrors(writer, repo.getImportErrors());

        writer.close();
    }

    private void validationErrors(PrintWriter writer,
                                  List<ValidationError> errors) {
        if (errors.isEmpty()) {
            return;
        }

        Collections.sort(errors, new Comparator<ValidationError>() {
            @Override
            public int compare(ValidationError o1, ValidationError o2) {
                int file = o1.getFile().compareTo(o2.getFile());
                int sheet = o1.getSheet().compareTo(o2.getSheet());
                int line = Integer.compare(o1.getLine(), o2.getLine());

                return file * 100 + sheet * 10 + line;
            }
        });

        writer.println(String.format(
                "There where %s validation errors:", errors.size()));
        for (ValidationError error : errors) {
            writer.println(format(error));
        }

        writer.println();
    }

    private void importErrors(PrintWriter writer, List<ImportError> errors) {
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

        writer.println(String.format(
                "There where %s import errors:", errors.size()));

        for (ImportError error : errors) {
            writer.println(format(error));
        }

        writer.println();
    }

    private String format(ValidationError error) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(
                "Parsing error in %s on sheet \"%s\" on the line %d: %s",
                error.getFile(),
                error.getSheet(),
                error.getLine() + 1,
                error.getMessage()));

        if (!error.shouldStore()) {
            builder.append(" [not saved]");
        }

        return builder.toString();
    }

    private String format(ImportError error) {
        return String.format(
                "Parsing error in %s on sheet \"%s\" on the line %d: %s %s",
                error.getFile(),
                error.getSheet(),
                error.getLine() + 1,
                error.getError(),
                error.getMessage());
    }
}
