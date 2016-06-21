package se.vgregion.glasogonbidrag;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import java.util.List;

import static se.vgregion.glasogonbidrag.parser.ImportActionEvent.*;

class ExcelDataParser {

    private String password;
    private List<ImportError> errors;
    private File currentWorkingFile;
    private boolean debugOutput;

    ExcelDataParser() {
        this(null, false);
    }

    ExcelDataParser(String password, boolean debugOutput) {
        this.password = password;
        this.debugOutput = debugOutput;

        this.errors = new ArrayList<>();
    }

    void run(File file) {
        currentWorkingFile = file;

        Workbook wb;

        try {
            wb = workbook();
        } catch (IOException e) {
            System.out.println(String.format(
                    "Exception opening the file %s, got exception %s.",
                    file.toString(), e.getMessage()));
            return;
        } catch (InvalidFormatException e) {
            System.out.println(String.format(
                    "The excel file %s seem to be of the wrong format, " +
                            "got exception %s.",
                    file.toString(), e.getMessage()));
            return;
        }

        int max = wb.getNumberOfSheets();

        for (int i = 0; i < max; i++) {
            Sheet sheet = wb.getSheetAt(i);

            if (debugOutput) {
                System.out.println(String.format(
                        "Starting processing sheet: %s.",
                        sheet.getSheetName()));
            }

            try {
                handle(sheet);
            } catch (IllegalImportStateException e) {
                if (debugOutput) {
                    System.out.println(String.format(
                            "Parsing error in the %s on the line %d:",
                            sheet.getSheetName(), e.getLine()));
                    System.out.println(String.format("Error: %s", e.getMessage()));
                }

                errors.add(new ImportError(
                        currentWorkingFile.toString(),
                        sheet.getSheetName(),
                        e.getLine(),
                        e.getMessage()));
            }
        }

        if (errors.size() > 0) {
            handleErrors();
        }
    }

    private void handleErrors() {
        System.out.println("\n\nThere where errors importing documents:");
        for (ImportError error : errors) {
            System.err.println(String.format(
                    "Parsing error in %s on sheet \"%s\" on the line %d: %s",
                    error.getFile(),
                    error.getSheet(),
                    error.getLine(),
                    error.getMessage()));
        }
    }

    private void handle(Sheet sheet) throws IllegalImportStateException {
        ImportState state = ImportStateFactory.newImportState();

        int index = 0;
        int max = sheet.getPhysicalNumberOfRows();
        while (index < max) {
            ImportAction action = action(
                    index, row(sheet, index));

            state = state.activate(action);

            index += 1;
        }

        ImportAction action = new ImportAction(max, EOF, null);
        state = state.activate(action);

        if (state.isFinal()) {
            ParseOutputData data = state.getData();

            System.out.println(String.format(
                    "Sheet \"%s\" in file %s imported correctly! " +
                            "Found %d documents with a total of %d grants.",
                    sheet.getSheetName(),
                    currentWorkingFile,
                    data.getDocuments().size(),
                    data.sumGrants()));
        } else {
            if (debugOutput) {
                System.out.println("Warn!");
            }

            errors.add(new ImportError(
                    currentWorkingFile.toString(),
                    sheet.getSheetName(),
                    max,
                    "Reached end of file but not in a final state. " +
                            "this is an error"));
        }
    }

    private ImportAction action(int line, String[] row) {
        String content = row[0];
        if (!content.isEmpty()) {
            if (IdentificationUtil.detect(content) != IdentificationType.NONE) {
                return new ImportAction(line, ID, row);
            } else {
                return new ImportAction(line, TEXT, row);
            }
        } else {
            return new ImportAction(line, EMPTY, row);
        }
    }

    private Workbook workbook()
            throws InvalidFormatException, IOException {
        Workbook wb;

        if (usePassword()) {
            wb = WorkbookFactory.create(currentWorkingFile, password);
        } else {
            wb = WorkbookFactory.create(currentWorkingFile);
        }

        return wb;
    }

    private String[] row(Sheet sheet, int row) {
        String[] result = new String[6];
        for (int i = 0; i < 6; i++) result[i] = "";

        Row r = sheet.getRow(row);
        if (r != null) {
            int max = r.getPhysicalNumberOfCells();
            for (int i = 0; i < max && i < 6; i++) {
                result[i] = cell(r, i);
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
}
