package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.dto.StatisticReportDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.service.glasogonbidrag.domain.api.service.StatisticReportService;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchDateInterval;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchIntegerInterval;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchRequest;
import se.vgregion.service.glasogonbidrag.types.StatisticSearchResponse;
import se.vgregion.service.glasogonbidrag.types.query.AggregationSqlQuery;
import se.vgregion.service.glasogonbidrag.types.query.AggregationSqlQueryBuilder;
import se.vgregion.service.glasogonbidrag.types.query.From;
import se.vgregion.service.glasogonbidrag.types.query.conditions.BetweenWhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.conditions.EqualsWhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.join.LeftJoin;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static se.vgregion.service.glasogonbidrag.types.query.QueryBuilderFactory.simpleQuery;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class StatisticReportServiceImpl implements StatisticReportService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticReportServiceImpl.class);

    @Autowired
    private DataSource ds;

//    // If the above is fine, remove this.
//    @PersistenceContext
//    private EntityManager em;

    public StatisticSearchResponse search(StatisticSearchRequest request) {
        AggregationSqlQuery query = buildQuery(request);

        LOGGER.debug("search() - prepared the query {}.", query.toSqlString());

        List<StatisticReportDTO> result = new ArrayList<>();

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = ds.getConnection();
            statement = conn.prepareCall(query.toSqlString());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                long count = rs.getLong("count");
                long amount = rs.getLong("amount");
                String data = rs.getString("group_data");

                result.add(new StatisticReportDTO(count, amount, data));
            }
        } catch (SQLException e) {
            LOGGER.warn("Problem executing SQL statement," +
                    "Got exception {}", e.getMessage());
        }finally{
            try {
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                LOGGER.warn("Problem closing statement or connection, " +
                        "Got exception {}", e.getMessage());
            }
        }

        LOGGER.debug("search() - the query found {} results.", result.size());

        return new StatisticSearchResponse(request.getType(), result);
    }

    private AggregationSqlQuery buildQuery(StatisticSearchRequest request) {
        AggregationSqlQueryBuilder builder = simpleQuery()
                .select()
                .agg("COUNT(*) AS count", "SUM(g.amount) AS amount")
                .from(new From("grant_", "g"))
                .join(new LeftJoin("beneficiary", "b",
                                new EqualsWhereCondition(
                                        "g.beneficiary_id",
                                        "b.id")),
                        new LeftJoin("identification", "i",
                                new EqualsWhereCondition(
                                        "b.identification_id",
                                        "i.id")),
                        new LeftJoin("prescription", "p",
                                new EqualsWhereCondition(
                                        "g.prescription_id",
                                        "p.id")),
                        new LeftJoin("diagnose", "d",
                                new EqualsWhereCondition(
                                        "p.diagnose_id",
                                        "d.id")));

        // Add where for the interval
        if (request.getInterval() != null) {
            StatisticSearchDateInterval interval = request.getInterval();
            if (interval.isInterval()) {
                builder.where(new BetweenWhereCondition(
                        "g.create_date",
                        String.format("'%s'", interval.getStart()),
                        String.format("'%s'", interval.getEnd())));
            } else {
                builder.where(new EqualsWhereCondition(
                        "to_char(g.create_date, 'YYYY-MM-DD')",
                        String.format("'%s'", interval.getDate())));
            }
        }

        if (request.getDiagnoseType() != null) {
            builder.where(new EqualsWhereCondition("d.diagnose_type",
                    String.format("'%s'",
                            Diagnose.getDiscriminatorValueMap().get(
                                    request.getDiagnoseType()))));
        }

        if (request.getIdentificationType() != null) {
            builder.where(new EqualsWhereCondition("i.identity_type",
                    String.format("'%s'",
                            Identification.getDiscriminatorValueMap().get(
                                    request.getIdentificationType()))));
        }

        if (request.getSex() != null) {
            builder.where(new EqualsWhereCondition("b.sex",
                    String.format("'%s'", request.getSex())));
        }

        if (request.getBirthYearInterval() != null) {
            StatisticSearchIntegerInterval interval =
                    request.getBirthYearInterval();

            builder.where(new BetweenWhereCondition(
                    "b.birth_year",
                    String.format("%d", interval.getStart()),
                    String.format("%d", interval.getEnd())));
        }

        if (request.getType() == null) {
            throw new IllegalArgumentException(
                    "Must set a search type on the request.");
        }

        switch (request.getType()) {
            case MUNICIPALITY:
                builder.groupBy("g.county", "g.municipality");
                break;
            case BIRTH_YEAR:
                builder.groupBy("b.birth_year");
                break;
            case SEX:
                builder.groupBy("b.sex");
                break;
            case GRANT_TYPE:
                builder.groupBy("d.diagnose_type");
                break;
        }

        return builder.build();
    }
}
