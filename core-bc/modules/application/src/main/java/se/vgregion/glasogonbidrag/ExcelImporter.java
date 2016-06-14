package se.vgregion.glasogonbidrag;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.WorkbookUtil;
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

    public void benficiaryRun() throws Exception {
        File xls = new File("./file.xlsx");

        Workbook wb = WorkbookFactory.create(xls, "");
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
                        if (detectPersonalNumber(number)) {
                            if (validatePersonalNumber(number)) {
                                numbers.put(number, NumberType.VALID);
                            } else {
                                numbers.put(number, NumberType.INVALID);
                            }
                        } else if (detectReservedNumber(number)) {
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

        Workbook wb = WorkbookFactory.create(xls, "synverksamheten");
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
            System.out.println("Grant's id: " + padWithCentury(b[0]) +
                    ", with name: " + b[1]);
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


//        // First
//
//        int rows = rowsForGrant(sheet, first);
//        System.out.println("Grant have : " + rows);
//
//        String[] b = beneficiary(sheet, first, rows);
//        if (b == null) {
//            System.out.println("Didn't find name");
//            return;
//        }
//
//        List<String[]> gs = grants(sheet, first, rows);
//        if (gs == null) {
//            System.out.println("didn't find grants");
//            return;
//        }
//
//        for (String[] g : gs) {
//            System.out.println(String.format(
//                    "Grant: recipe-date %s, delivery-date %s, " +
//                            "amount %s, invoice %s, verifcation %s",
//                    g[0], g[1], g[2], g[3], g[4]));
//        }
//
//        // Other loop!
//
//        first = findNextRow(sheet, first + rows);
//        System.out.println("Found second number: " +
//                padWithCentury(cell.toString()));
//
//        rows = rowsForGrant(sheet, first);
//        System.out.println("Grant have : " + rows);
//
//        b = beneficiary(sheet, first, rows);
//        if (b == null) {
//            System.out.println("Didn't find name");
//            return;
//        }
//        System.out.println("Grant's id: " + padWithCentury(b[0]) +
//                ", with name: " + b[1]);
//
//        gs = grants(sheet, first, rows);
//        if (gs == null) {
//            System.out.println("didn't find grants");
//            return;
//        }
//
//        for (String[] g : gs) {
//            System.out.println(String.format(
//                    "Grant: recipe-date %s, delivery-date %s, " +
//                            "amount %s, invoice %s, verifcation %s",
//                    g[0], g[1], g[2], g[3], g[4]));
//        }
//
//        // Third loop!
//
//        first = findNextRow(sheet, first + rows);
//        System.out.println("Found second number: " +
//                padWithCentury(cell.toString()));
//
//        rows = rowsForGrant(sheet, first);
//        System.out.println("Grant have : " + rows);
//
//        b = beneficiary(sheet, first, rows);
//        if (b == null) {
//            System.out.println("Didn't find name");
//            return;
//        }
//        System.out.println("Grant's id: " + padWithCentury(b[0]) +
//                ", with name: " + b[1]);
//
//        gs = grants(sheet, first, rows);
//        if (gs == null) {
//            System.out.println("didn't find grants");
//            return;
//        }
//
//        for (String[] g : gs) {
//            System.out.println(String.format(
//                    "Grant: recipe-date %s, delivery-date %s, " +
//                            "amount %s, invoice %s, verifcation %s",
//                    g[0], g[1], g[2], g[3], g[4]));
//        }
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
        if (cell != null && detectPersonalNumber(cell.toString())) {
            next = findNextRow(sheet, first + 1);
        }

        if (next == -1) {
            return -1;
        }

        return next - first;
    }

    private String padWithCentury(String number) {
        if (number == null) {
            return "";
        }

        int length = number.length() - 1;

        StringBuilder builder = new StringBuilder();

        if (length == 10) {
            int year = Integer.parseInt(number.substring(0, 2));
            if (0 <= year && year < 17) {
                builder.append("20");
            } else {
                builder.append("19");
            }
        }

        builder.append(number);

        return builder.toString();
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

            if (cell != null && detectPersonalNumber(cell.toString())) {
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

    private boolean detectPersonalNumber(String number) {
        int length = number.length();
        if (number.contains("-")) {
            length = length - 1;
        }

        if (length == 10 || length == 12) {
            String reversedNumber =
                    new StringBuilder(number).reverse().toString();

            return reversedNumber.charAt(4) == '-' &&
                    reversedNumber.substring(0, 4)
                            .concat(reversedNumber.substring(5))
                            .matches("[0-9]+");
        }

        return false;
    }

    private boolean validatePersonalNumber(String number) {
        String tenCharNumber = number;
        if (number.contains("-")) {
            int hyphenIndex = number.indexOf('-');
            tenCharNumber = number.substring(0, hyphenIndex)
                .concat(number.substring(hyphenIndex+1));
        }

        int length = tenCharNumber.length();
        if (length == 12) {
            tenCharNumber = tenCharNumber.substring(2);
        }


        int[] numberArray = stringToNumberArray(tenCharNumber);
        int[] validationPattern = new int[] {2,1,2,1,2,1,2,1,2,1};

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            int product = numberArray[i] * validationPattern[i];
            sum = sum + (int)Math.floor(product/10.0) + (product % 10);
        }

        return sum % 10 == 0;
    }

    private int[] stringToNumberArray(String number) {
        int[] numberArray = new int[10];

        char[] numbers = number.toCharArray();
        for (int i = 0; i < 10; i++) {
            numberArray[i] = Integer.parseInt(Character.toString(numbers[i]));
        }

        return numberArray;
    }

    private boolean detectReservedNumber(String number) {
        int length = number.length() - 1;
        if (length == 10 || length == 12) {
            String[] split = number.split("-", 2);

            return split.length == 2 &&
                    !split[0].isEmpty() && !split[1].isEmpty() &&
                    split[0].matches("[0-9]+") &&
                    split[0].length() == 6 && split[1].length() == 4;
        }

        return false;
    }
}