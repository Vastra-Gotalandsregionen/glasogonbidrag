package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.dto.StatisticExportDTO;
import se.vgregion.service.glasogonbidrag.domain.api.service.StatisticExportService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class StatisticExportServiceImpl implements StatisticExportService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticExportServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public byte[] export(Date start, Date end)
            throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Export");

        List<StatisticExportDTO> exportData = getData(start, end);

        int rowIndex = 0;

        // TODO: Fix language keys.
        // Headers
        {
            Row row = sheet.createRow(rowIndex);
            int cellIndex = 0;

            Cell cell = row.createCell(cellIndex);
            cell.setCellValue("belopp");
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue("kön");
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue("diagnose type");
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue("födelse datum");
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue("kvitterings datum");
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue("skapades");
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue("län");
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue("kommun");
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue("ansvars kod");
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue("fri kod");
        }

        for (StatisticExportDTO exportRow : exportData) {
            rowIndex = rowIndex + 1;
            Row row = sheet.createRow(rowIndex);

            int cellIndex = 0;

            Cell cell;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getAmount());
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            if (exportRow.getSex() != null) {
                cell.setCellValue(exportRow.getSex().toString());
            } else {
                cell.setCellValue("okänt");
            }
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getDiagnoseType().toString());
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getBirthDate());
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getDeliveryDate());
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getCreateDate());
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getCounty());
            cellIndex = cellIndex + 1;

            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getMunicipality());
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
