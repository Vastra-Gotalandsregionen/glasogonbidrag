package se.vgregion.portal.glasogonbidrag.domain.jpa;

import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Table(name = "vgr_glasogonbidrag_prescription")
@NamedQueries({
        @NamedQuery(
                name = "glasogonbidrag.prescription.findLatest",
                query = "SELECT p " +
                        "FROM Prescription p " +
                        "WHERE p.beneficiary = :beneficiary " +
                        "ORDER BY p.date DESC"),
})
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Liferay specific

    @Column(name = "user_id")
    private long userId;

    @Column(name = "group_id")
    private long groupId;

    @Column(name = "company_id")
    private long companyId;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @OneToOne
    @JoinColumn(name = "diagnose_id")
    private Diagnose diagnose;

    @Column(name = "prescription_date")
    @Temporal(TemporalType.DATE)
    private Date date;
    private String prescriber;

    @Lob
    @Basic(fetch=FetchType.EAGER, optional=true)
    @Column(name = "prescriber_comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;

    @OneToMany(mappedBy = "prescription")
    private Set<Grant> grants;

    public Prescription() {
        grants = new HashSet<>();
        diagnose = new None();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Diagnose getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(Diagnose diagnose) {
        this.diagnose = diagnose;
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

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Set<Grant> getGrants() {
        return grants;
    }

    public void setGrants(Set<Grant> grants) {
        this.grants = grants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prescription that = (Prescription) o;

        if (beneficiary != null ? !beneficiary.equals(that.beneficiary) : that.beneficiary != null)
            return false;
        if (date != null ? !date.equals(that.date) : that.date != null)
            return false;
        if (diagnose != null ? !diagnose.equals(that.diagnose) : that.diagnose != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = diagnose != null ? diagnose.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (beneficiary != null ? beneficiary.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", diagnose=" + diagnose +
                ", date=" + date +
                ", prescriber='" + prescriber + '\'' +
                ", comment='" + comment + '\'' +
                ", beneficiary=" + beneficiary +
                '}';
    }
}
