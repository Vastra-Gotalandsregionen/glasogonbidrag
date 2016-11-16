package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "vgr_glasogonbidrag_beneficiary")
@NamedQueries({
        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findWithParts",
                query = "SELECT DISTINCT b " +
                        "FROM Beneficiary b " +
                        "LEFT JOIN FETCH b.grants " +
                        "LEFT JOIN FETCH b.prescriptionHistory " +
                        "WHERE b.id = :id"),
        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findWithPartsByIdent",
                query = "SELECT DISTINCT b " +
                        "FROM Beneficiary b " +
                        "LEFT JOIN FETCH b.grants " +
                        "LEFT JOIN FETCH b.prescriptionHistory " +
                        "WHERE b.identification = :id"),

        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findAll",
                query = "SELECT b " +
                        "FROM Beneficiary b " +
                        "ORDER BY b.id ASC"),
        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findAllWithParts",
                query = "SELECT DISTINCT b " +
                        "FROM Beneficiary b " +
                        "LEFT JOIN FETCH b.grants " +
                        "LEFT JOIN FETCH b.prescriptionHistory " +
                        "ORDER BY b.id ASC"),

        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findAllOrderByName",
                query = "SELECT b " +
                        "FROM Beneficiary b " +
                        "ORDER BY b.fullName ASC"),

})
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liferay Related

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    // Beneficiary Specific

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "beneficiary")
    private Set<Grant> grants;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "identification_id")
    private Identification identification;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "beneficiary")
    //@OrderColumn(name = "prescription_date")
    //@OrderBy("date DESC")
    //@OrderColumn(name = "sort_index")
    private Set<Prescription> prescriptionHistory;

    public Beneficiary() {
        grants = new HashSet<>();
        prescriptionHistory = new HashSet<>();
//        prescription = new Prescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Grant> getGrants() {
        return grants;
    }

    public void setGrants(Set<Grant> grants) {
        this.grants = grants;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    public Set<Prescription> getPrescriptionHistory() {
        return prescriptionHistory;
    }

    public void setPrescriptionHistory(Set<Prescription> prescriptionHistory) {
        this.prescriptionHistory = prescriptionHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Beneficiary that = (Beneficiary) o;

        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null)
            return false;
        if (identification != null ? !identification.equals(that.identification) : that.identification != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fullName != null ? fullName.hashCode() : 0;
        result = 31 * result + (identification != null ? identification.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Beneficiary{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", identification=" + identification.toString() +
                '}';
    }
}
