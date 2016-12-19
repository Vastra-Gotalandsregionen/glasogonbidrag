package se.vgregion.service.glasogonbidrag.local.internal;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.SexType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.dto.ImportDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Keratoconus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Special;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Lma;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Other;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Protected;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Reserve;
import se.vgregion.service.glasogonbidrag.local.api.GrantImportService;
import se.vgregion.service.glasogonbidrag.local.api.IdentificationTypeService;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberFormatService;
import se.vgregion.service.glasogonbidrag.local.api.PersonalNumberService;
import se.vgregion.service.glasogonbidrag.types.migration.IdentificationStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
@SuppressWarnings("unused")
public class GrantImportServiceImpl implements GrantImportService {
    private static final String DATA_SHEET_NAME = "Data";

    private static final int DATA_ROW_START = 2;

    private static final int IDENTIFICATION_NUMBER_CELL_POSITION = 0;
    private static final int FULL_NAME_CELL_POSITION = 1;
    private static final int RECIPE_DATE_CELL_POSITION = 2;
    private static final int DIAGNOSE_TYPE_CELL_POSITION = 3;
    private static final int VISUAL_LATERALITY_CELL_POSITION = 4;
    private static final int ACUITY_RIGHT_CELL_POSITION = 5;
    private static final int ACUITY_LEFT_CELL_POSITION = 6;
    private static final int NO_GLASSES_CELL_POSITION = 7;
    private static final int WEAK_SIGHT_CELL_POSITION = 8;
    private static final int COMMENT_CELL_POSITION = 9;
    private static final int DELIVERY_DATE_CELL_POSITION = 10;
    private static final int AMOUNT_CELL_POSITION = 11;
    private static final int PRESCRIBER_CELL_POSITION = 12;
    private static final int INVOICE_NUMBER_CELL_POSITION = 13;
    private static final int VERIFICATION_NUMBER_CELL_POSITION = 14;

    @Autowired
    private IdentificationTypeService identificationTypeService;

    @Autowired
    private PersonalNumberService personalNumberService;

    @Autowired
    private PersonalNumberFormatService personalNumberFormatService;

    @Autowired
    private TranslateService translateService;

    public ImportDTO importData(byte[] data, String currentYear, Locale locale) {
        ByteArrayInputStream input = new ByteArrayInputStream(data);

        Workbook wb;
        try {
            wb = WorkbookFactory.create(input);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }

        int sheetCount = wb.getNumberOfSheets();
        if (sheetCount < 1) {
            return new ImportDTO();
        }

        IdentificationStore store = new IdentificationStore();

        Sheet sheet = wb.getSheet(DATA_SHEET_NAME);
        int maxRow = sheet.getPhysicalNumberOfRows();

        // Extract all grants ordered by beneficiary/identification number
        for (int nRow = DATA_ROW_START; nRow < maxRow; nRow++) {
            Cell identCell = null;
            Cell nameCell = null;

            Row row = sheet.getRow(nRow);
            if (row != null) {
                identCell = row.getCell(IDENTIFICATION_NUMBER_CELL_POSITION);
                nameCell = row.getCell(FULL_NAME_CELL_POSITION);
            }

            String identificationNumber = "";
            if (identCell != null) {
                identificationNumber = identCell.getStringCellValue();
                store.addRow(identificationNumber, nRow);
            }

            if (!identificationNumber.isEmpty() && nameCell != null) {
                String name = nameCell.getStringCellValue();
                store.addName(identificationNumber, name);
            }
        }

        List<Prescription> prescriptions = new ArrayList<>();
        List<Beneficiary> beneficiaries = new ArrayList<>();

        // Create grants for all
        List<String> identifications = store.getIdentifications();
        for (String number : identifications) {
            String fullName = store.getName(number);

            Identification identification =
                    buildIdentification(number, currentYear);
            Beneficiary beneficiary = buildBeneficiary(
                    fullName, identification);

            List<Integer> rows = store.getRows(number);
            for (Integer nRow : rows) {
                Row row = sheet.getRow(nRow);
                Diagnose diagnose = buildDiagnoseFromRow(row, locale);
                Prescription prescription = buildPrescriptionFromRow(row);

                // Set diagnose to prescription
                prescription.setDiagnose(diagnose);

                // Set mapping between beneficiary and prescription
                prescription.setBeneficiary(beneficiary);
                beneficiary.getPrescriptionHistory().add(prescription);

                prescriptions.add(prescription);
            }

            beneficiaries.add(beneficiary);
        }

        return new ImportDTO(beneficiaries, prescriptions);
    }

