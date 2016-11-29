package se.vgregion.portal.glasogonbidrag.domain.jpa;

import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Table(name = "grant_")
@NamedQueries({
        @NamedQuery(
                name = "glasogonbidrag.grant.findWithParts",
                query = "SELECT g FROM Grant g " +
                        "LEFT JOIN FETCH g.beneficiary b " +
                        "LEFT JOIN FETCH b.grants " +
                        "LEFT JOIN FETCH b.prescriptionHistory " +
                        "LEFT JOIN FETCH g.prescription " +
                        "WHERE g.id = :id"),

        @NamedQuery(
                name = "glasogonbidrag.grant.findAllByUser",
                query = "SELECT g " +
                        "FROM Grant g " +
                        "WHERE g.userId = :user " +
                        "ORDER BY g.createDate"),
        @NamedQuery(
                name = "glasogonbidrag.grant.findAllByCaseWorker",
                query = "SELECT g " +
                        "FROM Grant g " +
                        "WHERE g.caseWorker = :caseWorker " +
                        "ORDER BY g.createDate"),

        @NamedQuery(
                name = "glasogonbidrag.grant.findAllByDate",
                query = "SELECT g " +
                        "FROM Grant g " +
                        "WHERE DATE_TRUNC('day', g.createDate) = :date"),

        @NamedQuery(
                name = "glasogonbidrag.grant.currentProgressByDate",
                query = "SELECT SUM(g.amount) " +
                        "FROM Grant g " +
                        "WHERE DATE_TRUNC('day', g.createDate) = :date"),

        @NamedQuery(
                name = "glasogonbidrag.grant.currentProgressByUserAndDate",
                query = "SELECT SUM(g.amount) " +
                        "FROM Grant g " +
                        "WHERE g.userId = :user " +
                        "AND DATE_TRUNC('day', g.createDate) = :date")
})
public class Grant {

    private static SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd");

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

    private long amount;

    private String county;
    private String municipality;

    @Column(name = "case_worker")
    private String caseWorker;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Transient
    private final KronaCalculationUtil currency =
            new KronaCalculationUtil();

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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String responsibilityRegion) {
        this.municipality = responsibilityRegion;
    }

    public String getCaseWorker() {
        return caseWorker;
    }

    public void setCaseWorker(String caseWorker) {
        this.caseWorker = caseWorker;
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

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }


    // Public helper methods.

    public BigDecimal getAmountAsKrona() {
        return currency.calculatePartsAsKrona(amount);
    }

    public void setAmountAsKrona(BigDecimal valueAsKrona) {
        this.amount = currency.calculateKronaAsParts(valueAsKrona);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        // TODO: is this check alright? We should add message when user tries to add an "identical" grant.

        Grant grant = (Grant) o;

        if (amount != grant.amount) return false;
        if (deliveryDate != null ? !deliveryDate.equals(grant.deliveryDate) : grant.deliveryDate != null)
            return false;
        if (beneficiary != null ? !beneficiary.equals(grant.beneficiary) : grant.beneficiary != null)
            return false;
        return invoice != null ? invoice.equals(grant.invoice) : grant.invoice == null;

    }

    @Override
    public int hashCode() {
        int result = deliveryDate != null ? deliveryDate.hashCode() : 0;
        result = 31 * result + (int) (amount ^ (amount >>> 32));
        result = 31 * result + (beneficiary != null ? beneficiary.hashCode() : 0);
        result = 31 * result + (invoice != null ? invoice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String deliveryDateString =
                deliveryDate == null ? "" : DATE_FORMAT.format(deliveryDate);

        return "Grant{" +
                "id=" + id +
                ", deliveryDate=" + deliveryDateString +
                ", amount=" + currency.format(amount) +
                '}';
    }
}
