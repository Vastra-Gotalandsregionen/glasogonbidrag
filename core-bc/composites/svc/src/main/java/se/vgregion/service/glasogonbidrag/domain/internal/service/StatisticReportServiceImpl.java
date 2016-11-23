package se.vgregion.service.glasogonbidrag.domain.internal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.dto.StatisticReportDTO;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;
import se.vgregion.service.glasogonbidrag.domain.api.service.StatisticReportService;
import se.vgregion.service.glasogonbidrag.types.query.AggregationQuery;
import se.vgregion.service.glasogonbidrag.types.query.From;
import se.vgregion.service.glasogonbidrag.types.query.conditions.BetweenWhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.conditions.EqualsWhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.conditions.InstanceOfWhereCondition;
import se.vgregion.service.glasogonbidrag.types.query.join.LeftJoin;
import se.vgregion.service.glasogonbidrag.types.query.join.LeftSqlJoin;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static se.vgregion.service.glasogonbidrag.types.query.QueryBuilderFactory.simpleQuery;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class StatisticReportServiceImpl implements StatisticReportService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(LowLevelDatabaseQueryServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    public List<StatisticReportDTO> search() {
        AggregationQuery query = simpleQuery()
                .type(StatisticReportDTO.class)
                .select()
                .agg("COUNT(*)", "SUM(g.amount)")
                .from(new From("Grant", "g"))
                .join(new LeftJoin("g.beneficiary", "b"),
                        new LeftJoin("b.identification", "i"),
                        new LeftJoin("g.prescription", "p"),
                        new LeftJoin("p.diagnose", "d"))
                .where(new BetweenWhereCondition(
                                "g.createDate", "'2016-03-03'", "'2016-06-06'"),
                        new EqualsWhereCondition("b.sex",
                                "se.vgregion.portal.glasogonbidrag.domain.SexType.MALE"),
                        new InstanceOfWhereCondition("i", Personal.class),
                        new InstanceOfWhereCondition("d", None.class))
                .groupBy("g.county", "g.municipality")
                .build();

        TypedQuery<StatisticReportDTO> q =
                em.createQuery(query.toJpqlString(), StatisticReportDTO.class);

        List<StatisticReportDTO> result = q.getResultList();

        LOGGER.debug("search() - The query {} found {} results",
                query, result.size());

        return result;
    }
}
