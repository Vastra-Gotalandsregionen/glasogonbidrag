package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.dto.StatisticExportDTO;
import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;
import se.vgregion.service.glasogonbidrag.domain.api.service.StatisticExportService;
import se.vgregion.service.glasogonbidrag.local.api.AreaCodeLookupService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class StatisticExportServiceImpl implements StatisticExportService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticExportServiceImpl.class);

    private static final SimpleDateFormat FORMAT_DATE =
            new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private AreaCodeLookupService areaCodeLookupService;

    private final KronaCalculationUtil kronaCalculationUtil =
            new KronaCalculationUtil();


    @PersistenceContext
    private EntityManager em;

    public byte[] export(Date start, Date end,
                         Map<String, String> localization)
            throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        String sheetName =
                FORMAT_DATE.format(start) + "-" + FORMAT_DATE.format(end);
        XSSFSheet sheet = workbook.createSheet(sheetName);

        List<StatisticExportDTO> exportData = getData(start, end);

        int rowIndex = 0;

        // TODO: Fix language keys.
        // Headers
        {
            Row row = sheet.createRow(rowIndex);
            int cellIndex = 0;

            Cell cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-amount-header"));
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-sex-header"));
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-diagnose-type-header"));
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-birth-date-header"));
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-delivery-date-header"));
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-create-date-header"));
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-county-header"));
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-municipality-header"));
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-responsibility-code-header"));
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(localization.get("excel-file-free-code-header"));
        }

        CreationHelper creationHelper = workbook.getCreationHelper();

        CellStyle dateCellStyle = workbook.createCellStyle();
        short dateDataFormat = creationHelper.createDataFormat().getFormat("yyyy-MM-dd");
        dateCellStyle.setDataFormat(dateDataFormat);

        CellStyle numberCellStyle = workbook.createCellStyle();
        short numberDataFormat = creationHelper.createDataFormat().getFormat("0");
        numberCellStyle.setDataFormat(numberDataFormat);

        for (StatisticExportDTO exportRow : exportData) {
            rowIndex = rowIndex + 1;
            Row row = sheet.createRow(rowIndex);

            int cellIndex = 0;

            Cell cell;

            long amount = exportRow.getAmount();
            BigDecimal amountAsKrona = kronaCalculationUtil.calculatePartsAsKrona(amount);
            long amountRounded = amountAsKrona.setScale(0, RoundingMode.HALF_UP).longValue();

            cell = row.createCell(cellIndex);
            cell.setCellValue(amountRounded);
            cell.setCellStyle(numberCellStyle);
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            if (exportRow.getSex() != null) {
                String sexLanguageKey = exportRow.getSex().getKey();
                String sexString = localization.get(sexLanguageKey);
                cell.setCellValue(sexString);
            } else {
                cell.setCellValue("n/a");
            }
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            String languageKey = exportRow.getDiagnoseType().getLanguageKey();
            String label = localization.get(languageKey);
            cell.setCellValue(label);
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getBirthDate());
            cell.setCellStyle(dateCellStyle);
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getDeliveryDate());
            cell.setCellStyle(dateCellStyle);
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getCreateDate());
            cell.setCellStyle(dateCellStyle);
            cellIndex = cellIndex + 1;


            String countyCode = exportRow.getCounty();
            String countyFriendlyName = areaCodeLookupService.lookupCountyFromCode(countyCode);
            if(countyFriendlyName == null || countyFriendlyName.isEmpty()) {
                countyFriendlyName = "n/a";
            }
            //String countyFriendlyName = countyCode;

            cell = row.createCell(cellIndex);
            cell.setCellValue(countyFriendlyName);
            cellIndex = cellIndex + 1;

            String municipalityCode = exportRow.getCounty() + exportRow.getMunicipality();
            String municipalityFriendlyName = areaCodeLookupService.lookupMunicipalityFromCode(municipalityCode);
            if(municipalityFriendlyName == null || municipalityFriendlyName.isEmpty()) {
                municipalityFriendlyName = "n/a";
            }
            //String municipalityFriendlyName = municipalityCode;

            cell = row.createCell(cellIndex);
            cell.setCellValue(municipalityFriendlyName);
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getResponsibility());
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getFreeCode());
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        workbook.write(stream);

        try {
            return stream.toByteArray();
        } finally {
            stream.close();
        }
    }

    private List<StatisticExportDTO> getData(Date start, Date end) {
        TypedQuery<StatisticExportDTO> q = em.createNamedQuery(
                "glasogonbidrag.grant.findAllGrantForExport",
                StatisticExportDTO.class);

        q.setParameter("startDate", start);
        q.setParameter("endDate", end);

        return q.getResultList();
    }
}
