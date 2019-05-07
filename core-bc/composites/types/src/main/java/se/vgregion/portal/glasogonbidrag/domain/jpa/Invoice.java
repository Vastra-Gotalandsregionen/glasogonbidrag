package se.vgregion.portal.glasogonbidrag.domain.jpa;

import org.hibernate.annotations.Type;
import se.vgregion.portal.glasogonbidrag.domain.IdentificationType;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;

import javax.persistence.Basic;
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
import javax.persistence.Lob;
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
import java.util.*;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Table(name = "invoice")
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
                        "LEFT JOIN FETCH i.grants g " +
                        "LEFT JOIN FETCH g.prescription p " +
                        "LEFT JOIN FETCH p.diagnose " +
                        "LEFT JOIN FETCH i.supplier " +
                        "LEFT JOIN FETCH i.distribution " +
                        "WHERE i.id = :id"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findByVerificationNumber",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "LEFT JOIN FETCH i.grants " +
                        "LEFT JOIN FETCH i.supplier " +
                        "WHERE i.verificationNumber = :number"),

        @NamedQuery(
                name = "glasogonbidrag.invoice.findAll",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "ORDER BY i.id ASC"),

        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllWithParts",
                query = "SELECT DISTINCT i " +
                        "FROM Invoice i " +
                        "LEFT JOIN FETCH i.grants " +
                        "LEFT JOIN FETCH i.supplier " +
                        "ORDER BY i.id ASC"),

        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllOrderByModificationDate",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "ORDER BY i.modifiedDate DESC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice" +
                        ".findAllByUserOrderByModificationDate",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "WHERE i.userId = :user " +
                        "ORDER BY i.modifiedDate DESC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllByInvoiceNumber",
                query = "SELECT i " +
                        "FROM Invoice i " +
                        "LEFT JOIN FETCH i.grants " +
                        "LEFT JOIN FETCH i.supplier " +
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
                        "ORDER BY i.id ASC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllByCaseWorker",
                query = "SELECT i FROM Invoice i " +
                        "WHERE i.caseWorker = :caseWorker " +
                        "ORDER BY i.id ASC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice" +
                        ".findAllByStatusOrderByModificationDate",
                query = "SELECT i FROM Invoice i " +
                        "WHERE i.status = :status " +
                        "ORDER BY i.modifiedDate DESC"),
        @NamedQuery(
                name = "glasogonbidrag.invoice.findAllByStatusAndUser",
                query = "SELECT i FROM Invoice i " +
                        "WHERE i.status = :status " +
                        "  AND i.userId = :user " +
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

    @Column(name = "verification_number", nullable = false, unique = true)
    private String verificationNumber;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    private long amount;

    @Column(name = "case_worker")
    private String caseWorker;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Basic(fetch=FetchType.EAGER, optional=true)
    @Column(name = "invoice_notes", columnDefinition = "text")
    private String notes;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "invoice")
    private Set<Grant> grants;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "migration")
    private Migration migration;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @OneToOne
    @JoinColumn(name = "distribution")
    private AccountingDistribution distribution;

    @Transient
    private final KronaCalculationUtil currency =
            new KronaCalculationUtil();

    public Invoice() {
        grants = new HashSet<>();
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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCaseWorker() {
        return caseWorker;
    }

    public void setCaseWorker(String caseWorker) {
        this.caseWorker = caseWorker;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Grant> getGrants() {
        return grants;
    }

    public void setGrants(Set<Grant> grants) {
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

    public Migration getMigration() {
        return migration;
    }

    public void setMigration(Migration migration) {
        this.migration = migration;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public AccountingDistribution getDistribution() {
        return distribution;
    }

    public void setDistribution(AccountingDistribution distribution) {
        this.distribution = distribution;
    }

    // Public helper methods.

    public BigDecimal getAmountAsKrona() {
        return currency.calculatePartsAsKrona(amount);
    }

    public void setAmountAsKrona(BigDecimal valueAsKrona) {
        this.amount = currency.calculateKronaAsParts(valueAsKrona);
    }

    // Helper to calculate amount into krona.

    public BigDecimal calculateGrantsAmountSumAsKrona() {
        if (grants == null) {
            return new BigDecimal(0);
        }

        long sum = sumGrantsAmount();

        return currency.calculatePartsAsKrona(sum);
    }

    public BigDecimal calculateDifferenceAsKrona() {
        long result = this.amount;

        result = result - sumGrantsAmount();

        return currency.calculatePartsAsKrona(result);
    }

    public BigDecimal calculateInvoiceAmountAsKrona() {
        return currency.calculatePartsAsKrona(this.amount);
    }

    // Helper to sort grants in lists

    public List<Grant> getOrderedGrants() {
        List<Grant> grants = new ArrayList<>(this.grants);
        Collections.sort(grants, new Comparator<Grant>() {
            @Override
            public int compare(Grant o1, Grant o2) {
                String o1Number = o1.getBeneficiary().getIdentification().getNumber();
                String o2Number = o2.getBeneficiary().getIdentification().getNumber();

                IdentificationType i1 = o1.getBeneficiary().getIdentification().getType();
                IdentificationType i2 = o1.getBeneficiary().getIdentification().getType();

                if (i1.equals(i2)) {
                    return o1Number.compareToIgnoreCase(o2Number);
                }

                return i1.compareTo(i2);
            }
        });

        return grants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        return verificationNumber != null ? verificationNumber.equals(invoice.verificationNumber) : invoice.verificationNumber == null;

    }

    @Override
    public int hashCode() {
        return verificationNumber != null ? verificationNumber.hashCode() : 0;
    }

    @Override
    public String toString() {
        String grantString;
        try {
            grantString = ", grants=" + grants;
        } catch (Exception e) {
            grantString = ", grants=notloaded";
        }

        return "Invoice{" + "id=" + id +
                ", userId=" + userId +
                ", groupId=" + groupId +
                ", companyId=" + companyId +
                ", verificationNumber='" + verificationNumber + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", amount=" + amount +
                ", supplier=" + supplier +
                grantString +
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
}
