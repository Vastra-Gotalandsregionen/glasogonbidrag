package se.vgregion.portal.glasogonbidrag.domain.dto;

import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.SexType;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;

import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class StatisticExportDTO {
    private final long amount;
    private final SexType sex;
    private final Diagnose diagnose;
    private final Identification identification;
    private final Date recipeDate;
    private final Date deliveryDate;
    private final Date createDate;
    private final String county;
    private final String municipality;
    private final int responsibility;
    private final int freeCode;

    public StatisticExportDTO(long amount,
                              SexType sex,
                              Diagnose diagnose,
                              Identification identification,
                              Date recipeDate,
                              Date deliveryDate,
                              Date createDate,
                              String county,
                              String municipality,
                              int responsibility,
                              int freeCode) {
        this.amount = amount;
        this.sex = sex;
        this.diagnose = diagnose;
        this.identification = identification;
        this.recipeDate = recipeDate;
        this.deliveryDate = deliveryDate;
        this.createDate = createDate;
        this.county = county;
        this.municipality = municipality;
        this.responsibility = responsibility;
        this.freeCode = freeCode;
    }

    public long getAmount() {
        return amount;
    }

    public SexType getSex() {
        return sex;
    }

    public DiagnoseType getDiagnoseType() {
        return diagnose.getType();
    }

    public Date getBirthDate() {
        return identification.getBirthDate();
    }

    public Date getRecipeDate() {
        return recipeDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCounty() {
        return county;
    }

    public String getMunicipality() {
        return municipality;
    }

    public int getResponsibility() {
        return responsibility;
    }

    public int getFreeCode() {
        return freeCode;
    }
}
