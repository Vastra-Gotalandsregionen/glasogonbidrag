package se.vgregion.service.glasogonbidrag.domain.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;

import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface GrantRepository {
    Grant find(Long id);

    Grant findWithParts(long id);

    // TODO: Should be renamed to findAllByUser
    List<Grant> findByUser(long userId);

    // TODO: Should be renamed to findAllByDate
    List<Grant> findByDate(Date date);

    long currentProgressByDate(Date date);

    long currentProgressByUserAndDate(long userId, Date date);
}
