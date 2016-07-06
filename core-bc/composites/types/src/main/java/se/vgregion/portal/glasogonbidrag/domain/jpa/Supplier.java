package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "vgr_glasogonbidrag_supplier")
@NamedQueries({
        @NamedQuery(
                name = "glasogonbidrag.supplier.findAll",
                query = "SELECT s " +
                        "FROM Supplier s " +
                        "ORDER BY s.name ASC"),
        @NamedQuery(
                name = "glasogonbidrag.supplier.findAllActive",
                query = "SELECT s " +
                        "FROM Supplier s " +
                        "WHERE s.active = TRUE " +
                        "ORDER BY s.name ASC"),
        @NamedQuery(
                name = "glasogonbidrag.supplier.findAllInactive",
                query = "SELECT s " +
                        "FROM Supplier s " +
                        "WHERE s.active = FALSE " +
                        "ORDER BY s.name ASC"),
        @NamedQuery(
                name = "glasogonbidrag.supplier.findAllByName",
                query = "SELECT s " +
                        "FROM Supplier s " +
                        "WHERE s.name = :name " +
                        "ORDER BY s.name ASC"),
})
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Liferay Related

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    // Supplier specific

    @Column(name = "supplier_name")
    private String name;

    private boolean active;

    @Column(name = "external_service_id")
    private String externalServiceId;

    public Supplier() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalServiceId() {
        return externalServiceId;
    }

    public void setExternalServiceId(String externalServiceId) {
        this.externalServiceId = externalServiceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Supplier supplier = (Supplier) o;

        if (externalServiceId != null ? !externalServiceId.equals(supplier.externalServiceId) : supplier.externalServiceId != null)
            return false;
        if (name != null ? !name.equals(supplier.name) : supplier.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (externalServiceId != null ? externalServiceId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "externalServiceId='" + externalServiceId + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
