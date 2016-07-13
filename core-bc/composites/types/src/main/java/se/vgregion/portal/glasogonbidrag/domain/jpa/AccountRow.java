package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Entity
@Table(name = "vgr_glasogonbidrag_account_row")
public class AccountRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int count;
    private int responsibility;
    private int account;

    @Column(name = "free_code")
    private int freeCode;

    @Column(name = "amount_excl_vat")
    private long amountExclVat;

    @ManyToOne
    @JoinColumn(name = "distribution_id")
    private AccountingDistribution distribution;

    /**
     * Default constructor.
     */
    public AccountRow() {
    }

    public AccountRow(AccountingDistribution distribution,
                      int count, int responsibility,
                      int account, int amountExclVat) {
        this.distribution = distribution;

        this.count = count;
        this.responsibility = responsibility;
        this.account = account;
        this.amountExclVat = amountExclVat;
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

    public long getAmountExclVat() {
        return amountExclVat;
    }

    public void setAmountExclVat(long amountExclVat) {
        this.amountExclVat = amountExclVat;
    }

    public AccountingDistribution getDistribution() {
        return distribution;
    }

    public void setDistribution(AccountingDistribution distribution) {
        this.distribution = distribution;
    }
}
