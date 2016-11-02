package se.vgregion.glasogonbidrag.viewobject;

import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;

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
    private boolean noGlasses;
    private float visualAcuityLeft;
    private float visualAcuityRight;
    private boolean weakEyeSight;

    public PrescriptionVO() {
        type = DiagnoseType.NONE;
    }

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

    public boolean isNoGlasses() {
        return noGlasses;
    }

    public void setNoGlasses(boolean noGlasses) {
        this.noGlasses = noGlasses;
    }

    public float getVisualAcuityLeft() {
        return visualAcuityLeft;
    }

    public void setVisualAcuityLeft(float visualAcuityLeft) {
        this.visualAcuityLeft = visualAcuityLeft;
    }

    public float getVisualAcuityRight() {
        return visualAcuityRight;
    }

    public void setVisualAcuityRight(float visualAcuityRight) {
        this.visualAcuityRight = visualAcuityRight;
    }

    public boolean isWeakEyeSight() {
        return weakEyeSight;
    }

    public void setWeakEyeSight(boolean weakEyeSight) {
        this.weakEyeSight = weakEyeSight;
    }

    public boolean isDiagnoseAphakia() {
        return type == DiagnoseType.APHAKIA;
    }

    // TODO: Fix spelling, keratoconus with c instead of k.
    public boolean isDiagnoseKeratokonus() {
        return type == DiagnoseType.KERATOCONUS;
    }

    public boolean isDiagnoseNone() {
        return type == DiagnoseType.NONE;
    }

    public boolean isDiagnoseSpecial() {
        return type == DiagnoseType.SPECIAL;
    }
}
