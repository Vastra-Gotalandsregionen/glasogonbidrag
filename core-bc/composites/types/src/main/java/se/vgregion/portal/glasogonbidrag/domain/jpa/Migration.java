package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Table(name = "migration")
@NamedQueries(value = {
        @NamedQuery(
                name = "glasogonbidrag.migration.exists",
                query = "SELECT CASE " +
                        "   WHEN (COUNT(m) > 0) THEN TRUE " +
                        "   ELSE FALSE " +
                        "END " +
                        "FROM Migration m"),
        @NamedQuery(
                name = "glasogonbidrag.migration.findAll",
                query = "SELECT m FROM Migration m ORDER BY m.date")
})
public class Migration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "migration_date")
    private Date date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name="beneficiary_id",
            unique=true,
            nullable=true)
    private Beneficiary beneficiary;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "prescription_id",
            unique=true,
            nullable=true)
    private Prescription prescription;

    public Migration() {
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

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }
}