    private Identification buildIdentification(String number,
                                               String currentYear) {
        Identification identification;

        // We don't know the birth date of a persons with LMA or Other
        // identification, we set this to the first of january of
        // "current year".
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, Integer.parseInt(currentYear));
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        switch (identificationTypeService.detect(number, currentYear)) {
            case PERSONAL: {
                String format = personalNumberFormatService.to(
                        number, currentYear);
                identification = new Personal(format);
                break;
            }
            case RESERVE: {
                String format = personalNumberFormatService.to(
                        number, currentYear);
                identification = new Reserve(
                        format,
                        personalNumberService.parseBirthYear(format));
                break;
            }
            case LMA: {
                identification = new Lma(number, cal.getTime());
                break;
            }
            case OTHER: {
                identification = new Other(number, cal.getTime());
                break;
            }
            case PROTECTED: {
                String format = personalNumberFormatService.to(
                        number, currentYear);
                identification = new Protected(format);
                break;
            }
            case NONE:
            default:
                identification = null;
                break;
        }

        return identification;
    }

    private Beneficiary buildBeneficiary(String fullName,
                                         Identification identification) {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setFullName(fullName);
        beneficiary.setIdentification(identification);
        beneficiary.setSex(SexType.UNKNOWN);

        return beneficiary;
    }

    private Diagnose buildDiagnoseFromRow(Row row, Locale locale) {
        Cell cell;

        DiagnoseType type = null;
        cell = row.getCell(DIAGNOSE_TYPE_CELL_POSITION);
        if (cell != null) {
            String value = cell.getStringCellValue();
            String key = value == null || value.isEmpty()? "barn" : value;

            String translated = translateService.translate(locale, key);
            type = DiagnoseType.parse(translated);
        }

        VisualLaterality laterality = null;
        cell = row.getCell(VISUAL_LATERALITY_CELL_POSITION);
        if (cell != null) {
            String value = cell.getStringCellValue();
            String key = value == null || value.isEmpty()? "ingen" : value;

            String translated = translateService.translate(locale, key);
            laterality = VisualLaterality.parse(translated);
        }

        float acuityRight = 0.0f;
        cell = row.getCell(ACUITY_RIGHT_CELL_POSITION);
        if (cell != null) {
            acuityRight = new Double(cell.getNumericCellValue()).floatValue();
        }

        float acuityLeft = 0.0f;
        cell = row.getCell(ACUITY_LEFT_CELL_POSITION);
        if (cell != null) {
            acuityLeft = new Double(cell.getNumericCellValue()).floatValue();
        }

        boolean noGlasses = false;
        cell = row.getCell(NO_GLASSES_CELL_POSITION);
        if (cell != null) {
            String value = cell.getStringCellValue();
            String key = value == null || value.isEmpty()? "nej" : value;
            String translated = translateService.translate(locale, key);
            noGlasses = "yes".equals(translated);
        }

        boolean weakSight = false;
        cell = row.getCell(WEAK_SIGHT_CELL_POSITION);
        if (cell != null) {
            String value = cell.getStringCellValue();
            String key = value == null || value.isEmpty()? "nej" : value;
            String translated = translateService.translate(locale, key);
            weakSight = "yes".equals(translated);
        }

        if (type == null) {
            throw new IllegalStateException();
        }

        Diagnose diagnose;
        switch (type) {
            case APHAKIA:
                diagnose = new Aphakia(laterality);
                break;

            case KERATOCONUS:
                diagnose = new Keratoconus(
                        laterality, acuityRight, acuityLeft, noGlasses);
                break;

            case SPECIAL:
                diagnose = new Special(laterality, weakSight);
                break;

            case NONE:
                diagnose = new None();
                break;

            default:
                diagnose = null;
                break;
        }

        return diagnose;
    }

    private Prescription buildPrescriptionFromRow(Row row) {
        Cell cell;

        String comment = null;
        cell = row.getCell(COMMENT_CELL_POSITION);
        if (cell != null) {
            comment = cell.getStringCellValue();
        }

        Date recipeDate = null;
        cell = row.getCell(RECIPE_DATE_CELL_POSITION);
        if (cell != null) {
            recipeDate = cell.getDateCellValue();
        }

        String prescriber = null;
        cell = row.getCell(PRESCRIBER_CELL_POSITION);
        if (cell != null) {
            prescriber = cell.getStringCellValue();
        }

        Prescription prescription = new Prescription();
        prescription.setComment(comment);
        prescription.setDate(recipeDate);
        prescription.setPrescriber(prescriber);

        return prescription;
    }
}
