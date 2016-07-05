package se.vgregion.glasogonbidrag.viewobject;

import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;

import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class PrescriptionVO {
    private DiagnoseType type;
    private VisualLaterality laterality;
    private String comment;
    private String prescriber;
    private Date date;

    public DiagnoseType getType() {
        return type;
    }

    public void setType(DiagnoseType type) {
        this.type = type;
    }

    public VisualLaterality getLaterality() {
        return laterality;
    }

    public void setLaterality(VisualLaterality laterality) {
        this.laterality = laterality;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(String prescriber) {
        this.prescriber = prescriber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
