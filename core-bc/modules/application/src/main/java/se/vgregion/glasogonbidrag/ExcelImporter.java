package se.vgregion.glasogonbidrag;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.WorkbookUtil;
import se.vgregion.glasogonbidrag.internal.IdentificationUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ExcelImporter {

    private enum NumberType {
        VALID, INVALID, RESERVE;
    }

    private String password;
    private File file;

    public ExcelImporter(String password, File file) {
        this.password = password;
        this.file = file;
    }

    private boolean shouldUsePassword() {
        return password != null;
    }

    public void benficiaryRun() throws Exception {
        Workbook wb;

        if (shouldUsePassword()) {
            wb = WorkbookFactory.create(file, password);
        } else {
            wb = WorkbookFactory.create(file);
        }

        int sheets = wb.getNumberOfSheets();

        Map<String, NumberType> numbers = new HashMap<>();

        for (int i = 1; i < sheets; i++) {
            Sheet sheet = wb.getSheetAt(i);

            int currentRow = 0;
            int totalRows = sheet.getPhysicalNumberOfRows();

            while (currentRow < totalRows) {
                Row row = sheet.getRow(currentRow);

                if (row != null) {
                    Cell cell = row.getCell(0);
                    if (cell != null) {
                        String number = cell.toString();
                        if (IdentificationUtil.detectPersonalNumber(number)) {
                            if (IdentificationUtil.validatePersonalNumber(number)) {
                                numbers.put(number, NumberType.VALID);
                            } else {
                                numbers.put(number, NumberType.INVALID);
                            }
                        } else if (IdentificationUtil.detectReservedNumber(number)) {
                            numbers.put(number, NumberType.RESERVE);
                        }
                    }
                }

                currentRow += 1;
            }
        }

        int nonValid = 0;

        System.out.println("Invalid & Reserved ID-numbers:");
        for (String number : numbers.keySet()) {
            if (numbers.get(number) == NumberType.INVALID) {
                System.out.println(number + " (Invalid ID)!");
                nonValid += 1;
            } else if (numbers.get(number) == NumberType.RESERVE) {
                System.out.println(number + " (Reserved ID)!");
                nonValid += 1;
            }
        }

        System.out.println("Total number of id numbers: " + numbers.size() +
                " which of " + nonValid + " where not valid.");
    }

    public void run() throws Exception {
        File xls = new File("./file.xlsx");

        Workbook wb = WorkbookFactory.create(xls, "");
        Sheet sheet = wb.getSheet("2008");

        int totalRows = sheet.getPhysicalNumberOfRows();

        int beneficiariesCount = 0;
        int grantCount = 0;

        int row = findFirstRow(sheet);
        while (row < totalRows || row != -1) {
            row = findNextRow(sheet, row);
            if (row == -1) {
                System.out.println("ERROR!");
                return;
            }

            int rows = rowsForGrant(sheet, row);

            String[] b = beneficiary(sheet, row, rows);
            List<String[]> gs = grants(sheet, row, rows);

            if (b == null) {
                System.out.println("Didn't find name");
                return;
            }

            if (gs == null) {
                System.out.println("didn't find grants");
                return;
            }

            // Output
            for (String[] g : gs) {
                System.out.println(String.format(
                        "Grant: recipe-date %s, delivery-date %s, " +
                                "amount %s, invoice %s, verifcation %s",
                        g[0], g[1], g[2], g[3], g[4]));
                grantCount += 1;
            }

            // Advance `rows` number of rows.
            row += rows;

            beneficiariesCount += 1;
        }

        System.out.println(
                "Found" + beneficiariesCount + " beneficiaries. " +
                        "Found " + grantCount + " grants.");
    }

    private String[] beneficiary(Sheet sheet, int first, int rows) {
        if (rows < 2) {
            return null;
        }

        String[] str = new String[2];

        Row idRow = sheet.getRow(first);
        Row nameRow = sheet.getRow(first + 1);

        Cell idCell = idRow.getCell(0);
        Cell nameCell = nameRow.getCell(0);

        if (idCell != null) {
            str[0] = idCell.toString();
        } else {
            str[0] = "";
        }

        if (nameCell != null) {
            str[1] = nameCell.toString();
        } else {
            str[0] = "";
        }

        return str;
    }

    private List<String[]> grants(Sheet sheet, int first, int rows) {
        if (rows < 2) {
            return null;
        }

        List<String[]> gs = new ArrayList<>();

        int i = 1;
        while (i < rows) {
            Row row = sheet.getRow(first + i);
            if (!emptyRow(row)) {
                gs.add(grant(row));
            }
            i += 1;
        }

        return gs;
    }

    private String[] grant(Row row) {
        String[] g = new String[5];

        if (row == null) {
            return new String[] {"", "", "", "", ""};
        }

        for (int i = 0; i < g.length; i++) {
            Cell cell = row.getCell(i + 1);

            if (cell != null) {
                g[i] = cell.toString();
            } else {
                g[i] = "";
            }
        }

        return g;
    }

    private int rowsForGrant(Sheet sheet, int first) {
        Row row = sheet.getRow(first);
        Cell cell = row.getCell(0);

        int next = 0;
        if (cell != null &&
                IdentificationUtil.detectPersonalNumber(cell.toString())) {
            next = findNextRow(sheet, first + 1);
        }

        if (next == -1) {
            return -1;
        }

        return next - first;
    }

    private int findFirstRow(Sheet sheet) {
        return findNextRow(sheet, 0);
    }

    private int findNextRow(Sheet sheet, int rowIndex) {
        int rows = sheet.getPhysicalNumberOfRows();

        int firstRow = -1;

        Row row;
        Cell cell = null;

        // Find first row
        int i = rowIndex;
        while (i < rows && firstRow == -1) {
            row = sheet.getRow(i);
            if (row != null) {
                cell = row.getCell(0);
            }

            if (cell != null && IdentificationUtil.detectPersonalNumber(cell.toString())) {
                firstRow = i;
            }

            i += 1;

            if (i == rows) {
                firstRow = i;
            }
        }

        return firstRow;
    }

    private boolean emptyRow(Row row) {
        if (row == null) {
            return false;
        }

        int max = row.getPhysicalNumberOfCells();

        boolean[] cells = new boolean[max];

        for (int i = 0; i < max; i++) {
            Cell cell = row.getCell(i);
            boolean value = false;
            if (cell != null) {
                value = cell.toString().trim().isEmpty();
            }
            cells[i] = value;
        }

        boolean all = true;

        for (boolean cellValue : cells) {
            all = all & cellValue;
        }

        return all;
    }

}