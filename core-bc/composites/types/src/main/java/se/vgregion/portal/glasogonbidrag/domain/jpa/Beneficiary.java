package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vgr_glasogonbidrag_beneficiary")
@NamedQueries({
        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findWithParts",
                query = "SELECT b FROM Beneficiary b " +
                        "LEFT JOIN FETCH b.grants " +
                        "WHERE b.id = :id"),
        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findWithPartsByIdent",
                query = "SELECT b FROM Beneficiary b " +
                        "LEFT JOIN FETCH b.grants " +
                        "WHERE b.identification = :id"),

        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findAll",
                query = "SELECT b FROM Beneficiary b " +
                        "ORDER BY b.id ASC"),
        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findAllOrderByFirstName",
                query = "SELECT b FROM Beneficiary b " +
                        "ORDER BY b.firstName, b.lastName ASC"),
        @NamedQuery(
                name = "glasogonbidrag.beneficiary.findAllOrderByLastName",
                query = "SELECT b FROM Beneficiary b " +
                        "ORDER BY b.lastName, b.firstName ASC"),

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

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "beneficiary")
    private List<Grant> grants;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "identification_id")
    private Identification identification;

//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "prescription_id")
//    private Prescription prescription;

    public Beneficiary() {
        grants = new ArrayList<>();
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Grant> getGrants() {
        return grants;
    }

    public void setGrants(List<Grant> grants) {
        this.grants = grants;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

//    public Prescription getPrescription() {
//        return prescription;
//    }
//
//    public void setPrescription(Prescription prescription) {
//        this.prescription = prescription;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Beneficiary that = (Beneficiary) o;

        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null)
            return false;
        return identification.equals(that.identification);

    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + identification.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Beneficiary{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", grants=" + grants +
                ", identification=" + identification +
                '}';
    }

}
