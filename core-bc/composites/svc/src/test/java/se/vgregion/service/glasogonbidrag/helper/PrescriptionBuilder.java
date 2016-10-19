package se.vgregion.service.glasogonbidrag.helper;

import se.vgregion.portal.glasogonbidrag.domain.jpa.Diagnose;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Prescription;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class PrescriptionBuilder {
    private boolean hasParent;
    private GrantBuilder parent;

    private Diagnose diagnose;
    private Date date;
    private String prescriber;
    private String comment;

    public PrescriptionBuilder() {
        hasParent = false;
    }

    public PrescriptionBuilder(GrantBuilder parent) {
        hasParent = true;
        this.parent = parent;
    }

    public PrescriptionBuilder diagnose(Diagnose diagnose) {
        this.diagnose = diagnose;
        this.diagnose.setId(0L);
        return this;
    }

    public PrescriptionBuilder date(Date date) {
        this.date = date;
        return this;
    }

    public PrescriptionBuilder date(int year, int month, int dayOfMonth) {
        this.date =
                new GregorianCalendar(year, month, dayOfMonth).getTime();
        return this;
    }

    public PrescriptionBuilder prescriber(String prescriber) {
        this.prescriber = prescriber;
        return this;
    }

    public PrescriptionBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public Prescription build() {
        if (hasParent) {
            throw new IllegalStateException();
        }

        return _build();
    }

    public GrantBuilder end() {
        if (!hasParent) {
            throw new IllegalStateException();
        }

        parent.prescription(_build());

        return parent;
    }

    private Prescription _build() {
        Prescription prescription = new Prescription();

        Calendar cal = new GregorianCalendar();
        Date cur = cal.getTime();

        prescription.setId(0L);
        prescription.setGroupId(0L);
        prescription.setCompanyId(0L);
        prescription.setCreateDate(cur);
        prescription.setModifiedDate(cur);

        prescription.setDiagnose(diagnose);
        prescription.setDate(date);
        prescription.setPrescriber(prescriber);
        prescription.setComment(comment);

        return prescription;
    }
}
