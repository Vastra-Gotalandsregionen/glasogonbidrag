package se.vgregion.service.glasogonbidrag.local.api;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface RegionResponsibilityLookupService {
    int lookupResponsibility(String municipality);
    String lookupRegion(String municipality);
}
