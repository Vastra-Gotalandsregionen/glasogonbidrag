package se.vgregion.service.glasogonbidrag.api.service;


import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface GrantService {
    void create(Grant grant);

    void update(Grant grant);

    void delete(Long id);


}
