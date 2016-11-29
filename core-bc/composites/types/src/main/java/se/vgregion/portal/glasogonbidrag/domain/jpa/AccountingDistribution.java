package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologes AB
 */
@Entity
@Table(name = "account_distribution")
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
public class AccountingDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

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

    // Accounting Distribution Specific

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "distribution",
            cascade = CascadeType.ALL)
    private List<AccountRow> rows;

    public AccountingDistribution() {
        rows = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<AccountRow> getRows() {
        return rows;
    }

    public void setRows(List<AccountRow> rows) {
        this.rows = rows;
    }

    public void addRow(AccountRow row) {
        row.setDistribution(this);

        rows.add(row);
    }

    public void appendRow(AccountRow row) {
        int index = rows.indexOf(row);

        if (index == -1) {
            addRow(row);
        } else {
            rows.get(index).incrementCount(row.getCount());
            rows.get(index).incrementAmount(row.getAmount());
        }
    }
}
