package se.vgregion.portal.glasogonbidrag.domain.jpa;

import se.vgregion.portal.glasogonbidrag.domain.internal.KronaCalculationUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Table(name = "account_row")
public class AccountRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int count;
    private int responsibility;
    private int account;

    @Column(name = "free_code")
    private int freeCode;

    @Column(name = "amount")
    private long amount;

    @ManyToOne
    @JoinColumn(name = "distribution_id")
    private AccountingDistribution distribution;

    @Transient
    private final KronaCalculationUtil currency =
            new KronaCalculationUtil();

    /**
     * Default constructor.
     */
    public AccountRow() {
    }

    public AccountRow(int responsibility,
                      int account, int freeCode,
                      long amount) {
        this(1, responsibility, account, freeCode, amount);
    }

    public AccountRow(int count, int responsibility,
                      int account, int freeCode,
                      long amount) {
        this(null, count, responsibility, account, freeCode, amount);
    }

    public AccountRow(AccountingDistribution distribution,
                      int count, int responsibility,
                      int account, int freeCode,
                      long amount) {
        this.distribution = distribution;

        this.count = count;
        this.responsibility = responsibility;
        this.account = account;
        this.freeCode = freeCode;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(int responsibility) {
        this.responsibility = responsibility;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public int getFreeCode() {
        return freeCode;
    }

    public void setFreeCode(int freeCode) {
        this.freeCode = freeCode;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public AccountingDistribution getDistribution() {
        return distribution;
    }

    public void setDistribution(AccountingDistribution distribution) {
        this.distribution = distribution;
    }

    // Public helper to add and remove amount excl. VAT on the row.

    public void incrementAmount(long amount) {
        this.amount = this.amount + amount;
    }

    public void decrementAmount(long amount) {
        this.amount = this.amount - amount;
    }

    // Public helper to manipulate count

    public void incrementCount() {
        incrementCount(1);
    }

    public void incrementCount(int count) {
        this.count = this.count + count;
    }

    public void decrementCount() {
        decrementCount(1);
    }

    public void decrementCount(int count) {
        this.count = this.count - count;
    }

    // Public helper to transform amount as krona

    public BigDecimal getAmountAsKrona() {
        return currency.calculatePartsAsKrona(amount);
    }

    public void setAmountAsKrona(BigDecimal valueAsKrona) {
        amount = currency.calculateKronaAsParts(valueAsKrona);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountRow that = (AccountRow) o;

        if (account != that.account) return false;
        if (freeCode != that.freeCode) return false;
        if (responsibility != that.responsibility) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = responsibility;
        result = 31 * result + account;
        result = 31 * result + freeCode;
        return result;
    }

    @Override
    public String toString() {
        return "AccountRow{" +
                "count=" + count +
                ", responsibility=" + responsibility +
                ", account=" + account +
                ", freeCode=" + freeCode +
                ", amount=" + amount +
                '}';
    }
}
