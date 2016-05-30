package se.vgregion.portal.glasogonbidrag.domain.jpa;

import se.vgregion.portal.glasogonbidrag.domain.CurrencyConstants;

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
import java.math.BigDecimal;
import java.math.RoundingMode;
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
                        "ORDER BY i.invoiceDate ASC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllByInvoiceNumber",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "LEFT JOIN FETCH i.grants " +
                        "LEFT JOIN FETCH i.supplier " +
                        "LEFT JOIN FETCH i.adjustment " +
                        "WHERE i.invoiceNumber = :number " +
                        "ORDER BY i.invoiceDate ASC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllBySupplier",
                query = "SELECT i FROM Invoice i " +
                        "WHERE i.supplier = :supplier " +
                        "ORDER BY i.invoiceDate ASC")
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

    @Column(name = "invoice_date")
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;

    @Column(name = "verification_number", nullable = false)
    private String verificationNumber;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    private long vat;

    private long amount;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private List<Grant> grants;

    @ManyToOne
    private Supplier supplier;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private GrantAdjustment adjustment;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

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

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
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
        BigDecimal amountDecimal = new BigDecimal(amount);

        return amountDecimal.divide(
                CurrencyConstants.PARTS_PER_KRONA, 2, RoundingMode.HALF_EVEN);
    }

    public void setAmountAsKrona(BigDecimal valueAsKrona) {
        this.amount = valueAsKrona.multiply(
                CurrencyConstants.PARTS_PER_KRONA).longValue();
    }

    public BigDecimal getVatAsKrona() {
        BigDecimal vatDecimal = new BigDecimal(vat);

        return vatDecimal.divide(
                CurrencyConstants.PARTS_PER_KRONA, 2, RoundingMode.HALF_EVEN);
    }

    public void setVatAsKrona(BigDecimal valueAsKrona) {
        this.vat = valueAsKrona.multiply(
                CurrencyConstants.PARTS_PER_KRONA).longValue();
    }

    public BigDecimal getAmountIncludingVatAsKrona() {
        BigDecimal amountDecimal = new BigDecimal(amount);
        BigDecimal vatDecimal = new BigDecimal(vat);

        return amountDecimal.add(vatDecimal)
                .divide(CurrencyConstants.PARTS_PER_KRONA,
                        2,
                        RoundingMode.HALF_EVEN);
    }

    public void setAmountIncludingVatAsKrona(BigDecimal valueAsKrona) {
        BigDecimal value = valueAsKrona.multiply(
                CurrencyConstants.PARTS_PER_KRONA);

        BigDecimal amountDecimal = value.multiply(new BigDecimal("0.8"));
        BigDecimal vatDecimal = value.subtract(amountDecimal);

        this.amount = amountDecimal.setScale(0, RoundingMode.HALF_DOWN).longValue();
        this.vat = vatDecimal.setScale(0, RoundingMode.HALF_UP).longValue();
    }

    public BigDecimal calculateGrantsAmountSumAsKrona() {
        if (grants == null) {
            return new BigDecimal(0);
        }

        long sum = sumGrantsAmount();
        BigDecimal sumDecimal = new BigDecimal(sum);

        return sumDecimal.divide(
                CurrencyConstants.PARTS_PER_KRONA, 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateGrantsVatSumAsKrona() {
        if (grants == null) {
            return new BigDecimal(0);
        }

        long sum = sumGrantsVat();
        BigDecimal sumDecimal = new BigDecimal(sum);

        return sumDecimal.divide(
                CurrencyConstants.PARTS_PER_KRONA, 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateGrantsAmountIncludingVatSumAsKrona() {
        if (grants == null) {
            return new BigDecimal(0);
        }

        long sum = sumGrantsAmountIncludingVat();
        BigDecimal sumDecimal = new BigDecimal(sum);

        return sumDecimal.divide(
                CurrencyConstants.PARTS_PER_KRONA, 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateDifferenceExcludingVatAsKrona() {
        long result = this.amount;

        result = result - sumGrantsAmount();

        BigDecimal resultDecimal = new BigDecimal(result);

        return resultDecimal.divide(
                CurrencyConstants.PARTS_PER_KRONA, 2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateDifferenceIncludingVatAsKrona() {
        long result = this.amount + vat;

        result = result - sumGrantsAmountIncludingVat();

        BigDecimal resultDecimal = new BigDecimal(result);

        return resultDecimal.divide(
                CurrencyConstants.PARTS_PER_KRONA, 2, RoundingMode.HALF_EVEN);
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
        return "Invoice{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", invoiceDate=" + invoiceDate +
                ", verificationNumber='" + verificationNumber + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", vat=" + vat +
                ", amount=" + amount +
                ", grants=" + grants +
                ", supplier=" + supplier +
                ", adjustment=" + adjustment +
                '}';
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
