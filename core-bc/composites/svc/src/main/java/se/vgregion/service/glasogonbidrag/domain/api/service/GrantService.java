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

    // TODO: Should be renamed to findAllByUser
    List<Grant> findByUser(long userId);

    // TODO: Should be renamed to findAllByDate
    List<Grant> findByDate(Date date);

    List<Grant> findAllByCaseWorker(String caseWorker);

    long currentProgressByDate(Date date);

    long currentProgressByUserAndDate(long userId, Date date);
}
