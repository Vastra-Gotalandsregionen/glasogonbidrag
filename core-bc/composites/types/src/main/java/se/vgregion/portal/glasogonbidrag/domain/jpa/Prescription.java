package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Table(name = "vgr_glasogonbidrag_prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "prescription_date")
    @Temporal(TemporalType.DATE)
    private Date date;
    private String prescriber;
    @Lob
    @Column(name = "prescriber_comment")
    private String comment;

    @Column(name = "aphakia")
    private boolean aphakiaDiagnosed = false;
    @Column(name = "keratoconus")
    private boolean keratoconusDiagnosed = false;
    @Column(name = "weak_eye_sight")
    private boolean weakEyeSightDiagnosed = false;

    private DiagnoseAphakia aphakia;
    private DiagnoseKeratoconus keratoconus;
    private DiagnoseWeakEyesight weakEyeSight;

    public Prescription() {
        aphakia = new DiagnoseAphakia();
        keratoconus = new DiagnoseKeratoconus();
        weakEyeSight = new DiagnoseWeakEyesight();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPrescriber() {
        return prescriber;
    }

    public void setPrescriber(String prescriber) {
        this.prescriber = prescriber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isAphakiaDiagnosed() {
        return aphakiaDiagnosed;
    }

    public void setAphakiaDiagnosed(boolean aphakiaDiagnosed) {
        this.aphakiaDiagnosed = aphakiaDiagnosed;
    }

    public boolean isKeratoconusDiagnosed() {
        return keratoconusDiagnosed;
    }

    public void setKeratoconusDiagnosed(boolean keratoconusDiagnosed) {
        this.keratoconusDiagnosed = keratoconusDiagnosed;
    }

    public boolean isWeakEyeSightDiagnosed() {
        return weakEyeSightDiagnosed;
    }

    public void setWeakEyeSightDiagnosed(boolean weakEyeSightDiagnosed) {
        this.weakEyeSightDiagnosed = weakEyeSightDiagnosed;
    }

    public DiagnoseAphakia getAphakia() {
        return aphakia;
    }

    public void setAphakia(DiagnoseAphakia aphakia) {
        this.aphakia = aphakia;
    }

    public DiagnoseKeratoconus getKeratoconus() {
        return keratoconus;
    }

    public void setKeratoconus(DiagnoseKeratoconus keratoconus) {
        this.keratoconus = keratoconus;
    }

    public DiagnoseWeakEyesight getWeakEyeSight() {
        return weakEyeSight;
    }

    public void setWeakEyeSight(DiagnoseWeakEyesight weakEyeSight) {
        this.weakEyeSight = weakEyeSight;
    }
}
