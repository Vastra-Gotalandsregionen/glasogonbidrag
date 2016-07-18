package se.vgregion.service.glasogonbidrag.local.api;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public interface AreaCodeLookupService {
    public String lookupCountyFromCode(String code);
    public String lookupCodeForCounty(String name);

    public String lookupMunicipalityFromCode(String code);
    public String lookupCodeForMunicipality(String name);

    public String lookupCountyBelonging(String municipalityCode);

    public boolean isMunicipalityPartOfCounty(String countyCode,
                                              String municipalityCode);

}
