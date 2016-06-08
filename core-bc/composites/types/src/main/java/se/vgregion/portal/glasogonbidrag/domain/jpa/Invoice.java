package se.vgregion.portal.glasogonbidrag.domain.jpa;

import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Table(name = "vgr_glasogonbidrag_invoice")
@NamedQueries({
        @NamedQuery(
                name = "glasogonbidrag.invoice.findWithGrants",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "LEFT JOIN FETCH i.grants " +
                        "WHERE i.id = :id"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findWithParts",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "LEFT JOIN FETCH i.grants " +
                        "LEFT JOIN FETCH i.supplier " +
                        "LEFT JOIN FETCH i.adjustment " +
                        "WHERE i.id = :id"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findByVerificationNumber",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "LEFT JOIN FETCH i.grants " +
                        "LEFT JOIN FETCH i.supplier " +
                        "LEFT JOIN FETCH i.adjustment " +
                        "WHERE i.verificationNumber = :number"),

        @NamedQuery(
                name = "glasogonbidrag.invoice.findAll",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "ORDER BY i.id ASC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllOrderByModificationDate",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "ORDER BY i.modifiedDate ASC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice." +
                        "findAllByUserOrderByModificationDate",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "WHERE i.userId = :user " +
                        "ORDER BY i.modifiedDate ASC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllByInvoiceNumber",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "LEFT JOIN FETCH i.grants " +
                        "LEFT JOIN FETCH i.supplier " +
                        "LEFT JOIN FETCH i.adjustment " +
                        "WHERE i.invoiceNumber = :number " +
                        "ORDER BY i.id ASC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllBySupplier",
                query = "SELECT i FROM Invoice i " +
                        "WHERE i.supplier = :supplier " +
                        "ORDER BY i.id ASC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllByStatus",
                query = "SELECT i FROM Invoice i " +
                        "WHERE i.status = :status " +
                        "ORDER BY i.id ASC")
})
public class Invoice {

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

    // Invoice Specific

    @Column(name = "verification_number", nullable = false)
    private String verificationNumber;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    private long vat;

    private long amount;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "invoice")
    private List<Grant> grants;

    @ManyToOne
    @JoinColumn(name = "supplier_name")
    private Supplier supplier;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "adjustment")
    private GrantAdjustment adjustment;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @Transient
    private final KronaCalculationUtil currency =
            new KronaCalculationUtil();

    public Invoice() {
        grants = new ArrayList<>();
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

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public long getVat() {
        return vat;
    }

    public void setVat(long vat) {
        this.vat = vat;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public List<Grant> getGrants() {
        return grants;
    }

    public void setGrants(List<Grant> grants) {
        this.grants = grants;
    }

    public void addGrant(Grant grant) {
        grants.add(grant);
    }

    public void removeGrant(Grant grant) {
        grants.remove(grant);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public GrantAdjustment getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(GrantAdjustment adjustment) {
        this.adjustment = adjustment;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    // Public helper methods.

    public BigDecimal getAmountAsKrona() {
        return currency.calculatePartsAsKrona(amount);
    }

    public void setAmountAsKrona(BigDecimal valueAsKrona) {
        this.amount = currency.calculateKronaAsParts(valueAsKrona);
    }

    public BigDecimal getVatAsKrona() {
        return currency.calculatePartsAsKrona(vat);
    }

    public void setVatAsKrona(BigDecimal valueAsKrona) {
        this.vat = currency.calculateKronaAsParts(valueAsKrona);
    }

    public BigDecimal getAmountIncludingVatAsKrona() {
        return currency.calculatePartsAsKrona(amount + vat);
    }

    public void setAmountIncludingVatAsKrona(BigDecimal valueAsKrona) {
        KronaCalculationUtil.ValueAndVat result =
                currency.calculateValueAndVatAsParts(valueAsKrona);

        this.amount = result.getValue();
        this.vat = result.getVat();
    }

    public BigDecimal calculateGrantsAmountSumAsKrona() {
        if (grants == null) {
            return new BigDecimal(0);
        }

        long sum = sumGrantsAmount();

        return currency.calculatePartsAsKrona(sum);
    }

    public BigDecimal calculateGrantsVatSumAsKrona() {
        if (grants == null) {
            return new BigDecimal(0);
        }

        long sum = sumGrantsVat();

        return currency.calculatePartsAsKrona(sum);
    }

    public BigDecimal calculateGrantsAmountIncludingVatSumAsKrona() {
        if (grants == null) {
            return new BigDecimal(0);
        }

        long sum = sumGrantsAmountIncludingVat();

        return currency.calculatePartsAsKrona(sum);
    }

    public BigDecimal calculateDifferenceExcludingVatAsKrona() {
        long result = this.amount;

        result = result - sumGrantsAmount();

        return currency.calculatePartsAsKrona(result);
    }

    public BigDecimal calculateDifferenceIncludingVatAsKrona() {
        long result = this.amount + vat;

        result = result - sumGrantsAmountIncludingVat();

        return currency.calculatePartsAsKrona(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        return verificationNumber.equals(invoice.verificationNumber);

    }

    @Override
    public int hashCode() {
        return verificationNumber.hashCode();
    }

    @Override
    public String toString() {
        try {
            return "Invoice{" +
                    "id=" + id +
                    ", companyId=" + companyId +
                    ", groupId=" + groupId +
                    ", userId=" + userId +
                    ", verificationNumber='" + verificationNumber + '\'' +
                    ", invoiceNumber='" + invoiceNumber + '\'' +
                    ", vat=" + vat +
                    ", amount=" + amount +
                    ", grants=" + grants +
                    ", supplier=" + supplier +
                    ", adjustment=" + adjustment +
                    '}';
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // Helper methods

    private long sumGrantsAmount() {
        long sum = 0;

        for (Grant grant : grants) {
            sum = sum + grant.getAmount();
        }

        return sum;
    }

    private long sumGrantsVat() {
        long sum = 0;

        for (Grant grant : grants) {
            sum = sum + grant.getVat();
        }

        return sum;
    }

    private long sumGrantsAmountIncludingVat() {
        long sum = 0;

        for (Grant grant : grants) {
            sum = sum + grant.getAmount() + grant.getVat();
        }

        return sum;
    }
}
