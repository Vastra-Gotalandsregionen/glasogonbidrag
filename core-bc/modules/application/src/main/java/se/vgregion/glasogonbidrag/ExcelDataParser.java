package se.vgregion.glasogonbidrag;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import se.vgregion.glasogonbidrag.model.ImportErrorType;
import se.vgregion.glasogonbidrag.parser.IllegalImportStateException;
import se.vgregion.glasogonbidrag.parser.ParseOutputData;
import se.vgregion.glasogonbidrag.model.IdentificationType;
import se.vgregion.glasogonbidrag.parser.ImportAction;
import se.vgregion.glasogonbidrag.parser.ImportState;
import se.vgregion.glasogonbidrag.parser.ImportStateFactory;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.util.IdentificationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static se.vgregion.glasogonbidrag.parser.ImportActionEvent.*;

/**
 * Parser of data in the excel files.
 *
 * @author Martin Lind - Monator Technologies AB
 */
class ExcelDataParser {

    private String password;
    private File file;

    private Map<String, ParseOutputData> data;
    private List<ImportError> fatalErrors;

    ExcelDataParser(File file) {
        this(file, null);
    }

    ExcelDataParser(File file, String password) {
        this.file = file;
        this.password = password;

        this.data = new HashMap<>();
        this.fatalErrors = new ArrayList<>();
    }

    // Getters for data

    public File getFile() {
        return file;
    }

    public Map<String, ParseOutputData> getData() {
        return data;
    }

    void parse() {
        Workbook wb;

        try {
            // Open workbook. If exceptions happens the import will stop
            // for this file.
            wb = workbook();
        } catch (IOException e) {
            System.out.println(String.format(
                    "Exception opening the file %s, got exception %s.",
                    file.toString(),
                    e.getMessage()));

            return;
        } catch (InvalidFormatException e) {
            System.out.println(String.format(
                    "The excel file %s seem to be of the wrong format, " +
                            "got exception %s.",
                    file.toString(),
                    e.getMessage()));

            return;
        }

        int max = wb.getNumberOfSheets();

        for (int i = 0; i < max; i++) {
            Sheet sheet = wb.getSheetAt(i);

            try {
                handle(sheet);
            } catch (IllegalImportStateException e) {
                fatalErrors.add(new ImportError(
                        file.toString(),
                        sheet.getSheetName(),
                        e.getLine(),
                        ImportErrorType.FATAL_ERROR,
                        e.getMessage()));
            }
        }

        if (fatalErrors.size() > 0) {
            handleErrors();
        }
    }

    private void handle(Sheet sheet) throws IllegalImportStateException {
        ImportState state = ImportStateFactory.newImportState();

        String name = sheet.getSheetName();

        int index = 0;
        int max = sheet.getPhysicalNumberOfRows();
        while (index < max) {
            ImportAction action = action(
                    name, index, row(sheet, index), extra(sheet, index));

            state = state.activate(action);

            index += 1;
        }

        ImportAction action = new ImportAction(
                file.getName(), name, max, EOF, null);
        state = state.activate(action);

        if (state.isFinal()) {
            ParseOutputData parsed = state.getData();
            data.put(sheet.getSheetName(), parsed);
        } else {
            fatalErrors.add(new ImportError(
                    file.toString(),
                    sheet.getSheetName(),
                    max,
                    ImportErrorType.FATAL_ERROR,
                    "Reached end of file but not in a final state. " +
                            "this is an error"));
        }
    }

    private ImportAction action(String name,
                                int line,
                                String[] row,
                                String extra) {
        String content = row[0];
        if (!content.isEmpty()) {
            if (IdentificationUtil
                    .detect(content) != IdentificationType.NONE) {
                return new ImportAction(
                        file.getName(), name, line, ID, row, extra);
            } else {
                return new ImportAction(
                        file.getName(), name, line, TEXT, row, extra);
            }
        } else {
            return new ImportAction(
                    file.getName(), name, line, EMPTY, row, extra);
        }
    }

    private Workbook workbook()
            throws InvalidFormatException, IOException {
        Workbook wb;

        if (usePassword()) {
            wb = WorkbookFactory.create(file, password);
        } else {
            wb = WorkbookFactory.create(file);
        }

        return wb;
    }

    private String[] row(Sheet sheet, int row) {
        String[] result = new String[6];
        for (int i = 0; i < 6; i++) result[i] = "";

        Row r = sheet.getRow(row);
        if (r != null) {
            int max = r.getLastCellNum();
            for (int i = 0; i < max && i < 6; i++) {
                result[i] = cell(r, i);
            }
        }

        return result;
    }

    private String extra(Sheet sheet, int row) {
        String result = null;

        Row r = sheet.getRow(row);

        if (r != null) {
            int last = r.getLastCellNum();
            if (last >= 6) {
                // Extra data is located on cell number 6 (zero indexed)
                result = cell(r, 6);
            }
        }

        return result;
    }

    private String cell(Row row, int cell) {
        String result = "";

        Cell c = row.getCell(cell);
        if (c != null) {
            result = value(c);
        }

        return result;
    }

    private String value(Cell cell) {
        String value = "";

        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            value = Boolean.toString(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            value = Double.toString(cell.getNumericCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            value = cell.getCellFormula();
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            value = cell.getStringCellValue();
        }

        return value;
    }

    private boolean usePassword() {
        return password != null;
    }

    private void handleErrors() {
        System.out.println("\n\nFatal errors importing documents:");
        for (ImportError error : fatalErrors) {
            System.err.println(String.format(
                    "Parsing error in %s on sheet \"%s\" on the line %d: %s",
                    error.getFile() + 1,
                    error.getSheet(),
                    error.getLine(),
                    error.getMessage()));
        }
    }
}
