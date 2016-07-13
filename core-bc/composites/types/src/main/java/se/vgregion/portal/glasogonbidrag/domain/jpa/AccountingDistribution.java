package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologes AB
 */
@Entity
@NamedQueries({
        @NamedQuery(
                name = "glasogonbidrag.distribution.findAll",
                query = "SELECT ad " +
                        "FROM AccountingDistribution ad " +
                        "ORDER BY ad.id ASC"),
        @NamedQuery(
                name = "glasogonbidrag.distribution.findAllWithParts",
                query = "SELECT ad " +
                        "FROM AccountingDistribution ad " +
                        "LEFT JOIN FETCH ad.rows " +
                        "ORDER BY ad.id ASC"),
        @NamedQuery(
                name = "glasogonbidrag.distribution.findWithParts",
                query = "SELECT ad " +
                        "FROM AccountingDistribution ad " +
                        "LEFT JOIN FETCH ad.rows " +
                        "WHERE ad.id = :id")
})
@Table(name = "vgr_glasogonbidrag_account_distribution")
public class AccountingDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    // Liferay Related

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    // Accounting Distribution Specific

    @OneToMany(mappedBy = "distribution")
    private List<AccountRow> rows;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<AccountRow> getRows() {
        return rows;
    }

    public void setRows(List<AccountRow> rows) {
        this.rows = rows;
    }

    public void addRow(AccountRow row) {
        rows.add(row);
    }
}
