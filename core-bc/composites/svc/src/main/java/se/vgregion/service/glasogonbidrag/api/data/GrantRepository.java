package se.vgregion.service.glasogonbidrag.api.data;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;

import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface GrantRepository {
    List<Grant> findByUser(long userId);

    long currentProgressByUserAndDate(long userId, Date date);
}
