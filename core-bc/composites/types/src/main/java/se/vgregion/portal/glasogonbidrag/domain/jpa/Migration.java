package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "vgr_glasogonbidrag_migration")
@NamedQueries(value = {
        @NamedQuery(
                name = "glasogonbidrag.migration.findAllWithPartsByDate",
                query = "SELECT m " +
                        "FROM Migration m " +
                        "LEFT JOIN FETCH m.invoices " +
                        "WHERE m.date = :date"),

        @NamedQuery(
                name = "glasogonbidrag.migration.findAllDates",
                query = "SELECT DISTINCT m.date " +
                        "FROM Migration m"),
})
public class Migration {
    @Id
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "migration_date")
    private Date date;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "migration")
    private Set<Invoice> invoices;

    public Migration() {
        invoices = new HashSet<>();
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

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public void addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
    }

    public void removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
    }
}
