package se.vgregion.service.glasogonbidrag.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;

import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface GrantRepository {
    Grant find(Long id);

    List<Grant> findByUser(long userId);

    List<Grant> findByDate(Date date);

    long currentProgressByDate(Date date);

    long currentProgressByUserAndDate(long userId, Date date);
}
