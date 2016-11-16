package se.vgregion.glasogonbidrag.viewobject;

import java.util.Date;
import java.util.List;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
public class ExportVO {

    // Attributes

    private Date endDate;
    private Date startDate;

    boolean includeAllFields;

    private String[] includedFields;

    // Constructors

    public ExportVO() {
    }

    // Getters and Setters


    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isIncludeAllFields() {
        return includeAllFields;
    }

    public void setIncludeAllFields(boolean includeAllFields) {
        this.includeAllFields = includeAllFields;
    }

    public String[] getIncludedFields() {
        return includedFields;
    }

    public void setIncludedFields(String[] includedFields) {
        this.includedFields = includedFields;
    }
}
