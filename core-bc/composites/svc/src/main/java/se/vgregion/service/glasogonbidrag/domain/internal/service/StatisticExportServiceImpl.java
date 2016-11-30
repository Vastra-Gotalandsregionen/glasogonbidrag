package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    @PersistenceContext
    private EntityManager em;

    @Override
    public ByteArrayOutputStream export(Date start, Date end)
            throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Export");

        int rowIndex = 0;

        List<StatisticExportDTO> exportData = getData(start,end);
        for (StatisticExportDTO exportRow : exportData) {
            rowIndex = rowIndex + 1;
            Row row = sheet.createRow(rowIndex);


            int cellIndex = 0;

            Cell cell;

            cellIndex = cellIndex + 1;
            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getAmount());

            cellIndex = cellIndex + 1;
            cell = row.createCell(cellIndex);
            cell.setCellValue(exportRow.getSex().toString());

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

        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);

        return byteArrayOutputStream;
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
