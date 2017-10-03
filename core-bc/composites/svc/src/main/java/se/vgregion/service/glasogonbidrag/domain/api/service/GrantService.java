package se.vgregion.service.glasogonbidrag.domain.api.service;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantMissingAreaException;

import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface GrantService {
    void create(Grant grant) throws GrantMissingAreaException;

    Grant update(Grant grant) throws GrantMissingAreaException;

    void delete(long id);

    Grant find(long id);

    Grant findWithParts(long id);

    List<Grant> findAllByUser(long userId);

    List<Grant> findAllByDate(Date date);

    List<Grant> findAllByCaseWorker(String caseWorker);

    long countByDate(Date date);

    long currentProgressByDate(Date date);

    long currentProgressByUserAndDate(long userId, Date date);
}
