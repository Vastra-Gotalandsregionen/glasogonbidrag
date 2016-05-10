package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vgr_glasogonbidrag_grant_adjustment")
public class GrantAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    @OneToOne
    @JoinColumn(name = "invoice")
    private Invoice invoice;

    public GrantAdjustment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GrantAdjustment that = (GrantAdjustment) o;

        if (amount != that.amount) return false;
        return invoice != null ? invoice.equals(that.invoice) : that.invoice == null;

    }

    @Override
    public int hashCode() {
        int result = amount;
        result = 31 * result + (invoice != null ? invoice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GrantAdjustment{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}
