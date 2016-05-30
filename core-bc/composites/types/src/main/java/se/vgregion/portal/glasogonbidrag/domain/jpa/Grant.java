package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Table(name = "vgr_glasogonbidrag_grant")
@NamedQueries({
        @NamedQuery(
                name = "glasogonbidrag.grant.findByUser",
                query = "SELECT g " +
                        "FROM Grant g " +
                        "WHERE g.userId = :user " +
                        "ORDER BY g.createDate"),

        @NamedQuery(
                name = "glasogonbidrag.grant.currentProgressByUserAndDate",
                query = "SELECT SUM(g.amount + g.vat) " +
                        "FROM Grant g " +
                        "WHERE g.userId = :user " +
                        "AND DATE_TRUNC('day', g.createDate) = :date")
})
public class Grant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liferay Related

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

    // Grant Specific

    @Column(name = "delivery_date")
    @Temporal(TemporalType.DATE)
    private Date deliveryDate;

    @Column(name = "prescription_date")
    @Temporal(TemporalType.DATE)
    private Date prescriptionDate;

    private int vat;

    private int amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver")
    private Beneficiary beneficiary;

    @ManyToOne(fetch = FetchType.EAGER)
    private Invoice invoice;

    public Grant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grant grant = (Grant) o;

        if (vat != grant.vat) return false;
        if (amount != grant.amount) return false;
        if (!id.equals(grant.id)) return false;
        if (deliveryDate != null ? !deliveryDate.equals(grant.deliveryDate) : grant.deliveryDate != null)
            return false;
        if (prescriptionDate != null ? !prescriptionDate.equals(grant.prescriptionDate) : grant.prescriptionDate != null)
            return false;
        return beneficiary != null ? beneficiary.equals(grant.beneficiary) : grant.beneficiary == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (deliveryDate != null ? deliveryDate.hashCode() : 0);
        result = 31 * result + (prescriptionDate != null ? prescriptionDate.hashCode() : 0);
        result = 31 * result + vat;
        result = 31 * result + amount;
        result = 31 * result + (beneficiary != null ? beneficiary.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Grant{" +
                "id=" + id +
                ", deliveryDate=" + deliveryDate +
                ", prescriptionDate=" + prescriptionDate +
                ", vat=" + vat +
                ", amount=" + amount +
                '}';
    }
}
